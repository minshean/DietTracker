package com.diettracker.ui.addexercise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.databinding.FragmentAddExerciseBinding
import com.diettracker.databinding.FragmentAddFoodBinding
import com.diettracker.models.ItemModel
import com.diettracker.services.auth.AuthService
import com.diettracker.utils.hideKeyboard
import com.diettracker.utils.toDateAndTime
import com.diettracker.utils.tohhmmss
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.util.*

class AddExerciseFragment : Fragment(R.layout.fragment_add_exercise) {

    private val binding by viewBinding(FragmentAddExerciseBinding::bind)

    private val snackbarDispatcher by inject<SnackbarDispatcher>()
    private val navigationDispatcher by inject<NavigationDispatcher>()
    private val authService by inject<AuthService>()

    private var date: Date? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTime.setOnClickListener {
            MaterialDialog(requireContext()).show {
                timePicker(
                    currentTime = Calendar.getInstance()
                ) { dialog, time ->
                    date = time.time
                    binding.tvTime.setText(time.time.tohhmmss())
                }
            }
        }

        binding.btnAdd.setOnClickListener {

            hideKeyboard()

            if(date == null){
                snackbarDispatcher.emitErrorSnackbar("Please Select Time")
                return@setOnClickListener
            }

            val name = binding.tvName.text.toString()
            val calories = binding.tvCalories.text.toString()

            if(name.isEmpty()){
                snackbarDispatcher.emitErrorSnackbar("Name is empty")
                return@setOnClickListener
            }

            if(calories.isEmpty() || calories.toDoubleOrNull() == null){
                snackbarDispatcher.emitErrorSnackbar("invalid Calories")
                return@setOnClickListener
            }

            authService.getCurrentUserUid()?.let { uid ->
                binding.btnAdd.isEnabled = false
                lifecycleScope.launch(Dispatchers.IO){
                    Firebase.database.reference
                        .child("exercise/$uid")
                        .push()
                        .setValue(
                            mapOf(
                                "name" to name,
                                "time" to date!!.time,
                                "calories" to calories.toDouble(),
                            )
                        ).await()

                    withContext(Dispatchers.Main){
                        binding.btnAdd.isEnabled = true
                        snackbarDispatcher.emitSuccessSnackbar("Exercise Added")
                        navigationDispatcher.emit {
                            navigateUp()
                        }
                    }
                }
            }
        }
    }

}