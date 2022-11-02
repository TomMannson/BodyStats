package com.tommannson.bodystats.infrastructure.configuration

import androidx.annotation.Keep
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tommannson.bodystats.infrastructure.*
import com.tommannson.bodystats.infrastructure.logs.Log
import com.tommannson.bodystats.infrastructure.logs.LogDataSource

@Database(
    entities = arrayOf(
        ApplicationUser::class,
        SavedStats::class,
        ReminderDefinition::class,
        ReminderInstance::class,
        Log::class
    ),
    autoMigrations = arrayOf(
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ),
    version = 3,
)
@TypeConverters(
    DateTimeConverter::class,
)
@Keep
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun statsDao(): StatsDao
    abstract fun reminderDao(): ReminderDao
    abstract fun logDao(): LogDataSource
}



