package com.jsuka.breweryscheduler.data.repository

import com.jsuka.breweryscheduler.data.local.dao.DepartmentDao
import com.jsuka.breweryscheduler.data.local.entity.DepartmentEntity
import com.jsuka.breweryscheduler.data.local.entity.JobPositionEntity
import kotlinx.coroutines.flow.Flow

class DepartmentRepository(private val dao: DepartmentDao) : IRepository<DepartmentEntity> {

    override suspend fun add(item: DepartmentEntity): Long = dao.insert(item)

    override suspend fun update(item: DepartmentEntity): Boolean = dao.update(item) > 0

    override suspend fun delete(item: DepartmentEntity): Boolean = dao.delete(item) > 0

    override suspend fun getById(id: Long): DepartmentEntity? = dao.findById(id)

    override suspend fun getAll(): List<DepartmentEntity> = dao.findAll()

    fun observeAll(): Flow<List<DepartmentEntity>> = dao.observeAll()

    fun observeAllPositions(): Flow<List<JobPositionEntity>> = dao.observeAllPositions()

    suspend fun positionsFor(departmentId: Long): List<JobPositionEntity> =
        dao.findPositionsFor(departmentId)

    suspend fun positionById(id: Long): JobPositionEntity? = dao.findPositionById(id)
}
