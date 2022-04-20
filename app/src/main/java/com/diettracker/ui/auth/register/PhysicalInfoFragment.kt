package com.diettracker.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.databinding.FragmentPhysicalInfoBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject

class PhysicalInfoFragment : Fragment(R.layout.fragment_physical_info) {

    private val binding by viewBinding(FragmentPhysicalInfoBinding::bind)

    private val snackbarDispatcher by inject<SnackbarDispatcher>()
    private val navigationDispatcher by inject<NavigationDispatcher>()

    private lateinit var dob: String
    private lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dob = it.getString("dob") ?: ""
            gender = it.getString("gender") ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val weight = binding.etWeight.text.toString()
            val height = binding.etHeight.text.toString()

            if(weight.toDoubleOrNull() == null){
                snackbarDispatcher.emitErrorSnackbar("Weight is invalid")
                return@setOnClickListener
            }

            if(height.toDoubleOrNull() == null){
                snackbarDispatcher.emitErrorSnackbar("Height is invalid")
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putString("gender", gender)
                putString("dob", dob)
                putDouble("height", height.toDouble())
                putDouble("weight", weight.toDouble())
            }

            navigationDispatcher.emit {
                navigate(R.id.action_physicalInfoFragment_to_registerFragment, bundle)
            }
        }
    }

}