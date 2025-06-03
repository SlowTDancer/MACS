package com.ikhut.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDao {
    @Query("SELECT * FROM to_do_table ORDER BY time DESC")
    fun getAllData(): LiveData<List<ToDoEntity>>

    @Query("DELETE FROM to_do_table")
    suspend fun clearTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: ToDoEntity)

    @Update
    suspend fun update(todo: ToDoEntity)

    @Delete
    suspend fun delete(todo: ToDoEntity)
}