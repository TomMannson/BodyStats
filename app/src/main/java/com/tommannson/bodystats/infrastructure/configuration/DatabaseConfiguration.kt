package com.tommannson.bodystats.infrastructure.configuration

//import ReminderDefinition
//import ReminderInstance
//import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tommannson.bodystats.infrastructure.*

@Database(
    entities = arrayOf(
        ApplicationUser::class,
        SavedStats::class,
//        ReminderDefinition::class,
//        ReminderInstance::class,
    ),
    autoMigrations = arrayOf(
//        AutoMigration(from = 1, to = 2)
    ),
    version = 1,
)
@TypeConverters(
    DateTimeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun statsDao(): StatsDao
//    abstract fun reminderDao(): ReminderDao
}



