package com.jsuka.breweryscheduler.domain.service

import com.jsuka.breweryscheduler.data.local.dao.EmployeeDao
import com.jsuka.breweryscheduler.data.local.dao.UserAccountDao
import com.jsuka.breweryscheduler.data.local.entity.PersonEntity
import com.jsuka.breweryscheduler.domain.model.PersonRole
import com.jsuka.breweryscheduler.security.PasswordHasher

/** Result of one authentication attempt. */
sealed interface AuthOutcome {
    data class Success(val person: PersonEntity, val role: PersonRole) : AuthOutcome

    /**
     * Deliberately does not say whether the username or the password was
     * wrong. Naming which one would tell an attacker that an account
     * exists, so the Module 6 test plan expects a generic failure.
     */
    data object InvalidCredentials : AuthOutcome
}

class AuthService(
    private val accounts: UserAccountDao,
    private val people: EmployeeDao
) {

    suspend fun authenticate(username: String, password: String): AuthOutcome {
        val account = accounts.findByUsername(username.trim())
            ?: return AuthOutcome.InvalidCredentials

        if (!PasswordHasher.verify(password, account.passwordSalt, account.passwordHash)) {
            return AuthOutcome.InvalidCredentials
        }

        val person = people.findById(account.personId) ?: return AuthOutcome.InvalidCredentials
        return AuthOutcome.Success(person, account.role)
    }
}
