package com.tommannson.bodystats.infrastructure.notifications.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tommannson.bodystats.infrastructure.notifications.AlertPointerUpdater
import com.tommannson.bodystats.infrastructure.notifications.PendingIntentCompat.getForegroundService
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.ChronoUnit
import java.util.*

//import com.tommannson.bodystats.infrastructure.notifications.DailyRemainderService;
class AlarmCreator {
    fun setAlarm(context: Context, newTimeToSet: LocalDateTime, alarmRequestId: Int) {
        val instant = newTimeToSet.toInstant(OffsetDateTime.now().getOffset())
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timeOfEvent = newTimeToSet.toInstant(ZoneOffset.UTC)
        val i = AlertPointerUpdater.starter(context = context, milis = timeOfEvent.toEpochMilli())
        val pi = getForegroundService(context, alarmRequestId, i, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, instant.toEpochMilli(), pi)
        } else {
            am[AlarmManager.RTC_WAKEUP, instant.toEpochMilli()] = pi
        }
    }

//    fun setAlarmPeriodic(
//        context: Context,
//        newTimeToSet: DateTime,
//        period: Period,
//        alarmRequestId: Int
//    ) {
//        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val i = Intent(context, DailyRemainderService::class.java)
//        val pi = getForegroundService(context, alarmRequestId, i, 0)
//        am.setRepeating(AlarmManager.RTC_WAKEUP, newTimeToSet.getMillis(), period.getMillis(), pi)
//    }

    fun cancelAlarm(context: Context, alarmRequestId: Int) {
        val intent = Intent(context, AlertPointerUpdater::class.java)
        val sender = getForegroundService(context, alarmRequestId, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}