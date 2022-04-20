package com.diettracker.services.session

import android.content.Context
import androidx.core.content.edit

class SessionManager(
    context: Context,
) {
    private val sharedPreferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)

    fun isOnboardingSeen(): Boolean{
        return sharedPreferences.getBoolean("onboarding_seen", false)
    }

    fun setOnboardingSeen(){
        sharedPreferences.edit {
            putBoolean("onboarding_seen", true)
        }
    }
}