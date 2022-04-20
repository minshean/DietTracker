package com.diettracker.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.models.ProfileModel
import com.diettracker.services.auth.AuthResult
import com.diettracker.services.auth.AuthService
import com.diettracker.services.profile.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val navigationDispatcher: NavigationDispatcher,
    private val snackbarDispatcher: SnackbarDispatcher,
    private val authService: AuthService,
): ViewModel() {

    val loading = MutableLiveData(false)

    fun signUp(profile: ProfileModel, email: String, password: String){

        if(profile.name.isEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Name is empty")
            return
        }

        if(email.isEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Email is empty")
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            snackbarDispatcher.emitErrorSnackbar("Invalid Email")
            return
        }

        if(password.isEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Password is empty")
            return
        }

        if(password.length < 8){
            snackbarDispatcher.emitErrorSnackbar("Password should be 8 or more characters", true)
            return
        }

        performLogin(profile, email, password)
    }

    private fun performLogin(profile: ProfileModel, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            val authResult = authService.register(profile, email, password)
            loading.postValue(false)
            withContext(Dispatchers.Main){
                when(authResult){
                    is AuthResult.Failure -> {
                        snackbarDispatcher.emitErrorSnackbar(authResult.errorMsg, true)
                    }
                    AuthResult.Success -> {
                        snackbarDispatcher.emitSuccessSnackbar("Sign Up Successful")
                        navigationDispatcher.emit {
                            navigate(R.id.action_registerFragment_to_homeFragment)
                        }
                    }
                }
            }
        }
    }
}