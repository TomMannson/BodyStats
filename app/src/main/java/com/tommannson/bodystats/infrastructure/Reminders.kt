package com.tommannson.bodystats.infrastructure


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime


@Entity(
    tableName = "reminder_definition",
)
data class ReminderDefinition(
    @ColumnInfo(name = "created_at") val createdAt: LocalDate,
    @ColumnInfo(name = "time_of_reminder") var timeOfReminder: LocalTime,
    @ColumnInfo(name = "days_of_reminder") var days: String,
    @ColumnInfo(name = "frequency") var timeBetweenReminders: Int, //in weeks
    @ColumnInfo(name = "reminder_type") val type: String,
    @ColumnInfo(name = "owner_id") val owner: Long,
    @ColumnInfo(name = "enabled") var enabled: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)

data class ReminderDefinitionListItem(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "reminder_type") val type: String,
    @ColumnInfo(name = "enabled") var enabled: Boolean,
)

@Entity(
    tableName = "reminder_instance",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ReminderDefinition::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("reminder_id"),
        )
    )
)
data class ReminderInstance(
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    @ColumnInfo(name = "reminder_day") val reminderDayTime: LocalDateTime,
    @ColumnInfo(name = "reminder_type") val type: String,
    @ColumnInfo(name = "reminder_id") val reminder: Long,

    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)

data class ReminderInstanceAgregation (
    var id: Long = 0,

    )
