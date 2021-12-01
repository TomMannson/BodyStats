package com.tommannson.bodystats.infrastructure

import androidx.room.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ApplicationUser::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("owner_id")
        )
    )
)
data class SavedStats(
    @ColumnInfo(name = "stat_name") val statName: String,
    @ColumnInfo(name = "value") val value: Float,
    @ColumnInfo(name = "submitted_at") val submitedAt: LocalDate,
    @ColumnInfo(name = "owner_id") val owner: Long,
    @ColumnInfo(name = "param_type") val type: Int = 1, //TODO not used can be removed
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)

object DateTimeConverter {

    @TypeConverter
    fun convertDateToLong(date: LocalDate) = date.toEpochDay()

    @TypeConverter
    fun convertLongToDay(longDay: Long) = LocalDate.ofEpochDay(longDay)

    @TypeConverter
    fun convertTimeToInt(time: LocalTime) = time.toSecondOfDay()

    @TypeConverter
    fun convertIntToTime(seconds: Int) = LocalTime.ofSecondOfDay(seconds.toLong())

    @TypeConverter
    fun convertDateTimeToInt(time: LocalDateTime) = time.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun convertIntToDateTime(seconds: Long) =
        LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC)

}