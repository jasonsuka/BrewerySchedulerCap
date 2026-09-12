package com.jsuka.breweryscheduler.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jsuka.breweryscheduler.data.local.entity.CredentialEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface QualificationDao {

    @Insert
    suspend fun insert(credential: CredentialEntity): Long

    @Update
    suspend fun update(credential: CredentialEntity): Int

    @Delete
    suspend fun delete(credential: CredentialEntity): Int

    @Query("SELECT * FROM credentials WHERE id = :id")
    suspend fun findById(id: Long): CredentialEntity?

    @Query("SELECT * FROM credentials")
    suspend fun findAll(): List<CredentialEntity>

    @Query("SELECT * FROM credentials WHERE personId = :personId")
    suspend fun findForPerson(personId: Long): List<CredentialEntity>

    /**
     * Backs the certification-expiration report. A null expirationDate means
     * the credential is a qualification and never appears here.
     */
    @Query(
        """
        SELECT * FROM credentials
        WHERE isCertification = 1
          AND expirationDate IS NOT NULL
          AND expirationDate <= :throughDate
        ORDER BY expirationDate
        """
    )
    suspend fun findExpiringThrough(throughDate: LocalDate): List<CredentialEntity>

    @Query("SELECT * FROM credentials WHERE code = :code")
    fun observeByCode(code: String): Flow<List<CredentialEntity>>
}
