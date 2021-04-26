package com.dismoi.scout.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.dismoi.scout.MainActivity
import com.dismoi.scout.R
import com.facebook.react.HeadlessJsTaskService

class BackgroundService : AccessibilityService() {
  private var _url: String? = "this should be url"
  private val handler = Handler()
  private val runnableCode: Runnable = object : Runnable {
    override fun run() {
      val context = applicationContext
      val myIntent = Intent(context, BackgroundEventService::class.java)
      val bundle = Bundle()

      bundle.putString("url", _url)

      myIntent.putExtras(bundle)

      context.startService(myIntent)
      HeadlessJsTaskService.acquireWakeLockNow(context)
    }
  }

  private val previousUrlDetections: HashMap<String, Long> = HashMap()

  override fun onServiceConnected() {
    val info = serviceInfo
    info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
    info.packageNames = packageNames()
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
    // throttling of accessibility event notification
    info.notificationTimeout = 300
    // support ids interception
    info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
      AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
    this.serviceInfo = info

    val startActivity: Intent? = applicationContext.getPackageManager().getLaunchIntentForPackage(
      applicationContext.getPackageName()
    )
    startActivity!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

    startActivity(startActivity)
  }

  @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  private fun captureUrl(info: AccessibilityNodeInfo, config: SupportedBrowserConfig): String? {
    val nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId)
    if (nodes == null || nodes.size <= 0) {
      return null
    }
    val addressBarNodeInfo = nodes[0]
    var url: String? = null
    if (addressBarNodeInfo.text != null) {
      url = addressBarNodeInfo.text.toString()
    }
    addressBarNodeInfo.recycle()
    return url
  }

  @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  override fun onAccessibilityEvent(event: AccessibilityEvent) {

    val parentNodeInfo = event.source ?: return
    val packageName = event.packageName.toString()
    var browserConfig: SupportedBrowserConfig? = null
    for (supportedConfig in getSupportedBrowsers()) {
      if (supportedConfig.packageName == packageName) {
        browserConfig = supportedConfig
      }
    }

    // this is not supported browser, so exit
    if (browserConfig == null) {
      return
    }
    val capturedUrl = captureUrl(parentNodeInfo, browserConfig)
    parentNodeInfo.recycle()

    // we can't find a url. Browser either was updated or opened page without url text field
    if (capturedUrl == null) {
      return
    }
    val eventTime = event.eventTime
    val detectionId = "$packageName, and url $capturedUrl"
    val lastRecordedTime =
      if (previousUrlDetections.containsKey(detectionId)) {
        previousUrlDetections[detectionId]!!
      } else 0.toLong()
    // some kind of redirect throttling
    if (eventTime - lastRecordedTime > 2000) {
      previousUrlDetections[detectionId] = eventTime

      val className = event.className

      if (className == "android.widget.FrameLayout") {
        handler.post(runnableCode)
      } else {
        _url = capturedUrl
        handler.post(runnableCode)
      }
    }
  }

  override fun onInterrupt() {
    TODO("Not yet implemented")
  }

  private fun packageNames(): Array<String> {
    val packageNames: MutableList<String> = ArrayList()
    for (config in getSupportedBrowsers()) {
      packageNames.add(config.packageName)
    }
    return packageNames.toTypedArray()
  }

  private class SupportedBrowserConfig(var packageName: String, var addressBarId: String)

  /** @return a list of supported browser configs
   * This list could be instead obtained from remote server to support future browser updates without updating an app
   */
  private fun getSupportedBrowsers(): List<SupportedBrowserConfig> {
    val browsers: MutableList<SupportedBrowserConfig> = ArrayList()
    browsers.add(SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"))
    return browsers
  }

  private fun createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(CHANNEL_ID, "BACKGROUND", importance)
      channel.description = "CHANEL DESCRIPTION"
      val notificationManager = getSystemService(NotificationManager::class.java)
      notificationManager.createNotificationChannel(channel)
    }
  }

  override fun onCreate() {
    super.onCreate()
  }

  override fun onDestroy() {
    super.onDestroy()
    handler.removeCallbacks(runnableCode)
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    createNotificationChannel()
    val notificationIntent = Intent(this, MainActivity::class.java)
    val contentIntent = PendingIntent.getActivity(
      this,
      0,
      notificationIntent,
      PendingIntent.FLAG_CANCEL_CURRENT
    )
    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("DisMoi")
      .setContentText("DisMoi is running in background...")
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(contentIntent)
      .setOngoing(true)
      .build()
    startForeground(SERVICE_NOTIFICATION_ID, notification)
    return START_STICKY
  }

  companion object {
    private const val SERVICE_NOTIFICATION_ID = 12345
    private const val CHANNEL_ID = "BACKGROUND"
  }
}
