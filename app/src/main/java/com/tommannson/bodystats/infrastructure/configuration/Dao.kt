package com.tommannson.bodystats.infrastructure.configuration

import androidx.room.*
import com.tommannson.bodystats.infrastructure.ApplicationUser
//import com.tommannson.bodystats.infrastructure.ReminderType
import com.tommannson.bodystats.infrastructure.SavedStats
//import com.tommannson.bodystats.infrastructure.UserReminder
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

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
    fun insertAll(vararg users: ApplicationUser): List<Long>

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
WHERE owner_id=:owner AND stat_name IN(:statsNames) AND submitted_at >= :date
ORDER BY submitted_at ASC
    """
    )
    fun getParamLive(
        owner: Long,
        statsNames: List<String>,
        date: LocalDate = LocalDate.now().minusYears(10)
    ): Flow<List<SavedStats>>

    @Insert
    fun createNewStats(itemsToCreate: List<SavedStats>)

    @Update
    fun udateStats(itemsToCreate: List<SavedStats>)
}
//
//@Dao
//interface ReminderDao {
//    @Query(
//        """SELECT *
//FROM user_reminder
//WHERE owner_id=:owner AND reminder_type=:type
//"""
//    )
//    suspend fun getParamsForDate(owner: Long, type: ReminderType): UserReminder
//}
//
