package com.ikhut.todo.domain

import androidx.lifecycle.LiveData
import com.ikhut.todo.data.ToDoDao
import com.ikhut.todo.data.ToDoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToDoRepository(private val toDoDao: ToDoDao) {
    fun getAllData(): LiveData<List<ToDoEntity>> {
        return toDoDao.getAllData()
    }

    suspend fun clearTable() {
        withContext(Dispatchers.IO) {
            toDoDao.clearTable()
        }
    }

    suspend fun insert(todo: ToDoEntity) {
        withContext(Dispatchers.IO) {
            todo.time = System.currentTimeMillis()
            toDoDao.insert(todo)
        }
    }

    suspend fun update(todo: ToDoEntity) {
        withContext(Dispatchers.IO) {
            todo.time = System.currentTimeMillis()
            toDoDao.update(todo)
        }
    }

    suspend fun delete(todo: ToDoEntity) {
        withContext(Dispatchers.IO) {
            todo.time = System.currentTimeMillis()
            toDoDao.delete(todo)
        }
    }
}