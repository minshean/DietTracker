package com.diettracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.Snack
import com.diettracker.core.snackbars.SnackbarType
import com.diettracker.database.FoodDatabase
import com.diettracker.database.FoodProduct
import com.diettracker.databinding.FragmentAddIngredientsBinding
import com.diettracker.databinding.SingleIngredientListItemBinding
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import com.zhuinden.livedatacombinetuplekt.combineTuple
import com.zhuinden.livedatacombinetuplekt.combineTupleNonNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddIngredientsFragment : Fragment(R.layout.fragment_add_ingredients) {

    private val binding by viewBinding(FragmentAddIngredientsBinding::bind)

    private val dataSource = dataSourceTypedOf<ProductIngredient>()

    private val listEmitter by inject<ProductIngredientListEmitter>()

    private val navigationDispatcher by inject<NavigationDispatcher>()

    val viewModel by viewModel<AddIngredientsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.addTextChangedListener {
            viewModel.searchTerm.postValue(it.toString())
        }

        binding.btnAddItems.setOnClickListener {
            listEmitter.emit(viewModel.selectedIngredients.value ?: emptyList())
            navigationDispatcher.emit {
                navigateUp()
            }
        }

        combineTupleNonNull(viewModel.allIngredients, viewModel.selectedIngredients, viewModel.searchTerm)
            .observe(viewLifecycleOwner){ (all, selected, search) ->

                binding.btnAddItems.text = "Add items(${selected.size})"
                binding.btnAddItems.isVisible = selected.isNotEmpty()

                val selectedIds = selected.map { it.product.id }
                val initial = mutableListOf<ProductIngredient>()
                initial.addAll(selected)

                all.filter {
                    !selectedIds.contains(it.id)
                }.map { ProductIngredient(it, 0) }.let {
                    initial.addAll(it)
                }

                dataSource.set(initial.filter { it.product.name.contains(search, true) }.sortedBy { it.product.name })
            }

        binding.rvItems.setup {
            withDataSource(dataSource)
            withItem<ProductIngredient, SingleIngredientListItemVH>(R.layout.single_ingredient_list_item){
                onBind(::SingleIngredientListItemVH){ _, item ->
                    with(itemBinding){
                        tvName.text = item.product.name
                        tvQuantity.text = item.quantity.toString()

                        btnPlus.setOnClickListener {
                            viewModel.addItem(item.product)
                        }

                        btnMinus.setOnClickListener {
                            viewModel.removeItem(item.product)
                        }
                    }
                }
            }
        }
    }
}

class SingleIngredientListItemVH(itemView: View): RecyclerView.ViewHolder(itemView){
    val itemBinding = SingleIngredientListItemBinding.bind(itemView)
}

class AddIngredientsViewModel(
    private val database: FoodDatabase,
): ViewModel(){

    fun addItem(product: FoodProduct) {
        val prevItems = (selectedIngredients.value ?: emptyList()).toMutableList()
        prevItems.find { it.product.id == product.id }?.let {
            val index = prevItems.indexOf(it)
            prevItems[index] = it.copy(quantity = it.quantity + 1)

        } ?: run {
            prevItems.add(ProductIngredient(product, 1))
        }

        selectedIngredients.postValue(prevItems)
    }

    fun removeItem(product: FoodProduct) {
        val prevItems = (selectedIngredients.value ?: emptyList()).toMutableList()
        prevItems.find { it.product.id == product.id }?.let {
            val index = prevItems.indexOf(it)
            if(it.quantity == 1){
                prevItems.removeAt(index)
            } else {
                prevItems[index] = it.copy(quantity = it.quantity - 1)
            }
        }

        selectedIngredients.postValue(prevItems)
    }

    private val dao = database.getProductDao()

    val allIngredients = MutableLiveData<List<FoodProduct>>(emptyList())
    val selectedIngredients = MutableLiveData<List<ProductIngredient>>(emptyList())

    val searchTerm = MutableLiveData("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dao.getAllFood().let { allIngredients.postValue(it) }
        }
    }
}

data class ProductIngredient(
    val product: FoodProduct,
    val quantity: Int
)

class ProductIngredientListEmitter {
    private val ingrdientListEmitter: EventEmitter<List<ProductIngredient>> = EventEmitter()
    val ingredientList: EventSource<List<ProductIngredient>> = ingrdientListEmitter

    fun emit(ingredients: List<ProductIngredient>) {
        ingrdientListEmitter.emit(ingredients)
    }
}