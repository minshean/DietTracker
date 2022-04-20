package com.diettracker.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.databinding.FragmentInfoBinding
import com.diettracker.utils.toddMMyyyy
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import java.util.*

class InfoFragment : Fragment(R.layout.fragment_info) {

    private val binding by viewBinding(FragmentInfoBinding::bind)

    private val snackbarDispatcher by inject<SnackbarDispatcher>()
    private val navigationDispatcher by inject<NavigationDispatcher>()

    private var selectedDate: Date? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val selectedChip = binding.genderChipGroup.checkedChipId
            val gender = if(selectedChip == R.id.chipMale) "Male" else "Female"
            if(selectedDate == null){
                snackbarDispatcher.emitErrorSnackbar("Enter Date of Birth...")
                return@setOnClickListener
            }
            val birthDay = selectedDate!!.toddMMyyyy()

            val bundle = Bundle().apply {
                putString("gender", gender)
                putString("dob", birthDay)
            }

            navigationDispatcher.emit {
                navigate(R.id.action_infoFragment_to_physicalInfoFragment, bundle)
            }
        }

        binding.etBirthday.setOnClickListener {
            val currentDate = Calendar.getInstance()
            currentDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR) - 30)

            if(selectedDate != null){
                currentDate.time = selectedDate!!
            }

            val maxDate = Calendar.getInstance()
            val minDate = Calendar.getInstance()
            maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) - 15)
            minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR) - 80)
            MaterialDialog(requireContext()).show {
                datePicker(
                    maxDate = maxDate,
                    minDate = minDate,
                    currentDate = currentDate
                ) { _, date ->
                   selectedDate = date.time
                    binding.etBirthday.setText(date.time.toddMMyyyy())
                }
            }
        }
    }
}