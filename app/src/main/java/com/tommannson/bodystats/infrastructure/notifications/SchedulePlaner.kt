package com.tommannson.bodystats.infrastructure.notifications

import android.app.Application
import com.tommannson.bodystats.infrastructure.ApplicationUser
import com.tommannson.bodystats.infrastructure.configuration.ReminderDao
import com.tommannson.bodystats.infrastructure.logs.LogDataSource
import com.tommannson.bodystats.infrastructure.notifications.alarms.AlarmCreator
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class SchedulePlaner
@Inject constructor() {

    @Inject
    lateinit var application: Application

    @Inject
    lateinit var logDao: LogDataSource

    @Inject
    lateinit var notificationRepo: ReminderDao

    private val alarmCreator = AlarmCreator()

    fun rescheduleNextReminder(user: ApplicationUser) {
        val timeOfSchedule = LocalDateTime.now().toString()
        logDao.logContent("SchedulePlaner $timeOfSchedule reschedule")
        val result = notificationRepo.findEnabledReminderInstancesByOwnerWhichWillTriggerAfter(
            user.id,
            LocalDateTime.now().plusSeconds(10)
        )

        if (result != null) {
            logDao.logContent("SchedulePlaner $timeOfSchedule found next alert instance with id ${result.id}")
            alarmCreator.setAlarm(
                application, result.reminderDayTime,
                AlertPointerUpdater.ALARM_REMINDER_ID
            )
            logDao.logContent("SchedulePlaner $timeOfSchedule alarm set at ${result.reminderDayTime}")
        } else {
            logDao.logContent("SchedulePlaner $timeOfSchedule alarm not found")
            alarmCreator.cancelAlarm(application, AlertPointerUpdater.ALARM_REMINDER_ID)
        }


    }
}