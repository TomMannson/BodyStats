package com.tommannson.bodystats.infrastructure.configuration

import androidx.room.*

@Entity
data class SavedStatsConfiguration(

    @ColumnInfo(name = "waga") val waga: Float,
    @ColumnInfo(name = "biust") val biust: Float,
    @ColumnInfo(name = "talia") val talia: Float,
    @ColumnInfo(name = "brzuch") val brzuch: Float,
    @ColumnInfo(name = "biodra") val biodra: Float,
    @ColumnInfo(name = "udo") val udo: Float,
    @ColumnInfo(name = "lydka") val lydka: Float,
    @ColumnInfo(name = "ramie") val ramie: Float,
    val userOwnerId: Long,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
)


@Entity
data class ApplicationUser(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "height") val height: Float,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "sex") val sex: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
)

//data class UserAndLibrary(
//    @Embedded val user: ApplicationUser,
//    @Relation(
//        parentColumn = "userId",
//        entityColumn = "userOwnerId"
//    )
//    val configuration: List<SavedStatsConfiguration>
//)


@Database(entities = arrayOf(SavedStatsConfiguration::class, ApplicationUser::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun userDao(): UserDao
    abstract fun nextDao(): NextDao
}

