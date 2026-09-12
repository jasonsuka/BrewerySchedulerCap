package com.jsuka.breweryscheduler.data.repository

/**
 * The generic data-access contract established in the Module 3 design.
 * Every repository implements these five operations while hiding the
 * queries specific to its entity, which is what keeps the data source
 * interchangeable behind a stable interface.
 */
interface IRepository<T> {
    suspend fun add(item: T): Long
    suspend fun update(item: T): Boolean
    suspend fun delete(item: T): Boolean
    suspend fun getById(id: Long): T?
    suspend fun getAll(): List<T>
}
