package com.jsuka.breweryscheduler

import com.jsuka.breweryscheduler.security.PasswordHasher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun `correct password verifies`() {
        val salt = PasswordHasher.newSalt()
        val hash = PasswordHasher.hash("Supervisor#1", salt)
        assertTrue(PasswordHasher.verify("Supervisor#1", salt, hash))
    }

    @Test
    fun `wrong password is rejected`() {
        val salt = PasswordHasher.newSalt()
        val hash = PasswordHasher.hash("Supervisor#1", salt)
        assertFalse(PasswordHasher.verify("supervisor#1", salt, hash))
    }

    @Test
    fun `same password with different salts gives different hashes`() {
        val first = PasswordHasher.newSalt()
        val second = PasswordHasher.newSalt()
        assertNotEquals(
            PasswordHasher.hash("Employee#1", first),
            PasswordHasher.hash("Employee#1", second)
        )
    }
}
