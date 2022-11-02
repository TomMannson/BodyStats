package com.tommannson.bodystats.infrastructure.notifications

import android.content.Intent
import android.app.PendingIntent
import android.content.Context
import android.os.Build

object PendingIntentCompat {
    fun getForegroundService(
        context: Context?,
        requestCode: Int,
        intent: Intent?,
        flags: Int
    ): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(context, requestCode, intent!!, flags)
        } else {
            PendingIntent.getService(context, requestCode, intent!!, flags)
        }
    }

    fun getBroadcast(
        context: Context?,
        requestCode: Int,
        intent: Intent?,
        flags: Int
    ): PendingIntent {
        return PendingIntent.getBroadcast(context, requestCode, intent!!, flags)
    }
}