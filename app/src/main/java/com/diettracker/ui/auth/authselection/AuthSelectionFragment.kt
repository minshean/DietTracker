package com.diettracker.ui.auth.authselection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.databinding.FragmentAuthSelectionBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject

class AuthSelectionFragment : Fragment(R.layout.fragment_auth_selection) {

    private val binding by viewBinding(FragmentAuthSelectionBinding::bind)
    private val navigationDispatcher by inject<NavigationDispatcher>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            btnLogin.setOnClickListener {
                navigationDispatcher.emit {
                    navigate(R.id.action_authSelectionFragment_to_loginFragment)
                }
            }

            btnSignUp.setOnClickListener {
                navigationDispatcher.emit {
                    navigate(R.id.action_authSelectionFragment_to_infoFragment)
                }
            }
        }
    }
}