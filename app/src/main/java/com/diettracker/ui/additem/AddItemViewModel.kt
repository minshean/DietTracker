package com.diettracker.ui.additem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.models.ItemModel
import com.diettracker.services.auth.AuthService
import com.diettracker.services.items.FirebaseItemRepository
import com.diettracker.services.items.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemViewModel(
    private val snackbarDispatcher: SnackbarDispatcher,
    private val navigationDispatcher: NavigationDispatcher,
    private val authService: AuthService,
    private val itemRepository: ItemRepository
): ViewModel() {

    val loading = MutableLiveData(false)

    fun addItem(name: String, calories: String){
        if(name.isEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Name is empty")
            return
        }

        if(calories.isEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Please Enter Calories")
            return
        }

        if(calories.toIntOrNull() == null){
            snackbarDispatcher.emitErrorSnackbar("Invalid Calories")
            return
        }

        if(calories.toInt() <= 0){
            snackbarDispatcher.emitErrorSnackbar("Calories cannot be zero or less")
            return
        }

        val uid = authService.getCurrentUserUid()

        if(uid == null){
            snackbarDispatcher.emitErrorSnackbar("No User Found")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            val result = itemRepository.addItem(uid, name, calories.toInt())
            withContext(Dispatchers.Main){
                if(result){
                    snackbarDispatcher.emitSuccessSnackbar("Item Added")
                    navigationDispatcher.emit {
                        navigateUp()
                    }
                } else {
                    snackbarDispatcher.emitErrorSnackbar("Item Add Failed")
                }
            }
        }
    }

    fun updateItem(name: String, id: String, calories: String){
        if(name.isEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Name is empty")
            return
        }

        if(calories.isEmpty()){
            snackbarDispatcher.emitErrorSnackbar("Please Enter Calories")
            return
        }

        if(calories.toIntOrNull() == null){
            snackbarDispatcher.emitErrorSnackbar("Invalid Calories")
            return
        }

        if(calories.toInt() <= 0){
            snackbarDispatcher.emitErrorSnackbar("Calories cannot be zero or less")
            return
        }

        val uid = authService.getCurrentUserUid()

        if(uid == null){
            snackbarDispatcher.emitErrorSnackbar("No User Found")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            val result = itemRepository.updateItem(uid,id, name, calories.toInt())
            withContext(Dispatchers.Main){
                if(result){
                    snackbarDispatcher.emitSuccessSnackbar("Item Updated")
                    navigationDispatcher.emit {
                        navigateUp()
                    }
                } else {
                    snackbarDispatcher.emitErrorSnackbar("Item Update Failed")
                }
            }
        }
    }

    fun delete(itemToDelete: ItemModel) {
        val uid = authService.getCurrentUserUid()

        if(uid == null){
            snackbarDispatcher.emitErrorSnackbar("No User Found")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            val result = itemRepository.deleteItem(uid,itemToDelete.id)
            withContext(Dispatchers.Main){
                if(result){
                    snackbarDispatcher.emitSuccessSnackbar("Item Deleted")
                    navigationDispatcher.emit {
                        navigateUp()
                    }
                } else {
                    snackbarDispatcher.emitErrorSnackbar("Item Delete Failed")
                }
            }
        }
    }
}