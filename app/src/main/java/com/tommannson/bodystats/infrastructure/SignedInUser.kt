package com.tommannson.bodystats.infrastructure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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