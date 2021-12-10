package com.tommannson.bodystats.infrastructure.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.tommannson.bodystats.infrastructure.notifications.AlertPointerUpdater
//import com.tommannson.bodystats.infrastructure.notifications.DailyRemainderService
import android.widget.Toast

/**
 * Created by tomasz.krol on 2017-06-19.
 */
class ServiceStarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ContextCompat.startForegroundService(
            context,
            AlertPointerUpdater.starter(context)
        )
//        DailyRemainderService.startScheduleDaily(context, true)
        Toast.makeText(context, "BodyStats started", Toast.LENGTH_SHORT).show()
    }
}