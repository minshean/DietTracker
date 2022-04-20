package com.diettracker.ui.home.tabs.diet

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.databinding.FragmentDietBinding
import com.diettracker.databinding.SingleFoodItemBinding
import com.diettracker.databinding.SingleItemBinding
import com.diettracker.models.FoodModel
import com.diettracker.models.ItemModel
import com.diettracker.ui.home.HomeViewModel
import com.diettracker.ui.home.tabs.items.ItemViewHolder
import com.diettracker.ui.home.tabs.items.ItemsViewModel
import com.diettracker.utils.tohhmmss
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class DietFragment : Fragment(R.layout.fragment_diet) {

    private val binding by viewBinding(FragmentDietBinding::bind)
    private val itemViewModel by sharedViewModel<ItemsViewModel>()
    private val viewModel by sharedViewModel<DietViewModel>()
    private val homeViewModel by sharedViewModel<HomeViewModel>()

    private val snackbarDispatcher by inject<SnackbarDispatcher>()
    private val navigationDispatcher by inject<NavigationDispatcher>()

    private val datasource = dataSourceTypedOf<FoodModel>()
    private val foodItemsList = mutableListOf<FoodModel>()

    private val list = mutableListOf<ItemModel>()

    private var date = Date()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemViewModel.getAllItems().observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
        }

        binding.rv.setup {
            withDataSource(datasource)
            withItem<FoodModel, FoodItemViewHolder>(R.layout.single_food_item){
                onBind(::FoodItemViewHolder){ index, item ->
                    bind(item)
                }
            }
        }

        binding.btnExercises.setOnClickListener {
            homeViewModel.openExerciseTab();
        }

        viewModel.getAllFoodItems().observe(viewLifecycleOwner) { newList ->
            foodItemsList.clear()
            foodItemsList.addAll(newList)

            val events = newList.map { Event(Color.RED, it.time) }
            binding.compactcalendarView.removeAllEvents()
            binding.compactcalendarView.addEvents(events)

            setDataForDate(date)
        }

        binding.compactcalendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date) {
                date = dateClicked
                setDataForDate(dateClicked)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                date = firstDayOfNewMonth
                setDataForDate(firstDayOfNewMonth)
            }
        })

        binding.btnAdd.setOnClickListener {
            addFood("Breakfast")
        }

        binding.btnAddLunch.setOnClickListener {
            addFood("Lunch")
        }

        binding.btnAddDinner.setOnClickListener {
            addFood("Dinner")
        }
    }

    private fun addFood(foodType: String){
        if (list.isEmpty()) {
            snackbarDispatcher.emitInfoSnackbar("Add an Item First")
            navigationDispatcher.emit {
                navigate(R.id.action_homeFragment_to_addItemFragment)
            }
            return
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an Item")

        val items: Array<String> =
            list.map { "${it.name} - ${it.calories} Calories" }.toTypedArray()
        builder.setItems(items) { dialog, which ->
            val item = list[which]
            val bundle = Bundle().apply {
                putParcelable("item", item)
                putString("type", foodType)
            }
            navigationDispatcher.emit {
                navigate(R.id.action_homeFragment_to_addFoodFragment, bundle)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setDataForDate(newDate: Date) {
        val newList = foodItemsList.filter { it.isSameDate(newDate) }
        datasource.clear()
        datasource.addAll(newList)
    }
}

class FoodItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val viewBinding = SingleFoodItemBinding.bind(itemView)

    fun bind(item: FoodModel){
        with(viewBinding){
            tvName.text = item.name
            tvCalories.text = "${item.calories} Calories"
            tvTime.text = Date(item.time).tohhmmss()
            tvType.text = item.type
        }
    }
}
