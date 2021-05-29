package com.dismoi.scout.floating

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dismoi.scout.R
import com.dismoi.scout.floating.layout.Bubble
import com.dismoi.scout.floating.layout.Bubble.OnBubbleClickListener
import com.dismoi.scout.floating.layout.Bubble.OnBubbleRemoveListener
import com.dismoi.scout.floating.layout.Message
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import org.jsoup.Jsoup

class FloatingModule(
  private val reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext),
  HighlightMessagesAdapter.onItemClickListener {
  private var bubblesManager: Manager? = Manager.Builder(reactContext).setTrashLayout(
    R.layout.bubble_trash
  ).setInitializationCallback(object : OnCallback {
    override fun onInitialized() {}
  }).build()

  private var messagesManager: Manager? = Manager.Builder(reactContext).setTrashLayout(
    R.layout.bubble_trash
  ).setInitializationCallback(object : OnCallback {
    override fun onInitialized() {}
  }).build()

  private var bubbleDisMoiView: Bubble? = null
  private var messageDisMoiView: Message? = null
  private var _size = 0
  private lateinit var _notices: ReadableArray
  private var _url = ""

  var verticalLayout: LinearLayout? = null

  @ReactMethod
  fun reopenApp() {
    val launchIntent = reactContext.packageManager.getLaunchIntentForPackage(
      reactContext.packageName
    )
    if (launchIntent != null) {
      reactContext.startActivity(launchIntent)
    }
  }

  override fun getName(): String {
    return "FloatingModule"
  }

  @ReactMethod
  fun showFloatingDisMoiBubble(
    x: Int,
    y: Int,
    numberOfNotice: Int,
    notices: ReadableArray,
    url: String,
    promise: Promise
  ) {
    Log.d("Notifications", "show floating dis moi module")
    bubblesManager!!.initialize()

    if (bubbleDisMoiView == null) {
      Log.d("Notifications", "INSIDE BUBBLE DISMOI VIEW")
      removeDisMoiMessage()
      _url = url
      _notices = notices
      _size = numberOfNotice
      addNewFloatingDisMoiBubble(x, y, numberOfNotice.toString())
      promise.resolve("")
    }
  }

  @ReactMethod
  fun showFloatingDisMoiMessage(x: Int, y: Int, promise: Promise) {
    messagesManager!!.initialize()

    if (messageDisMoiView == null) {
      try {
        removeDisMoiBubble()
        addNewFloatingDisMoiMessage(x, y)
        promise.resolve("")
      } catch (e: Exception) {
        promise.reject("0", "")
      }
    }
  }

  @ReactMethod
  fun hideFloatingDisMoiBubble(promise: Promise) {
    removeDisMoiBubble()
    promise.resolve("")
  }

  @ReactMethod
  fun receiveMessage(promise: Promise) {
    removeDisMoiBubble()
    promise.resolve("")
  }

  @ReactMethod
  fun hideFloatingDisMoiMessage(promise: Promise) {
    removeDisMoiMessage()
    removeDisMoiBubble()
    messageDisMoiView = null
    promise.resolve("")
  }

  @ReactMethod
  fun requestPermission(promise: Promise) {
    requestPermissionAction(promise)
  }

  @ReactMethod
  fun checkPermission(promise: Promise) {
    promise.resolve(hasPermission())
  }

/**
   * Searches for all URLSpans in current text replaces them with our own ClickableSpans
   * forwards clicks to provided function.
   */
  fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
    // create span builder and replaces current text with it
    text = SpannableStringBuilder.valueOf(text).apply {
      // search for all URL spans and replace all spans with our own clickable spans
      this.getSpans(0, length, URLSpan::class.java).forEach {
        // add new clickable span at the same position
        setSpan(
          object : ClickableSpan() {
            override fun onClick(widget: View) {
              onClicked?.invoke(it.url)
            }
          },
          getSpanStart(it),
          getSpanEnd(it),
          Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        // remove old URLSpan
        removeSpan(it)
      }
    }
    // make sure movement method is set
    movementMethod = LinkMovementMethod.getInstance()
  }

  fun String.toSpanned(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
      @Suppress("DEPRECATION")
      return Html.fromHtml(this)
    }
  }

  private fun addNewFloatingDisMoiMessageDetail(x: Int, y: Int, name: String?, message: String?) {
    removeDisMoiBubble()
    removeDisMoiMessage()

    messageDisMoiView = LayoutInflater.from(reactContext).inflate(
      R.layout.message, messageDisMoiView, false
    ) as Message

    var textView: TextView? = messageDisMoiView!!.findViewById(R.id.link)

    textView!!.text = message!!.toSpanned()

    var textViewContributorName: TextView? = messageDisMoiView!!.findViewById(R.id.name)

    textViewContributorName!!.text = name

    textView.handleUrlClicks { url ->
      val sharingIntent = Intent(Intent.ACTION_VIEW)
      sharingIntent.data = Uri.parse(url)
      sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      sharingIntent.setPackage("com.android.chrome")

      val bundle = Bundle()

      removeDisMoiMessage()
      removeDisMoiBubble()
      sendEventToReactNative("floating-dismoi-message-press")
      reactContext.startActivityForResult(sharingIntent, 0, bundle)
    }

    val imageButton = messageDisMoiView!!.findViewById<View>(R.id.close) as ImageButton
    imageButton.setOnClickListener { sendEventToReactNative("floating-dismoi-message-press") }

    messagesManager!!.addDisMoiMessage(messageDisMoiView!!, x, y)
  }

  private fun generateListForRecycleView(size: Int, notices: ReadableArray):
    ArrayList<HighlightMessage> {
      val list = ArrayList<HighlightMessage>()
      for (i in 0 until size) {
        val message: ReadableMap? = notices.getMap(i)
        var disMoiMessage: String? = message!!.getString("message")
        var disMoiContributorNameMap: ReadableMap? = message.getMap("contributor")

        var disMoiContributorName: String? = disMoiContributorNameMap!!.getString("name")

        var htmlToText = Jsoup.parse(disMoiMessage).text()

        var cutText = htmlToText!!.substring(0, 90)
        var cutToLastSpace = cutText.substringBeforeLast(" ")

        val item = HighlightMessage(disMoiContributorName, cutToLastSpace + "...", i.toString())
        list += item
      }
      return list
    }

  override fun onItemClick(position: Int) {
    val message: ReadableMap? = _notices.getMap(position)

    var disMoiContributorNameMap: ReadableMap? = message!!.getMap("contributor")

    var disMoiContributorName: String? = disMoiContributorNameMap!!.getString("name")

    var disMoiMessage: String? = message.getString("message")

    addNewFloatingDisMoiMessageDetail(10, 1500, disMoiContributorName, disMoiMessage)
  }

  private fun addNewFloatingDisMoiMessage(x: Int, y: Int) {
    removeDisMoiBubble()

    val highlistMessageList = generateListForRecycleView(_size, _notices)

    messageDisMoiView = LayoutInflater.from(reactContext).inflate(
      R.layout.highlight_messages, messageDisMoiView, false
    ) as Message

    val recycleViewHighlightMessageItems = messageDisMoiView!!.findViewById(
      R.id.recycler_view_highlight_messages
    ) as RecyclerView

    recycleViewHighlightMessageItems.adapter = HighlightMessagesAdapter(
      highlistMessageList, this
    )

    recycleViewHighlightMessageItems.layoutManager = LinearLayoutManager(reactContext)

    val imageButton = messageDisMoiView!!.findViewById<View>(R.id.close) as ImageButton
    imageButton.setOnClickListener {
      bubbleDisMoiView = null
      sendEventToReactNative("floating-dismoi-message-press")
    }

    messagesManager!!.addDisMoiMessage(messageDisMoiView!!, x, y)
  }

  private fun addNewFloatingDisMoiBubble(x: Int, y: Int, numberOfNotice: String) {

    bubbleDisMoiView = LayoutInflater.from(reactContext).inflate(
      R.layout.bubble, bubbleDisMoiView, false
    ) as Bubble

    var textView: TextView? = bubbleDisMoiView!!.findViewById(R.id.number_of_notice)
    textView!!.text = numberOfNotice

    bubbleDisMoiView!!.setOnBubbleRemoveListener(object : OnBubbleRemoveListener {
      override fun onBubbleRemoved(bubble: Bubble?) {
        bubbleDisMoiView = null
        sendEventToReactNative("floating-dismoi-bubble-remove")
      }
    })
    bubbleDisMoiView!!.setOnBubbleClickListener(object : OnBubbleClickListener {
      override fun onBubbleClick(bubble: Bubble?) {
        sendEventToReactNative("floating-dismoi-bubble-press")
      }
    })
    bubbleDisMoiView!!.setShouldStickToWall(true)

    bubblesManager!!.addDisMoiBubble(bubbleDisMoiView, x, y)
  }

  private fun hasPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      Settings.canDrawOverlays(reactContext)
    } else true
  }

  private fun removeDisMoiBubble() {
    if (bubbleDisMoiView != null) {
      bubblesManager!!.removeBubble(bubbleDisMoiView)
    }
  }

  private fun removeDisMoiMessage() {
    if (messageDisMoiView != null) {
      messagesManager!!.removeDisMoiMessage(messageDisMoiView)
    }
  }

  fun requestPermissionAction(promise: Promise) {
    if (!hasPermission()) {
      var intent: Intent? = null
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        intent = Intent(
          Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
          Uri.parse(
            "package:" + reactContext.packageName
          )
        )
      }
      val bundle = Bundle()
      reactContext.startActivityForResult(intent, 0, bundle)
    }
    if (hasPermission()) {
      promise.resolve("")
    } else {
      promise.reject("0", "")
    }
  }

  private fun sendEventToReactNative(eventName: String) {
    val params = Arguments.createMap()
    reactContext
      .getJSModule(RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }
}
