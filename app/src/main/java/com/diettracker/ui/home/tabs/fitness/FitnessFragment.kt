package com.diettracker.ui.home.tabs.fitness

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.databinding.FragmentFitnessBinding
import com.diettracker.databinding.SingleExerciseItemBinding
import com.diettracker.databinding.SingleFoodItemBinding
import com.diettracker.models.ExerciseModel
import com.diettracker.models.FoodModel
import com.diettracker.services.items.ItemRepository
import com.diettracker.ui.home.tabs.diet.FoodItemViewHolder
import com.diettracker.utils.tohhmmss
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import java.util.*

class FitnessFragment : Fragment(R.layout.fragment_fitness) {
    private val binding by viewBinding(FragmentFitnessBinding::bind)

    private val itemRepository by inject<ItemRepository>()

    private val navigationDispatcher by inject<NavigationDispatcher>()

    private val datasource = dataSourceTypedOf<ExerciseModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemRepository.getAllExerciseItems().observe(viewLifecycleOwner){
            datasource.clear()
            datasource.addAll(it)
        }

        binding.btnAdd.setOnClickListener {
            navigationDispatcher.emit {
                navigate(R.id.action_homeFragment_to_addExerciseFragment)
            }
        }

        binding.rv.setup {
            withDataSource(datasource)
            withItem<ExerciseModel, ExerciseItemViewHolder>(R.layout.single_exercise_item){
                onBind(::ExerciseItemViewHolder){ index, item ->
                    bind(item)
                }
            }
        }
    }
}

class ExerciseItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val viewBinding = SingleExerciseItemBinding.bind(itemView)

    fun bind(item: ExerciseModel){
        with(viewBinding){
            tvName.text = item.name
            tvTime.text = Date(item.time).tohhmmss()
            tvCalories.text = "${item.calories} cal"
        }
    }
}