package com.tommannson.bodystats.infrastructure.notifications.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.tommannson.bodystats.infrastructure.notifications.AlertPointerUpdater
import com.tommannson.bodystats.infrastructure.notifications.PendingIntentCompat
import com.tommannson.bodystats.infrastructure.notifications.PendingIntentCompat.getBroadcast
import com.tommannson.bodystats.infrastructure.notifications.PendingIntentCompat.getForegroundService
import com.tommannson.bodystats.infrastructure.notifications.ReminderReceiver
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

class AlarmCreator {
    fun setAlarm(context: Context, newTimeToSet: LocalDateTime, alarmRequestId: Int) {
        val instant = newTimeToSet.toInstant(OffsetDateTime.now().getOffset())
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timeOfEvent = newTimeToSet.toInstant(ZoneOffset.UTC)
        val intent = ReminderReceiver.createTrigger(context, timeOfEvent.toEpochMilli())
        val flagsForIntent = createFlagsForPendingIntent()
        val pi = getBroadcast(context, alarmRequestId, intent, flagsForIntent)

//        AlarmManagerCompat.setAlarmClock(am, instant.toEpochMilli(), pi, pi)
        AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, instant.toEpochMilli(), pi)
    }

    private fun createFlagsForPendingIntent() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

    fun cancelAlarm(context: Context, alarmRequestId: Int) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val flagsForIntent = 0 // for cancel this is enough
        val sender = getBroadcast(context, alarmRequestId, intent, flagsForIntent)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}