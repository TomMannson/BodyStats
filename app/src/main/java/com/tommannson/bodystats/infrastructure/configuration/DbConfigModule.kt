package com.tommannson.bodystats.infrastructure.configuration

import android.app.Application
import androidx.room.Room
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

}


