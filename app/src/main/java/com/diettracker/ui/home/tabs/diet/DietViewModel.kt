package com.diettracker.ui.home.tabs.diet

import androidx.lifecycle.ViewModel
import com.diettracker.services.items.ItemRepository

class DietViewModel(
    private val itemRepository: ItemRepository
): ViewModel() {

    fun getAllFoodItems() = itemRepository.getAllFoodItems()
}