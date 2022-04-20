package com.diettracker.ui.home.tabs.items

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.databinding.FragmentItemsBinding
import com.diettracker.databinding.SingleItemBinding
import com.diettracker.models.ItemModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ItemsFragment : Fragment(R.layout.fragment_items) {

    private val binding by viewBinding(FragmentItemsBinding::bind)
    private val navigationDispatcher by inject<NavigationDispatcher>()

    private val viewModel by sharedViewModel<ItemsViewModel>()

    private val dataSource = dataSourceTypedOf<ItemModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            navigationDispatcher.emit {
                navigate(R.id.action_homeFragment_to_addItemFragment)
            }
        }

        binding.rv.setup {
            withDataSource(dataSource)
            withItem<ItemModel, ItemViewHolder>(R.layout.single_item){
                onBind(::ItemViewHolder){index, item ->
                    bind(item)
                    viewBinding.root.setOnClickListener {
                        val bundle = Bundle().apply {
                            putParcelable("item", item)
                        }
                        navigationDispatcher.emit {
                            navigate(R.id.action_homeFragment_to_addItemFragment, bundle)
                        }
                    }
                }
            }
        }

        viewModel.getAllItems().observe(viewLifecycleOwner){
            dataSource.clear()
            dataSource.addAll(it)
        }
    }

}

class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val viewBinding = SingleItemBinding.bind(itemView)

    fun bind(item: ItemModel){
        with(viewBinding){
            tvName.text = item.name
            tvCalories.text = "${item.calories} Calories"
        }
    }
}

