package com.tommannson.bodystats.infrastructure.configuration

//import com.tommannson.bodystats.infrastructure.ReminderType
//import com.tommannson.bodystats.infrastructure.UserReminder
import androidx.room.*
import com.tommannson.bodystats.infrastructure.ApplicationUser
import com.tommannson.bodystats.infrastructure.ReminderDefinition
import com.tommannson.bodystats.infrastructure.ReminderInstance
import com.tommannson.bodystats.infrastructure.SavedStats
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

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

@Dao
interface ReminderDao {
    @Query(
        """SELECT *
                    FROM reminder_definition
                    WHERE owner_id=:owner """
    )
    fun getRemindersForOwner(owner: Long): Flow<List<ReminderDefinition>>

    @Query(
        """SELECT *
                    FROM reminder_definition
                    WHERE owner_id=:owner AND reminder_type=:type """
    )
    suspend fun getReminderByOwnerAndType(
        owner: Long,
        type: String
    ): ReminderDefinition?

    @Query(
        """SELECT r_i.id, r_i.reminder_day, r_i.reminder_id, r_i.reminder_type, r_i.created_at
                    FROM reminder_instance as r_i
                    JOIN reminder_definition as r_o ON r_i.reminder_id=r_o.id
                    WHERE r_o.owner_id=:owner AND r_o.enabled=1  AND r_i.reminder_day>:greaterThen
                    ORDER BY r_i.reminder_day
                    LIMIT 1
                    """
    )
    fun findEnabledReminderInstancesByOwnerWhichWillTriggerAfter(
        owner: Long,
        greaterThen: LocalDateTime
    ): List<ReminderInstance>

    @Query(
        """SELECT r_i.id, r_i.reminder_day, r_i.reminder_id, r_i.reminder_type, r_i.created_at
                    FROM reminder_instance as r_i
                    JOIN reminder_definition as r_o ON r_i.reminder_id=r_o.id
                    WHERE r_o.owner_id=:owner AND r_o.enabled=1  AND r_i.reminder_day=:greaterThen
                    ORDER BY r_i.reminder_day
                    """
    )
    fun findAllEnabledReminderInstancesByOwnerWithSpecificTime(
        owner: Long,
        greaterThen: LocalDateTime
    ): List<ReminderInstance>


    @Insert
    fun create(reminderDefinition: ReminderDefinition): Long

    @Update
    fun update(reminderDefinition: ReminderDefinition)

    @Insert
    fun createAll(listOfReminders: List<ReminderInstance>)

    @Query(
        """
        DELETE FROM reminder_instance
        WHERE reminder_id = :reminderId
        """
    )
    fun deleteByReminderId(reminderId: Long)


}

