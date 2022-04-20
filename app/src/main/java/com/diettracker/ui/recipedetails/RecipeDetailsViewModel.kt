package com.diettracker.ui.recipedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diettracker.models.RecipeModel
import com.diettracker.notifications.PushNotificationSender
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecipeDetailsViewModel(
    private val pushNotificationSender: PushNotificationSender
): ViewModel() {

    val recipeState = MutableLiveData<RecipeModel?>(null)

    private var eventListener: ValueEventListener? = null

    private var recipeId: String = ""

    fun getRecipe(recipeId: String){
        this.recipeId = recipeId
        eventListener = FirebaseDatabase.getInstance().reference.child("Recipes")
            .child(recipeId).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        snapshot.getValue(RecipeModel::class.java)?.let {
                            recipeState.value = it
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onCleared() {
        super.onCleared()

        eventListener?.let {
            FirebaseDatabase.getInstance().reference.child("Recipes")
                .child(recipeId)
                .removeEventListener(it)
        }
    }

    fun addReview(text: String) {
        val name = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"
        if(recipeId.isNotEmpty()){
            FirebaseDatabase.getInstance().reference.child("Recipes")
                .child(recipeId).child("reviews").push().setValue(
                    mapOf(
                        "text" to text,
                        "user" to name,
                    )
                )
        }

        sendPushNotification(text)
    }

    private fun sendPushNotification(text: String) {
        recipeState.value?.userId?.let { userId ->
            FirebaseDatabase.getInstance().reference.child("Tokens")
                .child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.getValue(String::class.java)?.let { token ->
                            pushNotificationSender.sendNotificationToToken(token, text)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}