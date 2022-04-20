package com.diettracker.ui.home.tabs.profile

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.databinding.FragmentProfileBinding
import com.diettracker.models.ProfileModel
import com.diettracker.services.auth.AuthService
import com.diettracker.services.profile.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by sharedViewModel<ProfileViewModel>()
    private val authService by inject<AuthService>()
    private val navigationDispatcher by inject<NavigationDispatcher>()
    private val snackbarDispatcher by inject<SnackbarDispatcher>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it.loading
            it.profile?.let { profileModel ->
                binding.tvName.setText(profileModel.name)
                binding.tvDOB.setText(profileModel.dateOfBirth)
                binding.tvGender.setText(profileModel.gender)
                binding.tvHeight.setText("${profileModel.height} cm")
                binding.tvWeight.setText("${profileModel.weight} lb")
            }
        }

        binding.btnUpdateWeight.setOnClickListener {
            showdialog()
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout Confirmation")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes"){_, _ ->
                    authService.signOut()
                    navigationDispatcher.emit {
                        navigate(R.id.action_homeFragment_to_splashFragment)
                    }
                }
                .setNegativeButton("No"){_, _ -> }
                .show()
        }
    }

    fun showdialog(){
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Submit Review")
        val input = EditText(requireContext())
        input.setHint("Enter Text")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var text = input.text.toString().trim()
            if(text.isEmpty()){
                snackbarDispatcher.emitErrorSnackbar("Empty Message")
            } else if(text.toDoubleOrNull() == null){
                snackbarDispatcher.emitErrorSnackbar("Invalid Weight")
            } else {
               FirebaseDatabase.getInstance()
                   .reference
                   .child("profiles")
                   .child(FirebaseAuth.getInstance().currentUser!!.uid)
                   .child("weight")
                   .setValue(text.toDouble())
                   .addOnSuccessListener {
                       snackbarDispatcher.emitSuccessSnackbar("Weight Updated Successfully")

                       FirebaseDatabase.getInstance()
                           .reference
                           .child("profiles")
                           .child(FirebaseAuth.getInstance().currentUser!!.uid)
                           .child("weightChanges")
                           .push()
                           .setValue(text.toDouble())
                           .addOnSuccessListener {
                               viewModel.loadProfile()
                           }

                   }
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}

class ProfileViewModel(
    private val authService: AuthService,
    private val profileRepository: ProfileRepository
): ViewModel(){

    val state = MutableLiveData(ProfileState())

    init {
        loadProfile()
    }

    fun loadProfile() {
        authService.getCurrentUserUid()?.let{ uid ->
            viewModelScope.launch {
               val profile = profileRepository.getProfile(uid)
                state.value = state.value?.copy(
                    loading = false,
                    profile = profile
                )
            }
        }
    }
}

data class ProfileState(
    val loading: Boolean = true,
    val profile: ProfileModel? = null
)