package com.diettracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FoodProduct::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun getProductDao(): FoodProductDao

    companion object {

        private const val FOOD_DATABASE_NAME = "foodProducts"
        private const val FOOD_DATABASE_PATH = "database/food.db"

        @Volatile
        private var instance: FoodDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance

            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(
                context.applicationContext,
                FoodDatabase::class.java,
                FOOD_DATABASE_NAME
            )
            .createFromAsset(FOOD_DATABASE_PATH)
            .build()

    }


}