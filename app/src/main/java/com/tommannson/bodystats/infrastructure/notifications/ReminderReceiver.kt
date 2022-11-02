package com.tommannson.bodystats.infrastructure.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.tommannson.bodystats.infrastructure.ApplicationUser
import com.tommannson.bodystats.infrastructure.ReminderInstance
import com.tommannson.bodystats.infrastructure.configuration.ReminderDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import com.tommannson.bodystats.infrastructure.logs.LogDataSource
import com.tommannson.bodystats.infrastructure.notifications.notificationorganizer.NotificationInvoker
import com.tommannson.bodystats.model.reminding.ReminderType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationRepo: ReminderDao

    @Inject
    lateinit var logDao: LogDataSource

    @Inject
    lateinit var userDao: UserDao

    private lateinit var context: Context

    var producer: NotificationInvoker = NotificationInvoker()

    override fun onReceive(context: Context, intent: Intent?) {
        this.context = context
        val triggeredTime = getLocalDateTime(intent ?: Intent())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                log("triggerTime $triggeredTime START notification progress ")
                val user = userDao.currentUser()
                log("triggerTime $triggeredTime user Loaded ")

                val foundReminders = findReminders(triggeredTime, user)
                log("triggerTime $triggeredTime found Reminders ${foundReminders.size} ")

                log("triggerTime $triggeredTime starting notifications")
                withContext(Dispatchers.Main) {

                    producer.showReminderNotification(
                        context,
                        foundReminders.map { ReminderType.valueOf(it.type) }.distinct()
                    )
                }
                log("triggerTime $triggeredTime notifications fired")


                ReminderSchedulerJob.startSchedule()
                log("triggerTime $triggeredTime FINISHED with success")
            } catch (ex: Exception) {
                log("triggerTime $triggeredTime FINISHED with exception $ex")
            }

        }
    }

    private fun findReminders(
        triggeredTime: LocalDateTime?,
        user: ApplicationUser
    ): List<ReminderInstance> {
        if (triggeredTime != null) {
            return notificationRepo.findAllEnabledReminderInstancesByOwnerWithSpecificTime(
                user.id,
                triggeredTime
            )
        }
        return emptyList()
    }

    companion object {
        const val TRIGGERED_DATE_TIME = "TRIGGERED_DATE_TIME"

        fun getLocalDateTime(intent: Intent): LocalDateTime? {
            val time = intent.extras?.getLong(TRIGGERED_DATE_TIME, 0L) ?: return null
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC)
        }

        fun createTrigger(context: Context, milis: Long): Intent {
            val args = bundleOf(TRIGGERED_DATE_TIME to milis)
            return Intent(context, ReminderReceiver::class.java)
                .apply {
                    putExtras(args)
                }
        }
    }


}

fun ReminderReceiver.log(log: String) {
    this.logDao.logContent(log)
}

