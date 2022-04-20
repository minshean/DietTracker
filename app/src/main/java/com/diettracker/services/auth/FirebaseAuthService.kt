package com.diettracker.services.auth

import com.diettracker.models.ProfileModel
import com.diettracker.services.profile.ProfileRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth,
    private val profileRepository: ProfileRepository
): AuthService {

    override suspend fun logIn(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException){
            AuthResult.Failure(e.message ?: "Login Failed")
        }
    }

    override suspend fun register(profile: ProfileModel, email: String, password: String): AuthResult {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(profile.name)
                    .build()
            )
            authResult.user?.uid?.let { uid ->
                profileRepository.updateProfile(uid, profile)
            }
            return AuthResult.Success
        } catch (e: FirebaseAuthException){
            return AuthResult.Failure(e.message ?: "Sign Up Failed")
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserUid(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AuthResult {
        return try {
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            firebaseAuth.currentUser?.reauthenticate(credential)?.await()
            firebaseAuth.currentUser?.updatePassword(newPassword)?.await()
            AuthResult.Success
        } catch (e: FirebaseAuthException){
            AuthResult.Failure(e.message ?: "Failed to Change Password")
        }
    }

    override fun getCurrentUserName(): String {
        return firebaseAuth.currentUser?.displayName ?: ""
    }
}