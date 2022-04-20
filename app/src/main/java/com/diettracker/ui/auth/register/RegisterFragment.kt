package com.diettracker.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.diettracker.R
import com.diettracker.databinding.FragmentRegisterBinding
import com.diettracker.models.ProfileModel
import com.diettracker.utils.hideKeyboard
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel by viewModel<RegisterViewModel>()

    private lateinit var dob: String
    private lateinit var gender: String
    private var height: Double = 0.0
    private var weight: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dob = it.getString("dob") ?: ""
            gender = it.getString("gender") ?: ""
            height = it.getDouble("height", 0.0)
            weight = it.getDouble("weight", 0.0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            hideKeyboard()
            val profile = ProfileModel(name, dob, gender, weight, height)
            viewModel.signUp(profile, email, password)
        }

        viewModel.loading.observe(viewLifecycleOwner){ loading ->
            binding.btnSignUp.isEnabled = !loading
        }
    }
}