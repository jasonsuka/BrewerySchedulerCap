package com.jsuka.breweryscheduler.data.repository

import com.jsuka.breweryscheduler.data.local.dao.QualificationDao
import com.jsuka.breweryscheduler.data.local.entity.CredentialEntity
import java.time.LocalDate

class QualificationRepository(
    private val dao: QualificationDao
) : IRepository<CredentialEntity> {

    override suspend fun add(item: CredentialEntity): Long = dao.insert(item)

    override suspend fun update(item: CredentialEntity): Boolean = dao.update(item) > 0

    override suspend fun delete(item: CredentialEntity): Boolean = dao.delete(item) > 0

    override suspend fun getById(id: Long): CredentialEntity? = dao.findById(id)

    override suspend fun getAll(): List<CredentialEntity> = dao.findAll()

    suspend fun forPerson(personId: Long): List<CredentialEntity> = dao.findForPerson(personId)

    /**
     * Builds the credential lookup the Module 3 design calls for: a map
     * keyed by credential code so a qualification check during validation
     * is a constant-time lookup rather than a repeated query.
     */
    suspend fun credentialMapFor(personId: Long): Map<String, CredentialEntity> =
        dao.findForPerson(personId).associateBy { it.code }

    suspend fun expiringThrough(date: LocalDate): List<CredentialEntity> =
        dao.findExpiringThrough(date)
}
