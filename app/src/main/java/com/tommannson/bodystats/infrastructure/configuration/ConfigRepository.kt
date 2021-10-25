package com.tommannson.bodystats.infrastructure.configuration

import androidx.room.*

@Dao
interface ConfigDao {

    @Query("SELECT * FROM savedstatsconfiguration")
    fun getAll(): List<SavedStatsConfiguration>

    @Insert
    fun insertAll(vararg users: SavedStatsConfiguration)

    @Delete
    fun delete(user: SavedStatsConfiguration)

}

@Dao
interface UserDao {

    @Query("""SELECT *
FROM applicationuser
"""
    )
    fun getAll(): List<ApplicationUser>

    @Insert
    fun insertAll(vararg users: ApplicationUser)

    @Delete
    fun delete(user: ApplicationUser)

}

@Dao
interface NextDao {

    @Query("""SELECT user.id, user.name, user.height, user.weight, user.sex, 
config.talia, config.brzuch, config.biodra 
FROM applicationuser as user
JOIN savedstatsconfiguration as config  ON user.id=config.userOwnerId; """)
    fun getAll(): List<Next>

}

data class ViewOfStats (
    @ColumnInfo(name = "talia") val talia: Float,
    @ColumnInfo(name = "brzuch") val brzuch: Float,
    @ColumnInfo(name = "biodra") val biodra: Float,
)

data class Next(

    @Embedded
    val user: ApplicationUser,
//    var name: String,
//    var height: Float,
//    var weight: Float,
//    var sex: Boolean,
//    var id: Int = 0,
    @Embedded
    val stats: ViewOfStats,

)

data class OwnerWithStats (
    @Embedded
    val owner: ApplicationUser,
    @Relation(
        parentColumn = "id",
        entityColumn = "userOwnerId"
    )
    val stats: List<SavedStatsConfiguration>


)

