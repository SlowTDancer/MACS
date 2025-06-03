package com.ikhut.todo.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromToDoItemList(items: List<ToDoItem>): String {
        return Gson().toJson(items)
    }

    @TypeConverter
    fun toToDoItemList(json: String): List<ToDoItem> {
        val type = object : TypeToken<List<ToDoItem>>() {}.type
        return Gson().fromJson(json, type)
    }
}
