package com.jsuka.breweryscheduler.data.repository

import com.jsuka.breweryscheduler.data.local.dao.EmployeeDao
import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity
import com.jsuka.breweryscheduler.data.local.entity.PersonEntity
import com.jsuka.breweryscheduler.domain.model.PersonRole
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val dao: EmployeeDao) : IRepository<PersonEntity> {

    override suspend fun add(item: PersonEntity): Long = dao.insert(item)

    override suspend fun update(item: PersonEntity): Boolean = dao.update(item) > 0

    override suspend fun delete(item: PersonEntity): Boolean = dao.delete(item) > 0

    override suspend fun getById(id: Long): PersonEntity? = dao.findById(id)

    override suspend fun getAll(): List<PersonEntity> = dao.findAll()

    fun observeEmployees(): Flow<List<PersonEntity>> = dao.observeByRole(PersonRole.EMPLOYEE)

    suspend fun inDepartment(departmentId: Long): List<PersonEntity> =
        dao.findByDepartment(departmentId)

    suspend fun countInDepartment(departmentId: Long): Int =
        dao.countInDepartment(departmentId)

    suspend fun availabilityFor(personId: Long): List<AvailabilityEntity> =
        dao.findAvailabilityFor(personId)

    suspend fun replaceAvailability(personId: Long, windows: List<AvailabilityEntity>) {
        dao.clearAvailabilityFor(personId)
        windows.forEach { dao.insertAvailability(it) }
    }
}
