package com.tommannson.bodystats.infrastructure.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.tommannson.bodystats.feature.BodyStatsApplication
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import com.tommannson.bodystats.infrastructure.logs.LogDataSource
import com.tommannson.bodystats.infrastructure.notifications.AlertPointerUpdater.Companion.ALERT_CRON_ID
import com.tommannson.bodystats.infrastructure.notifications.notificationorganizer.NotificationInvoker
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Created by tomasz.krol on 2017-05-18.
 */
class AlertPointerUpdater {

    companion object {
        const val ALARM_REMINDER_ID = 1
        const val ALERT_CRON_ID = 3
        const val ALERT_STATS_CHANNEL_ID = "ALERT_STATS_CHANNEL_ID"
        const val MEASUREMENTS_CHANNEL_ID = "MEASUREMENTS_CHANNEL_ID"
        const val TRIGGERED_DATE_TIME = "TRIGGERED_DATE_TIME"

    }
}

@HiltWorker
class ReminderSchedulerJob
@AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    var userDao: UserDao,
    var logDao: LogDataSource,
    var planer: SchedulePlaner
) : CoroutineWorker(appContext, workerParams) {

    var producer: NotificationInvoker = NotificationInvoker()

    override suspend fun doWork(): Result {

        try {
            val user = userDao.currentUser()
            logDao.logContent("ReminderSchedulerJob: Job STARTED for ${user.name}")
            planer.rescheduleNextReminder(user)
            logDao.logContent("ReminderSchedulerJob: job FINISHED")
        }catch (ex: Exception){
            logDao.logContent("ReminderSchedulerJob: job FINISHED with exception $ex")
        }

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(ALERT_CRON_ID, producer.buildForegroundNotification(applicationContext))
    }



    companion object {
        fun startSchedule() {
            val request: WorkRequest = OneTimeWorkRequestBuilder<ReminderSchedulerJob>()
                .build()

            WorkManager.getInstance(BodyStatsApplication.app).enqueue(request)
        }
    }
}
