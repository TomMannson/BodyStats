package com.tommannson.bodystats.feature.reminders

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.architecture.flow.NoParamsFlowTracker
import com.tommannson.bodystats.feature.BodyStatsApplication
import com.tommannson.bodystats.infrastructure.ReminderDefinition
import com.tommannson.bodystats.infrastructure.ReminderInstance
import com.tommannson.bodystats.infrastructure.configuration.ReminderDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import com.tommannson.bodystats.infrastructure.notifications.AlertPointerUpdater
import com.tommannson.bodystats.model.reminding.ReminderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.WeekFields
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemindersListViewModel
@Inject constructor(
    remindersTracker: RemindersTracker,
    private val modelReminder: ModelReminder,
) : ViewModel() {

    val state: StateFlow<State> = remindersTracker.flow
        .map {
            State(
                list = it,
                groupedDefinitions = it.groupBy { it.type }
            )
        }
        .stateIn(
            scope = viewModelScope + Dispatchers.IO,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = State()
        )

    val actions = MutableSharedFlow<Actions>()

    init {
        remindersTracker.track()
    }

    fun toggle(type: ReminderType, it: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            modelReminder.toggle(type)
        }
    }

    fun setReminder(type: ReminderType, frequency: Int, days: List<Int>, time: LocalTime) {
        viewModelScope.launch(Dispatchers.IO) {
            modelReminder.setReminder(type, time, days, frequency)
            actions.emit(Actions.Success)
        }
    }
}

data class State(
    val list: List<ReminderDefinition> = listOf(),
    val groupedDefinitions: Map<String, List<ReminderDefinition>> = mapOf()
)

sealed class Actions {
    object Success : Actions()
}

class RemindersTracker
@Inject constructor(
    private val userDao: UserDao,
    private val reminderDao: ReminderDao
) : NoParamsFlowTracker<List<ReminderDefinition>>(Dispatchers.IO) {
    override fun createObservable(): Flow<List<ReminderDefinition>> {
        val user = userDao.getAll().first()
        return reminderDao.getRemindersForOwner(user.id)
    }
}

class ModelReminder
@Inject constructor(
    private val userDao: UserDao,
    private val reminderDao: ReminderDao
) {
    suspend fun setReminder(type: ReminderType, time: LocalTime, days: List<Int>, frequency: Int) {
        val user = userDao.getAll().first()
        var definition = reminderDao.getReminderByOwnerAndType(user.id, type.name)

        if (definition != null) {
            definition.timeOfReminder = time
            definition.days = days.joinToString(separator = "_")
            definition.timeBetweenReminders = frequency
            definition.enabled = true
            reminderDao.update(definition)
        } else {
            val now = LocalDate.now()
            definition = ReminderDefinition(
                now,
                time,
                days.joinToString(separator = "_"),
                frequency,
                type.name,
                user.id,
                true,
            )
            val id = reminderDao.create(definition)
            definition.id = id
        }
        refreshReminders(definition)
        ContextCompat.startForegroundService(
            BodyStatsApplication.app,
            AlertPointerUpdater.starter(BodyStatsApplication.app)
        )
    }

    suspend fun toggle(reminderType: ReminderType) {
        val user = userDao.getAll().first()
        var definition = reminderDao.getReminderByOwnerAndType(user.id, reminderType.name)

        if (definition != null) {
            definition.enabled = !definition.enabled
            reminderDao.update(definition)
        } else {
            val now = LocalDate.now()
            definition = ReminderDefinition(
                now,
                LocalTime.now().truncatedTo(ChronoUnit.HOURS),
                now.dayOfWeek.value.toString(),
                1,
                reminderType.name,
                user.id,
                true,
            )
            val id = reminderDao.create(definition)
            definition.id = id
        }
        refreshReminders(definition)
        ContextCompat.startForegroundService(
            BodyStatsApplication.app,
            AlertPointerUpdater.starter(BodyStatsApplication.app)
        )
    }

    fun refreshReminders(definition: ReminderDefinition) {
        if (!definition.enabled) {
            return
        }

        reminderDao.deleteByReminderId(definition.id)
        val fullNow = LocalDateTime.now()
        val now = LocalDate.now()
        var dayToAdd =
            now.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek())
        var numberOfReminderToCreate = NUMBER_OF_REMINDERS_TO_CREATE
        val listOfDays = definition.days.split("_").map { it.toLong() }
        val listOfReminders = mutableListOf<ReminderInstance>()

        reminderCreator@ while (true) {
            val startOfWeek = dayToAdd
            for (dayOfWeek in listOfDays) {
                dayToAdd = startOfWeek.plusDays(dayOfWeek-1)
                val localDateTimeToAdd = LocalDateTime.of(dayToAdd, definition.timeOfReminder)
                if (localDateTimeToAdd.isBefore(fullNow)) {
                    continue
                }

                listOfReminders.add(
                    ReminderInstance(
                        fullNow,
                        localDateTimeToAdd,
                        definition.type,
                        definition.id
                    )
                )
                numberOfReminderToCreate--;
                if (numberOfReminderToCreate == 0) {
                    break@reminderCreator
                }
            }

            dayToAdd = startOfWeek.plusWeeks(definition.timeBetweenReminders.toLong())
        }
        try {
            if (listOfReminders.isNotEmpty()) {
                reminderDao.createAll(listOfReminders)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    companion object {
        const val NUMBER_OF_REMINDERS_TO_CREATE = 1000
    }
}
