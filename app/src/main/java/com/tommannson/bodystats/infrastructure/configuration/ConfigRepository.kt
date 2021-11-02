package com.tommannson.bodystats.infrastructure.configuration

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

//@Dao
//interface ConfigDao {
//
//    @Query("SELECT * FROM savedstatsconfiguration")
//    fun getAll(): List<SavedStatsConfiguration>
//
//    @Insert
//    fun insertAll(vararg users: SavedStatsConfiguration)
//
//    @Delete
//    fun delete(user: SavedStatsConfiguration)
//
//}

@Dao
interface UserDao {

    @Query(
        """SELECT *
FROM applicationuser
"""
    )
    fun getAll(): List<ApplicationUser>

    @Update
    fun updateUser(user: ApplicationUser);

    @Insert
    fun insertAll(vararg users: ApplicationUser)

    @Delete
    fun delete(user: ApplicationUser)

}

@Dao
interface StatsDao {

    @Query(
        """SELECT *
FROM savedstats
WHERE owner_id=:owner AND submitted_at=:forDate AND stat_name IN(:statsNames)
ORDER BY submitted_at
"""
    )
    fun getParamsForDate(
        owner: Long,
        forDate: LocalDate,
        statsNames: List<String>
    ): List<SavedStats>

    @Query(
        """SELECT *
FROM savedstats
WHERE owner_id=:owner AND stat_name IN(:statsNames) AND 
    submitted_at=(SELECT max(submitted_at) FROM savedstats WHERE stat_name IN(:statsNames)) 
ORDER BY submitted_at ASC
"""
    )
    fun getLastParams(owner: Long, statsNames: List<String>): List<SavedStats>

    @Query(
        """SELECT *
FROM savedstats
WHERE owner_id=:owner AND stat_name IN(:statsNames)
ORDER BY submitted_at ASC
"""
    )
    fun getParams(owner: Long, statsNames: List<String>): List<SavedStats>

    @Query(
        """SELECT *
FROM savedstats
WHERE owner_id=:owner AND submitted_at=:forDate AND stat_name IN(:statsNames)
ORDER BY submitted_at
    """
    )
    fun getParamInfo(owner: Long, forDate: LocalDate, statsNames: List<String>): SavedStats?

    @Query(
        """SELECT *
FROM savedstats
WHERE owner_id=:owner AND stat_name IN(:statsNames)
ORDER BY submitted_at ASC
    """
    )
    fun getParamLive(
        owner: Long,
        statsNames: List<String>
    ): Flow<List<SavedStats>>

    @Insert
    fun createNewStats(itemsToCreate: List<SavedStats>)

    @Update
    fun udateStats(itemsToCreate: List<SavedStats>)


}

