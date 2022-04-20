package com.diettracker.ui.home.tabs.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.diettracker.services.auth.AuthService
import com.diettracker.services.items.ItemRepository

class ItemsViewModel(
    private val itemRepository: ItemRepository
): ViewModel() {

    fun getAllItems() = itemRepository.getAllItems()
}