package com.jsuka.breweryscheduler.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity
import com.jsuka.breweryscheduler.data.local.entity.PersonEntity
import com.jsuka.breweryscheduler.domain.model.PersonRole
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert
    suspend fun insert(person: PersonEntity): Long

    @Update
    suspend fun update(person: PersonEntity): Int

    @Delete
    suspend fun delete(person: PersonEntity): Int

    @Query("SELECT * FROM people WHERE id = :id")
    suspend fun findById(id: Long): PersonEntity?

    @Query("SELECT * FROM people ORDER BY lastName, firstName")
    suspend fun findAll(): List<PersonEntity>

    @Query("SELECT * FROM people WHERE role = :role ORDER BY lastName, firstName")
    fun observeByRole(role: PersonRole): Flow<List<PersonEntity>>

    @Query("SELECT * FROM people WHERE homeDepartmentId = :departmentId ORDER BY lastName")
    suspend fun findByDepartment(departmentId: Long): List<PersonEntity>

    @Query("SELECT COUNT(*) FROM people WHERE homeDepartmentId = :departmentId AND role = 'EMPLOYEE'")
    suspend fun countInDepartment(departmentId: Long): Int

    @Insert
    suspend fun insertAvailability(availability: AvailabilityEntity): Long

    @Query("SELECT * FROM availability WHERE personId = :personId")
    suspend fun findAvailabilityFor(personId: Long): List<AvailabilityEntity>

    @Query("DELETE FROM availability WHERE personId = :personId")
    suspend fun clearAvailabilityFor(personId: Long)
}
