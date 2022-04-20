package com.diettracker.services.items

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diettracker.models.ExerciseModel
import com.diettracker.models.FoodModel
import com.diettracker.models.ItemModel
import com.diettracker.services.auth.AuthService
import com.diettracker.ui.ProductIngredient
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class FirebaseItemRepository(
    private val databaseReference: DatabaseReference,
    private val authService: AuthService,
) : ItemRepository {

    override suspend fun addItem(uid: String, name: String, calories: Int): Boolean {
        try {
            val key = databaseReference.push().key!!

            databaseReference.child("Items/$uid")
                .child(key)
                .updateChildren(
                    mapOf(
                        "name" to name,
                        "id" to key,
                        "calories" to calories
                    )
                ).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun updateItem(uid: String, id: String, name: String, calories: Int): Boolean {
        return try {

            databaseReference.child("Items/$uid")
                .child(id)
                .updateChildren(
                    mapOf(
                        "name" to name,
                        "id" to id,
                        "calories" to calories
                    )
                ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItem(uid: String, id: String): Boolean {
        return try {
            databaseReference.child("Items/$uid")
                .child(id)
                .removeValue()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getAllItems(): LiveData<List<ItemModel>> {
        return object : MutableLiveData<List<ItemModel>>() {
            private val mutableLiveData = this

            private var query: Query? = null
            private val listener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ItemModel>()
                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap ->
                            snap.getValue(ItemModel::class.java)?.let {
                                list.add(it)
                            }
                        }
                    }
                    mutableLiveData.value = list.reversed()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("112233", "onCancelled: Database Error", error.toException())
                }
            }

            override fun onActive() {
                query?.removeEventListener(listener)
                val query =
                    databaseReference.child("Items/${authService.getCurrentUserUid() ?: ""}")
                this.query = query
                query.addValueEventListener(listener)
            }

            override fun onInactive() {
                query?.removeEventListener(listener)
                query = null
            }
        }
    }

    override fun getAllFoodItems(): LiveData<List<FoodModel>> {
        return object : MutableLiveData<List<FoodModel>>() {
            private val mutableLiveData = this

            private var query: Query? = null
            private val listener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<FoodModel>()
                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap ->
                            snap.getValue(FoodModel::class.java)?.let {
                                list.add(it)
                            }
                        }
                    }
                    mutableLiveData.value = list.reversed()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("112233", "onCancelled: Database Error", error.toException())
                }
            }

            override fun onActive() {
                query?.removeEventListener(listener)
                val query =
                    databaseReference.child("food/${authService.getCurrentUserUid() ?: ""}")
                this.query = query
                query.addValueEventListener(listener)
            }

            override fun onInactive() {
                query?.removeEventListener(listener)
                query = null
            }
        }
    }

    override suspend fun addRecipe(
        name: String,
        prepMethod: String,
        products: List<ProductIngredient>,
        image: Uri,
        vegetarian: Boolean,
        glutenFree: Boolean,
        dairyFree: Boolean,
    ): Boolean {
        try {
            val task: UploadTask.TaskSnapshot = Firebase.storage.reference.child("recipes/${authService.getCurrentUserUid()}/$name").putFile(image)
                .await()

            val imageUrl = task.storage.downloadUrl.await()

            val pushKey = databaseReference.push().key!!

            val calories: Float = products.map { it.product.calories * it.quantity }.sum()
            val proteins: Float = products.map { it.product.proteins * it.quantity }.sum()
            val carbs: Float = products.map { it.product.carbs * it.quantity }.sum()
            val fats: Float = products.map { it.product.fats * it.quantity }.sum()

            databaseReference.child("Recipes")
                .child(pushKey)
                .updateChildren(
                    mapOf(
                        "name" to name,
                        "id" to pushKey,
                        "prepMethod" to prepMethod,
                        "userName" to authService.getCurrentUserName(),
                        "userId" to authService.getCurrentUserUid(),
                        "calories" to calories,
                        "proteins" to proteins,
                        "imageUrl" to imageUrl.toString(),
                        "carbs" to carbs,
                        "fats" to fats,
                        "vegetarian" to vegetarian,
                        "glutenFree" to glutenFree,
                        "dairyFree" to dairyFree,
                        "ingredients" to products.map {
                            mapOf(
                                "productId" to it.product.id,
                                "quantity" to it.quantity,
                                "name" to it.product.name,
                                "calories" to it.product.calories,
                                "proteins" to it.product.proteins,
                                "carbs" to it.product.carbs,
                                "fats" to it.product.fats
                            )
                        },
                    )
                ).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun getAllExerciseItems(): LiveData<List<ExerciseModel>> {

        return object : MutableLiveData<List<ExerciseModel>>() {
            private val mutableLiveData = this

            private var query: Query? = null
            private val listener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ExerciseModel>()
                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap ->
                            snap.getValue(ExerciseModel::class.java)?.let {
                                list.add(it)
                            }
                        }
                    }
                    mutableLiveData.value = list.reversed()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("112233", "onCancelled: Database Error", error.toException())
                }
            }

            override fun onActive() {
                query?.removeEventListener(listener)
                val query =
                    databaseReference.child("exercise/${authService.getCurrentUserUid() ?: ""}")
                this.query = query
                query.addValueEventListener(listener)
            }

            override fun onInactive() {
                query?.removeEventListener(listener)
                query = null
            }
        }
    }
}