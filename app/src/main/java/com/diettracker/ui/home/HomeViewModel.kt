package com.diettracker.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diettracker.models.RecipeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeViewModel: ViewModel() {

    val openExerciseTab = MutableLiveData<Boolean?>(null)

    fun openExerciseTab() {
        openExerciseTab.value = true
    }

    val  recipesList = MutableLiveData<List<RecipeModel>>(emptyList())

    val filterState = MutableLiveData(FilterState())

    init {
        Firebase.database.reference.child("Recipes").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<RecipeModel>()
                snapshot.children.forEach { snap ->
                    snap.getValue(RecipeModel::class.java)?.let {
                        list.add(it)
                    }
                }
                recipesList.value = list
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}

data class FilterState(
    var calorieType: CalorieType = CalorieType.All,
    var vegetarian: Boolean = false,
    var dairyFree: Boolean = false,
    var glutenFree: Boolean = false
)

enum class CalorieType{
    All, Under200, Between200To400, Between400To600, Over600
}