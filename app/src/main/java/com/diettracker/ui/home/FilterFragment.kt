package com.diettracker.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.diettracker.R
import com.diettracker.databinding.FragmentFilterBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private val binding by viewBinding(FragmentFilterBinding::bind)

    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filterState.observe(viewLifecycleOwner) {
            with(binding) {
                btnAll.setSelectedForCheck(it.calorieType == CalorieType.All)
                btnUnder200.setSelectedForCheck(it.calorieType == CalorieType.Under200)
                btn200To400.setSelectedForCheck(it.calorieType == CalorieType.Between200To400)
                btn400To600.setSelectedForCheck(it.calorieType == CalorieType.Between400To600)
                btnAbove600.setSelectedForCheck(it.calorieType == CalorieType.Over600)
                btnVegetarian.setSelectedForCheck(it.vegetarian)
                btnDairyFree.setSelectedForCheck(it.dairyFree)
                btnGlutenFree.setSelectedForCheck(it.glutenFree)
            }
        }

        binding.btnUnder200.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(calorieType = CalorieType.Under200)
        }

        binding.btnAll.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(calorieType = CalorieType.All)
        }

        binding.btn200To400.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(calorieType = CalorieType.Between200To400)
        }

        binding.btn400To600.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(calorieType = CalorieType.Between400To600)
        }

        binding.btnAbove600.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(calorieType = CalorieType.Over600)
        }

        binding.btnGlutenFree.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(glutenFree = !viewModel.filterState.value?.glutenFree!!)
        }

        binding.btnDairyFree.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(dairyFree = !viewModel.filterState.value?.dairyFree!!)
        }

        binding.btnVegetarian.setOnClickListener {
            viewModel.filterState.value = viewModel.filterState.value?.copy(vegetarian = !viewModel.filterState.value?.vegetarian!!)
        }
    }
}

private fun ImageView.setSelectedForCheck(selected: Boolean) {
    if (selected) {
        setImageResource(R.drawable.checked)
    } else {
        setImageResource(R.drawable.unchecked)
    }
}