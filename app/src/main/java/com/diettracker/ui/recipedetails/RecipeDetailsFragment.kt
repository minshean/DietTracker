package com.diettracker.ui.recipedetails

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import coil.load
import com.diettracker.R
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.databinding.FragmentRecipeDetailsBinding
import com.diettracker.models.RecipeModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class RecipeDetailsFragment : Fragment(R.layout.fragment_recipe_details) {

    private val binding by viewBinding(FragmentRecipeDetailsBinding::bind)

    private val viewModel by viewModel<RecipeDetailsViewModel>()

    private val snackbarDispatcher by inject<SnackbarDispatcher>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recipeState.observe(viewLifecycleOwner){ recipeModel ->
            recipeModel?.let { recipe: RecipeModel ->
                with(binding){
                    recipeImage.load(recipe.imageUrl)
                    tvRecipeName.text = recipe.name
                    tvProteins.text = recipe.proteins.roundOffDecimal().toString() + "g"
                    tvCalories.text = recipe.calories.roundOffDecimal().toString() + "g"
                    tvCarbohydrates.text = recipe.carbs.roundOffDecimal().toString() + "g"
                    tvFats.text = recipe.fats.roundOffDecimal().toString() + "g"

                    val ingredients = recipe.ingredients.joinToString(separator = "\n") {
                        it.quantity.toString() + " - " + it.name
                    }

                    val reviewsHtml = recipe.reviews.values.reversed().joinToString(separator = "\n") {
                        "<h6>${it.user}</h6> ${it.text} <br>"
                    }
                    tvReviews.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(reviewsHtml, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(reviewsHtml)
                    }

                    tvPreparation.text = recipe.prepMethod

                    tvIngredients.text = ingredients
                }
            }
        }

        binding.btnAddReview.setOnClickListener {
            showdialog()
        }

        arguments?.let { args ->
            args.getString("recipeId")?.let { recipeId ->
                viewModel.getRecipe(recipeId)
            }
        }
    }

    fun showdialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Submit Review")
        val input = EditText(requireContext())
        input.setHint("Enter Text")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var text = input.text.toString().trim()
            if(text.isEmpty()){
                snackbarDispatcher.emitErrorSnackbar("Empty Message")
            } else {
                viewModel.addReview(text)
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}

fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}