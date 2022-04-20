package com.diettracker.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.services.auth.AuthResult
import com.diettracker.services.auth.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val navigationDispatcher: NavigationDispatcher,
    private val snackbarDispatcher: SnackbarDispatcher,
    private val authService: AuthService,
): ViewModel() {

    val loading = MutableLiveData(false)

    fun logIn(email: String, password: String){

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

        performLogin(email, password)
    }

    private fun performLogin(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            val authResult = authService.logIn(email, password)
            loading.postValue(false)
            withContext(Dispatchers.Main){
                when(authResult){
                    is AuthResult.Failure -> {
                        snackbarDispatcher.emitErrorSnackbar(authResult.errorMsg, true)
                    }
                    AuthResult.Success -> {
                        snackbarDispatcher.emitSuccessSnackbar("Login Successful")
                        navigationDispatcher.emit {
                            navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                    }
                }
            }
        }
    }

    fun onDontHaveAccount() {
        navigationDispatcher.emit {
            navigate(R.id.action_loginFragment_to_infoFragment)
        }
    }
}