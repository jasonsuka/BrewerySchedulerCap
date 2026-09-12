package com.jsuka.breweryscheduler.security

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Salted PBKDF2 password hashing. Module 2 committed to hashing passwords
 * rather than storing them, so no plaintext password is ever written to
 * the database.
 *
 * java.util.Base64 is used rather than android.util.Base64 so this class
 * runs in plain JVM unit tests without needing an emulator. It is available
 * from API 26, which is the project's minSdk.
 */
object PasswordHasher {

    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH = 256
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"

    fun newSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun hash(password: String, salt: String): String {
        val saltBytes = Base64.getDecoder().decode(salt)
        val spec = PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val hashed = factory.generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(hashed)
    }

    /** Constant-time comparison so a wrong password cannot be timed. */
    fun verify(password: String, salt: String, expectedHash: String): Boolean {
        val actual = hash(password, salt)
        if (actual.length != expectedHash.length) return false
        var diff = 0
        for (i in actual.indices) {
            diff = diff or (actual[i].code xor expectedHash[i].code)
        }
        return diff == 0
    }
}
