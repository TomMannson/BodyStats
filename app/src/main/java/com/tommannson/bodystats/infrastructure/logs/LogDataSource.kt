package com.tommannson.bodystats.infrastructure.logs

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface LogDataSource {

    @Insert
    fun createLog(log: Log)

    @Insert
    fun logContent(logContent: String) {
        createLog(Log(logContent))
    }
}