package com.dineverse.ai.domain.repository

import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<Unit>
    suspend fun signup(email: String, password: String, fullName: String): Resource<Unit>
    suspend fun logout(): Resource<Unit>
    suspend fun forgotPassword(email: String): Resource<Unit>
    suspend fun verifyEmail(): Resource<Boolean>
    suspend fun getCurrentUser(): User?
    suspend fun isLoggedIn(): Boolean
    suspend fun resendVerificationEmail(email: String): Resource<Unit>
    fun observeAuthState(): Flow<Boolean>
}
