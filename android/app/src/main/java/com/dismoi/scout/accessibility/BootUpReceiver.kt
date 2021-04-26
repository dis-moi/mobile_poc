package com.dismoi.scout.accessibility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootUpReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    if (intent.action === Intent.ACTION_BOOT_COMPLETED) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(Intent(context, BackgroundService::class.java))
        return
      }
      context.startService(Intent(context, BackgroundService::class.java))
    }
  }
}
