package com.tommannson.bodystats.infrastructure.configuration

import android.app.Application
import androidx.room.Room
import com.tommannson.bodystats.infrastructure.logs.LogDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DbConfigModule {

    @Singleton
    @Provides
    fun createDataBase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "database-name")
            .build()
    }

    @Singleton
    @Provides
    fun createUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun createStatsDao(db: AppDatabase): StatsDao {
        return db.statsDao()
    }

    @Singleton
    @Provides
    fun createReminderDao(db: AppDatabase): ReminderDao {
        return db.reminderDao()
    }

    @Singleton
    @Provides
    fun createLogDao(db: AppDatabase): LogDataSource {
        return db.logDao()
    }

}


