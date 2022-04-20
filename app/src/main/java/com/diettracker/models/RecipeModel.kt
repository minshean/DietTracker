package com.diettracker.models

import com.diettracker.ui.home.CalorieType
import com.diettracker.ui.home.FilterState

data class RecipeModel(
    val calories: Double,
    val carbs: Double,
    val fats: Double,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val id: String,
    val imageUrl: String,
    val name: String,
    val prepMethod: String,
    val proteins: Double,
    val userId: String,
    val userName: String,
    val vegetarian: Boolean,
    val ingredients: List<IngredientModel>,
    val reviews: Map<String, ReviewModel> = emptyMap(),
){

    // This method checks for all four conditions of the filter
    fun filterable(filter: FilterState): Boolean {
        val calorieCondition = when(filter.calorieType){
            CalorieType.Under200 -> calories < 200.0
            CalorieType.Between200To400 -> calories in 200.0..400.0
            CalorieType.Between400To600 -> calories in 400.0..600.0
            CalorieType.Over600 -> calories > 600.0
            else -> true
        }
        val glutenCondition = glutenFree == filter.glutenFree
        val dairyCondition = dairyFree == filter.dairyFree
        val vegetarianCondition = vegetarian == filter.vegetarian

        return calorieCondition && glutenCondition && dairyCondition && vegetarianCondition
    }


    constructor(): this(0.0, 0.0, 0.0, false, false, "","", "", "", 0.0, "", "", false, listOf())
}

data class ReviewModel(
    val text: String,
    val user: String,
) {
    constructor(): this("", "")
}

data class IngredientModel(
    val carbs: Double,
    val calories: Double,
    val fats: Double,
    val name: String,
    val productId: Int,
    val proteins: Double,
    val quantity: Int,
){
    constructor(): this(0.0, 0.0, 0.0, "", 0, 0.0, 0)
}
