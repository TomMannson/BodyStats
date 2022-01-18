package com.tommannson.bodystats.infrastructure.notifications

import android.app.IntentService
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.tommannson.bodystats.infrastructure.configuration.AppDatabase
import com.tommannson.bodystats.infrastructure.configuration.ReminderDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import com.tommannson.bodystats.infrastructure.notifications.alarms.AlarmCreator
import com.tommannson.bodystats.infrastructure.notifications.notificationorganizer.NotificationInvoker
import com.tommannson.bodystats.model.reminding.ReminderType
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import javax.inject.Inject

/**
 * Created by tomasz.krol on 2017-05-18.
 */
@AndroidEntryPoint
class AlertPointerUpdater : IntentService("AlertPointerUpdaterThread") {
    @Inject
    lateinit var notificationRepo: ReminderDao

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var database: AppDatabase
    var producer: NotificationInvoker = NotificationInvoker()
    var alarmCreator: AlarmCreator = AlarmCreator()


    override fun onHandleIntent(intent: Intent?) {

        startForeground(ALERT_CRON_ID, producer.buildForegroundNotification(this))

        val triggeredTime = getLocalDateTime(intent ?: Intent())

        val user = userDao.getAll().first()
        val result = notificationRepo.findEnabledReminderInstancesByOwnerWhichWillTriggerAfter(
            user.id,
            LocalDateTime.now()
        ).firstOrNull()

        if (triggeredTime != null) {
            val allReminders =
                notificationRepo.findAllEnabledReminderInstancesByOwnerWithSpecificTime(
                    user.id,
                    triggeredTime
                )

            producer.showReminderNotification(
                this,
                allReminders.map { ReminderType.valueOf(it.type) }.distinct()
            )
        }

        alarmCreator.cancelAlarm(this, ALARM_REMINDER_ID)
        if (result != null) {
            alarmCreator.setAlarm(this, result.reminderDayTime, ALARM_REMINDER_ID)
        }
        stopForeground(true)
    }

    companion object {
        const val ALARM_REMINDER_ID = 1
        const val ALERT_CRON_ID = 3
        const val ALERT_STATS_CHANNEL_ID = "ALERT_STATS_CHANNEL_ID"
        const val MEASUREMENTS_CHANNEL_ID = "MEASUREMENTS_CHANNEL_ID"
        const val TRIGGERED_DATE_TIME = "TRIGGERED_DATE_TIME"

        fun starter(context: Context, milis: Long): Intent {
            val args = bundleOf(
                TRIGGERED_DATE_TIME to milis,
            )
            return starter(context).apply {
                putExtras(args)
            }
        }

        fun starter(context: Context) = Intent(context, AlertPointerUpdater::class.java)

        fun getLocalDateTime(intent: Intent): LocalDateTime? {
            val time = intent.extras?.getLong(TRIGGERED_DATE_TIME, 0L) ?: return null
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC)
        }
    }
}
