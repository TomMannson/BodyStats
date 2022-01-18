package com.tommannson.bodystats.infrastructure.notifications.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tommannson.bodystats.infrastructure.notifications.AlertPointerUpdater
import com.tommannson.bodystats.infrastructure.notifications.PendingIntentCompat.getForegroundService
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

class AlarmCreator {
    fun setAlarm(context: Context, newTimeToSet: LocalDateTime, alarmRequestId: Int) {
        val instant = newTimeToSet.toInstant(OffsetDateTime.now().getOffset())
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timeOfEvent = newTimeToSet.toInstant(ZoneOffset.UTC)
        val i = AlertPointerUpdater.starter(context = context, milis = timeOfEvent.toEpochMilli())
        val pi = getForegroundService(
            context,
            alarmRequestId,
            i,
            createFlagsForPendingIntent()
        )

        am.setAlarmClock(AlarmManager.AlarmClockInfo(instant.toEpochMilli(), pi), pi)
    }

    private fun createFlagsForPendingIntent() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

    fun cancelAlarm(context: Context, alarmRequestId: Int) {
        val intent = Intent(context, AlertPointerUpdater::class.java)
        val sender = getForegroundService(context, alarmRequestId, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}