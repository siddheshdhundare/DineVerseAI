package com.dineverse.ai.core.validation

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Password Rules:
     * Minimum 8 Characters
     * One Uppercase
     * One Lowercase
     * One Number
     * One Special Character
     */
    fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }

    fun isValidName(name: String): Boolean {
        return name.trim().isNotEmpty() && name.length >= 2
    }

    fun isPasswordMatching(password: String, confirm: String): Boolean {
        return password == confirm
    }
}
