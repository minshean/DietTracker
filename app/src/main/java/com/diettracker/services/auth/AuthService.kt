package com.diettracker.services.auth

import com.diettracker.models.ProfileModel

interface AuthService {
    suspend fun logIn(email: String, password: String): AuthResult
    suspend fun register(profile: ProfileModel, email: String, password: String): AuthResult
    fun signOut()
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserUid(): String?
    suspend fun changePassword(email: String, oldPassword: String, newPassword: String) : AuthResult
    fun getCurrentUserName(): String
}

sealed class AuthResult{
    object Success: AuthResult()
    data class Failure(val errorMsg: String): AuthResult()
}