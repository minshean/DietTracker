package com.diettracker.services.profile

import com.diettracker.models.ProfileModel
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await

class FirebaseProfileRepository(
    private val databaseReference: DatabaseReference
): ProfileRepository {

    override fun updateProfile(uid: String, profile: ProfileModel) {
            databaseReference.child("profiles/$uid")
                .setValue(profile)
    }

    override suspend fun getProfile(uid: String): ProfileModel? {
        val snap = databaseReference.child("profiles/$uid").get().await()
        return if(snap.exists()){
            snap.getValue(ProfileModel::class.java)
        } else {
            null
        }
    }
}