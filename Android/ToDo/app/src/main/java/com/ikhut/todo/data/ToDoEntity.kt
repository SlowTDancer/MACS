package com.ikhut.todo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "to_do_table")
@Parcelize
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var pinned: Boolean = false,
    var items: List<ToDoItem> = emptyList(),
    var time: Long
) : Parcelable

@Parcelize
data class ToDoItem(
    var name: String, var isChecked: Boolean = false
) : Parcelable
