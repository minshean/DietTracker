package com.diettracker.ui.addfood

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.databinding.FragmentAddFoodBinding
import com.diettracker.models.ItemModel
import com.diettracker.services.auth.AuthService
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

class AddFoodFragment : Fragment(R.layout.fragment_add_food) {

    private val binding by viewBinding(FragmentAddFoodBinding::bind)

    private val snackbarDispatcher by inject<SnackbarDispatcher>()
    private val navigationDispatcher by inject<NavigationDispatcher>()
    private val authService by inject<AuthService>()

    private lateinit var item: ItemModel
    private var type: String = ""

    private var date: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            item = bundle.getParcelable("item")!!
            type = bundle.getString("type")!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvName.setText("${item.name} - ${item.calories} Calories")
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
            if(date == null){
                snackbarDispatcher.emitErrorSnackbar("Please Select Time")
                return@setOnClickListener
            }

            authService.getCurrentUserUid()?.let { uid ->
                binding.btnAdd.isEnabled = false
                lifecycleScope.launch(Dispatchers.IO){
                    Firebase.database.reference
                        .child("food/$uid")
                        .push()
                        .setValue(
                            mapOf(
                                "name" to item.name,
                                "calories" to item.calories,
                                "time" to date!!.time,
                                "type" to type
                            )
                        ).await()

                    withContext(Dispatchers.Main){
                        binding.btnAdd.isEnabled = true
                        snackbarDispatcher.emitSuccessSnackbar("Item Added")
                        navigationDispatcher.emit {
                            navigateUp()
                        }
                    }
                }
            }
        }
    }

}