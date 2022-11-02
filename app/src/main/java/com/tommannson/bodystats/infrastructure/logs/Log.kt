package com.tommannson.bodystats.infrastructure.logs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity(
    tableName = "logs",
)
class Log(
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "reminder_type") val type: String = "D",
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)
