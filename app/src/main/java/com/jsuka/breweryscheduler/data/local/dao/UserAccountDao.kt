package com.jsuka.breweryscheduler.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jsuka.breweryscheduler.data.local.entity.UserAccountEntity

@Dao
interface UserAccountDao {

    @Insert
    suspend fun insert(account: UserAccountEntity): Long

    @Query("SELECT * FROM user_accounts WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): UserAccountEntity?

    @Query("SELECT COUNT(*) FROM user_accounts")
    suspend fun count(): Int
}
