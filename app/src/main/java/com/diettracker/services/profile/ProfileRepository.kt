package com.diettracker.services.profile

import com.diettracker.models.ProfileModel

interface ProfileRepository {
    fun updateProfile(uid: String, profile: ProfileModel)
    suspend fun getProfile(uid: String): ProfileModel?
}