package com.tommannson.bodystats.infrastructure.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.tommannson.bodystats.feature.BodyStatsApplication
import com.tommannson.bodystats.infrastructure.ReminderDefinition
import com.tommannson.bodystats.infrastructure.configuration.ReminderDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import com.tommannson.bodystats.infrastructure.notifications.notificationorganizer.NotificationInvoker
import com.tommannson.bodystats.model.reminding.ReminderType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemindersAvailable
@Inject constructor() {

    fun startSchedule() {
        val request: WorkRequest = OneTimeWorkRequestBuilder<RemindMeFeatures>()
            .setInitialDelay(10, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(BodyStatsApplication.app).enqueue(request)
    }

}


@HiltWorker
class RemindMeFeatures
@AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userDao: UserDao,
    private val reminderDao: ReminderDao,
) :
    CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result {

        val notifier = NotificationInvoker()

        val users = userDao.getAll()

        if (users.isEmpty()) {
            return Result.success()
        }

        val foundUser = users.first()
        val remindersCount = reminderDao.numberOfEnabledRemindersForOwner(foundUser.id)

        if (remindersCount.filter(this::isSpecialReminder)
                .isNotEmpty()
        ) {
            return Result.success()
        }

        withContext(Dispatchers.Main) {
            notifier.showNotificationForRemindersFeature(applicationContext)
        }

        return Result.success()
    }

    private fun isSpecialReminder(it: ReminderDefinition) =
        it.type == ReminderType.Composition.name || it.type == ReminderType.Measurements.name
}