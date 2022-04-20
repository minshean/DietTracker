package com.diettracker.ui.addrecipe

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.services.items.ItemRepository
import com.diettracker.ui.ProductIngredient
import kotlinx.coroutines.launch
import javax.microedition.khronos.opengles.GL

class AddRecipeViewModel(
    private val itemRepository: ItemRepository,
    private val snackbarDispatcher: SnackbarDispatcher,
    private val navigationDispatcher: NavigationDispatcher,
): ViewModel() {

    val ingredients = MutableLiveData<List<ProductIngredient>>(emptyList())

    val imageUri = MutableLiveData<Uri?>(null)

    fun setIngredients(it: List<ProductIngredient>) {
        ingredients.value = it
    }

    fun addRecipe(name: String, method: String, vegetarian: Boolean, glutenFree: Boolean, dairyFree: Boolean) {

        if(name.isEmpty() || method.isEmpty()) {
            snackbarDispatcher.emitErrorSnackbar("Please fill in all fields")
            return
        }

        if(imageUri.value == null){
            snackbarDispatcher.emitErrorSnackbar("Please add an image")
            return
        }

        if(ingredients.value.isNullOrEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Please add ingredients")
            return
        }

        viewModelScope.launch {
            loading.postValue(true)
            val result = itemRepository.addRecipe(name, method, ingredients.value!!, imageUri.value!!, vegetarian, glutenFree, dairyFree)
            loading.postValue(false)
            if(result){
                snackbarDispatcher.emitSuccessSnackbar("Recipe added successfully")
                navigationDispatcher.emit { navigateUp() }
            } else {
                snackbarDispatcher.emitErrorSnackbar("Error adding recipe")
            }
        }
    }

    val loading = MutableLiveData(false)


}