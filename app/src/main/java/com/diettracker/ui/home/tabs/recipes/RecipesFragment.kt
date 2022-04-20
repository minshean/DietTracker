package com.diettracker.ui.home.tabs.recipes

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.databinding.FragmentRecipesBinding
import com.diettracker.databinding.SingleRecpieCardBinding
import com.diettracker.models.RecipeModel
import com.diettracker.ui.home.HomeViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import com.zhuinden.livedatacombinetuplekt.combineTuple
import com.zhuinden.livedatacombinetuplekt.combineTupleNonNull
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val binding by viewBinding(FragmentRecipesBinding::bind)

    val dataSource = dataSourceTypedOf<RecipeModel>()

    val homeViewModel by sharedViewModel<HomeViewModel>()

    val navigationDispatcher by inject<NavigationDispatcher>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFilter.setOnClickListener {
            navigationDispatcher.emit {
                navigate(R.id.action_homeFragment_to_filterFragment)
            }
        }

        combineTupleNonNull(homeViewModel.recipesList, homeViewModel.filterState).observe(viewLifecycleOwner){ (recipesList, filter) ->
            val filteredRecipes = recipesList.filter { it.filterable(filter) }
            dataSource.clear()
            dataSource.addAll(filteredRecipes)
        }

        binding.rvRecipes.setup {
            withDataSource(dataSource)
            withLayoutManager(GridLayoutManager(requireContext(), 2))
            withItem<RecipeModel, RecipeViewHolder>(R.layout.single_recpie_card) {
                onBind(::RecipeViewHolder) { index, item ->
                    with(itemBinding){
                        recpieImage.load(item.imageUrl)
                        tvCalories.text = item.calories.toInt().toString() + " CAL"
                        tvName.text = item.name

                        root.setOnClickListener {
                            navigationDispatcher.emit {
                                navigate(R.id.action_homeFragment_to_recipeDetailsFragment, Bundle().apply {
                                    putString("recipeId", item.id)
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val itemBinding = SingleRecpieCardBinding.bind(itemView)
}