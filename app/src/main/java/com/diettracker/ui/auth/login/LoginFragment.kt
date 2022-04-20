package com.diettracker.ui.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.diettracker.R
import com.diettracker.databinding.FragmentLoginBinding
import com.diettracker.utils.hideKeyboard
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            hideKeyboard()
            viewModel.logIn(email, password)
        }

        viewModel.loading.observe(viewLifecycleOwner){ loading ->
            binding.btnLogin.isEnabled = !loading
        }

        binding.btnDontHaveAccount.setOnClickListener {
            viewModel.onDontHaveAccount()
        }
    }

}