package com.diettracker.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.services.auth.AuthService
import com.diettracker.services.session.SessionManager
import org.koin.android.ext.android.inject

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val handler = Handler(Looper.getMainLooper())
    private val authService by inject<AuthService>()
    private val navigationDispatcher by inject<NavigationDispatcher>()
    private val sessionManager by inject<SessionManager>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed(
            {
                if (authService.isUserLoggedIn()) {
                    navigationDispatcher.emit {
                        navigate(R.id.action_splashFragment_to_homeFragment)
                    }
                } else {
                    if(sessionManager.isOnboardingSeen()){
                        navigationDispatcher.emit {
                            navigate(R.id.action_splashFragment_to_authSelectionFragment)
                        }
                    } else {
                        navigationDispatcher.emit {
                            navigate(R.id.action_splashFragment_to_onboardingFragment)
                        }
                    }
                }
            }, 2000L
        )
    }
}