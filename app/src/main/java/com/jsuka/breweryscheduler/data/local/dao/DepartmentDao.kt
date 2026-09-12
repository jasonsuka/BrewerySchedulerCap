package com.jsuka.breweryscheduler.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jsuka.breweryscheduler.data.local.entity.DepartmentEntity
import com.jsuka.breweryscheduler.data.local.entity.JobPositionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {

    @Insert
    suspend fun insert(department: DepartmentEntity): Long

    @Update
    suspend fun update(department: DepartmentEntity): Int

    @Delete
    suspend fun delete(department: DepartmentEntity): Int

    @Query("SELECT * FROM departments WHERE id = :id")
    suspend fun findById(id: Long): DepartmentEntity?

    @Query("SELECT * FROM departments ORDER BY name")
    suspend fun findAll(): List<DepartmentEntity>

    @Query("SELECT * FROM departments ORDER BY name")
    fun observeAll(): Flow<List<DepartmentEntity>>

    @Query("SELECT COUNT(*) FROM departments")
    suspend fun count(): Int

    @Insert
    suspend fun insertPosition(position: JobPositionEntity): Long

    @Query("SELECT * FROM job_positions WHERE departmentId = :departmentId ORDER BY title")
    suspend fun findPositionsFor(departmentId: Long): List<JobPositionEntity>

    @Query("SELECT * FROM job_positions ORDER BY title")
    fun observeAllPositions(): Flow<List<JobPositionEntity>>

    @Query("SELECT * FROM job_positions WHERE id = :id")
    suspend fun findPositionById(id: Long): JobPositionEntity?
}
