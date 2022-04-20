package com.diettracker.database

import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter



object Converters {

    const val TAG = "112233"

    // HashSet<String>
    @JvmStatic
    @TypeConverter
    fun fromStringSet(strings: HashSet<String?>?): String? {
        if (strings == null) {
            return null
        }
        val result = StringWriter()
        val json = JsonWriter(result)
        try {
            json.beginArray()
            for (s in strings) {
                json.value(s)
            }
            json.endArray()
            json.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception creating JSON", e)
        }
        return result.toString()
    }

    @JvmStatic
    @TypeConverter
    fun toStringSet(strings: String?): HashSet<String>? {
        if (strings == null) {
            return null
        }
        val reader = StringReader(strings)
        val json = JsonReader(reader)
        val result: HashSet<String> = HashSet()
        try {
            json.beginArray()
            while (json.hasNext()) {
                result.add(json.nextString())
            }
            json.endArray()
        } catch (e: IOException) {
            Log.e(TAG, "Exception parsing JSON", e)
        }
        return result
    }


    // List<String>
    @TypeConverter
    @JvmStatic
    fun StringlistToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun StringjsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()


    // FoodProduct  list
    @TypeConverter
    @JvmStatic

    fun FoodProductListToJson(value: List<FoodProduct>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic

    fun jsonToFoodProductList(value: String): List<FoodProduct>? {
        val objects = Gson().fromJson(value, Array<FoodProduct>::class.java) as Array<FoodProduct>
        return objects.toList()
    }
}