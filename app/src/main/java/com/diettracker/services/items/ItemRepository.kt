package com.diettracker.services.items

import android.net.Uri
import androidx.lifecycle.LiveData
import com.diettracker.models.ExerciseModel
import com.diettracker.models.FoodModel
import com.diettracker.models.ItemModel
import com.diettracker.ui.ProductIngredient

interface ItemRepository {
    suspend fun addItem(uid: String, name: String, calories: Int): Boolean
    suspend fun updateItem(uid: String, id: String, name: String, calories: Int): Boolean
    suspend fun deleteItem(uid: String, id: String): Boolean
    fun getAllItems(): LiveData<List<ItemModel>>
    fun getAllFoodItems(): LiveData<List<FoodModel>>
    fun getAllExerciseItems(): LiveData<List<ExerciseModel>>

    suspend fun addRecipe(
        name: String,
        prepMethod: String,
        products: List<ProductIngredient>,
        image: Uri,
        vegetarian: Boolean,
        glutenFree: Boolean,
        dairyFree: Boolean,
    ): Boolean
}