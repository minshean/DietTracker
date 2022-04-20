package com.diettracker.ui.additem

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.diettracker.R
import com.diettracker.databinding.FragmentAddItemBinding
import com.diettracker.models.ItemModel
import com.diettracker.utils.hideKeyboard
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddItemFragment : Fragment(R.layout.fragment_add_item) {

    private val binding by viewBinding(FragmentAddItemBinding::bind)

    private val viewModel by viewModel<AddItemViewModel>()

    private var item: ItemModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            item = bundle.getParcelable("item")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(item != null){
            binding.toolbar.text = "Update Food Item"
            binding.tvName.setText(item!!.name)
            binding.tvCalories.setText(item!!.calories.toString())
            binding.btnAdd.text = "Update"
            binding.btnDelete.isVisible = true
        }

        binding.btnDelete.setOnClickListener {
            item?.let { itemToDelete ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Confirmation")
                    .setMessage("Do you want to delete this item?")
                    .setPositiveButton("Yes"){_, _ ->
                        viewModel.delete(itemToDelete)
                    }
                    .setNegativeButton("No"){_, _ -> }
                    .show()
            }
        }

        binding.btnAdd.setOnClickListener {
            hideKeyboard()
            if(item == null){
                viewModel.addItem(
                    name = binding.tvName.text.toString().trim(),
                    calories = binding.tvCalories.text.toString().trim(),
                )
            } else {
                viewModel.updateItem(
                    name = binding.tvName.text.toString().trim(),
                    calories = binding.tvCalories.text.toString().trim(),
                    id = item!!.id
                )
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){ loading ->
            binding.btnAdd.isEnabled = !loading
        }
    }

}