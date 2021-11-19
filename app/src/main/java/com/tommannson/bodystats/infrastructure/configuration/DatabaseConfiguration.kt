package com.tommannson.bodystats.infrastructure.configuration

import androidx.room.*
import com.tommannson.bodystats.infrastructure.configuration.Statistic.ARM_STATISTIC
import com.tommannson.bodystats.infrastructure.configuration.Statistic.BELLY_STATISTIC
import com.tommannson.bodystats.infrastructure.configuration.Statistic.BUST_STATISTIC
import com.tommannson.bodystats.infrastructure.configuration.Statistic.CALF_STATISTIC
import com.tommannson.bodystats.infrastructure.configuration.Statistic.HIPS_STATISTIC
import com.tommannson.bodystats.infrastructure.configuration.Statistic.THIGH_STATISTIC
import com.tommannson.bodystats.infrastructure.configuration.Statistic.WAIST_STATISTIC
import com.tommannson.bodystats.infrastructure.configuration.Statistic.WEIGHT
import org.jetbrains.annotations.NotNull
import org.threeten.bp.LocalDate

@Entity
data class ApplicationUser(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "height") val height: Float,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "dream_weight") val dreamWeight: Float,
    @ColumnInfo(name = "sex") val sex: Int,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)

object Gender {
    const val FEMALE = 1
    const val MALE = 2
}

object ParamType {
    const val BASIC = 1
    const val ADVANCED = 2
}

object Statistic {
    const val WEIGHT = "weight"
    const val WEIGHT_COMPOSITION = "weight_composition"
    //basics
    const val BUST_STATISTIC = "bust"
    const val ARM_STATISTIC = "arm"
    const val WAIST_STATISTIC = "waist"
    const val BELLY_STATISTIC = "belly"
    const val HIPS_STATISTIC = "hips"
    const val THIGH_STATISTIC = "thigh"
    const val CALF_STATISTIC = "calf"
    //body composition
    const val FAT_PERCENT= "FAT_PERCENT"
    const val FAT_MASS = "FAT_MASS"
    const val FFM = "ftm"
    const val MUSCLE_MASS = "muscle_mass"
    const val TBW = "tbw"
    const val TBW_PERCENT = "tbw_percent"
    const val BONE_MASS = "bone_mass"
    const val BMR = "BMR"
    const val METABOLIC_AGE = "metabolic_age"
    const val VISCELAR_FAT_RATING = "viscelar_fat_rating"
    const val BMI = "bmi"
    const val IDEAL_BODY_WEIGHT = "ideal_body_weight"
}

val BASIC_PARAMS = listOf(
    WEIGHT,
    BUST_STATISTIC,
    ARM_STATISTIC,
    WAIST_STATISTIC,
    BELLY_STATISTIC,
    HIPS_STATISTIC,
    THIGH_STATISTIC, //udo
    CALF_STATISTIC
)

val BASIC_PARAMS_FOR_CREATE = listOf(
    BUST_STATISTIC,
    ARM_STATISTIC,
    WAIST_STATISTIC,
    BELLY_STATISTIC,
    HIPS_STATISTIC,
    THIGH_STATISTIC, //udo
    CALF_STATISTIC
)

val BODY_COMPOSITION_PARAMS = listOf(
    Statistic.WEIGHT_COMPOSITION,
    Statistic.FAT_MASS,
    Statistic.FAT_PERCENT,
    Statistic.FFM,
    Statistic.MUSCLE_MASS,
    Statistic.TBW,
    Statistic.TBW_PERCENT,
    Statistic.BONE_MASS, //udo
    Statistic.BMR,
    Statistic.METABOLIC_AGE,
    Statistic.VISCELAR_FAT_RATING,
    Statistic.BMI,
)

val FULL_LIST_OF_STATS = mutableListOf<String>()
    .also {
        it.addAll(BASIC_PARAMS)
        it.addAll(BODY_COMPOSITION_PARAMS)
    }


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
    @ColumnInfo(name = "param_type") val type: Int = ParamType.BASIC,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)


object DateConverter {

    @TypeConverter
    fun convertDateToLong(date: LocalDate) = date.toEpochDay()

    @TypeConverter
    fun convertLongToDay(longDay: Long) = LocalDate.ofEpochDay(longDay)

}

@Database(
    entities = arrayOf(
        ApplicationUser::class,
        SavedStats::class
    ),
    version = 1,
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun statsDao(): StatsDao
}



