package com.diettracker.ui.addrecipe

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.databinding.FragmentAddRecipeBinding
import com.diettracker.ui.ProductIngredientListEmitter
import com.github.dhaval2404.imagepicker.ImagePicker
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import com.zhuinden.liveevent.observe
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    private val binding by viewBinding(FragmentAddRecipeBinding::bind)
    private val navigationDispatcher by inject<NavigationDispatcher>()

    private val viewModel by viewModel<AddRecipeViewModel>()

    private val listEmitter by inject<ProductIngredientListEmitter>()

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                viewModel.imageUri.postValue(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddIngredient.setOnClickListener {
            navigationDispatcher.emit {
                navigate(R.id.action_addRecipeFragment_to_addIngredientsFragment)
            }
        }

        viewModel.imageUri.observe(viewLifecycleOwner) {
            binding.imageView.setImageURI(it)
        }

        binding.btnAddRecipe.setOnClickListener {
            viewModel.addRecipe(
                binding.etName.text.toString().trim(),
                binding.etPrepMethod.text.toString().trim(),
            binding.tvVegetarianSwitch.isChecked,
            binding.tvGlutenFreeSwitch.isChecked,
            binding.tvDairyFree.isChecked,)
        }

        viewModel.loading.observe(viewLifecycleOwner){
            binding.btnAddRecipe.isEnabled = !it
            binding.progressBar.isVisible = it
        }

        binding.btnPickImage.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .crop()//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        viewModel.ingredients.observe(viewLifecycleOwner){ list ->
            val carbs = list.map { it.product.carbs * it.quantity }.sum()
            val protein = list.map { it.product.proteins * it.quantity }.sum()
            val fats = list.map { it.product.fats * it.quantity }.sum()
            val calories = list.map { it.product.calories * it.quantity }.sum()

            binding.tvInfo.text = "Carbs: $carbs\nProtein: $protein\nFats: $fats\nCalories: $calories\nTotal Ingredients: ${list.size}"
        }

        listEmitter.ingredientList.observe(viewLifecycleOwner){
            viewModel.setIngredients(it)
        }
    }
}