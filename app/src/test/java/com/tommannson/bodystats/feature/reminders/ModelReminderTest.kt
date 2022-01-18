package com.tommannson.bodystats.feature.reminders

import com.tommannson.bodystats.infrastructure.ReminderDefinition
import com.tommannson.bodystats.infrastructure.ReminderInstance
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.WeekFields
import java.util.*

class ModelReminderTest {

    @Test
    fun createListOfReminders() {

        val instanceUnderTesting = ModelReminder(
            mockk(),
            mockk()
        )

        val date = LocalDate.of(2000, 3, 10)
            .with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek())
        val timeOfRedminder = LocalTime.of(10, 23, 0)
        val type = "TYPE"
        val ownerId = 50L
        val reminderId = 100L
        val fullNow = LocalDateTime.of(2000, 4, 10, 12, 40, 0)

        val reminderDefinitions = ReminderDefinition(
            date,
            timeOfRedminder,
            "",
            2,
            type,
            ownerId,
            true,
            reminderId,
            )

        val instances = instanceUnderTesting.createListOfReminders(
            date,
            listOf(1), // only in mondays
            reminderDefinitions,
            fullNow,
            10
        )

        assertEquals("sizez not matches", 10, instances.size)
        assertFirstReminderIsWithSpecficDateTime(timeOfRedminder, instances)
        assertLastReminderIsWithSpecficDateTime(timeOfRedminder, instances)


        instances.toString()

    }

    private fun assertFirstReminderIsWithSpecficDateTime(
        timeOfRedminder: LocalTime?,
        instances: List<ReminderInstance>
    ) {
        val expectedDateTime = LocalDateTime.of(LocalDate.of(2000, 4, 17), timeOfRedminder)

        assertEquals(
            "first reminder is",
            expectedDateTime,
            instances.first().reminderDayTime
        )
    }

    private fun assertLastReminderIsWithSpecficDateTime(
        timeOfRedminder: LocalTime?,
        instances: List<ReminderInstance>
    ) {
        val expectedDateTime = LocalDateTime.of(LocalDate.of(2000, 4, 17), timeOfRedminder)

        assertEquals(
            "first reminder is",
            expectedDateTime.plusWeeks(9*2),
            instances.last().reminderDayTime
        )
    }
}