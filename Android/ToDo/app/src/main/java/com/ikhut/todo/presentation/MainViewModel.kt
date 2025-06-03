package com.ikhut.todo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ikhut.todo.data.ToDoEntity
import com.ikhut.todo.domain.ToDoRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ToDoRepository) : ViewModel() {
    val todoList: LiveData<List<ToDoEntity>> = repository.getAllData()

    fun insertTodo(todo: ToDoEntity) {
        viewModelScope.launch {
            repository.insert(todo)
        }
    }

    fun updateTodo(todo: ToDoEntity) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    fun deleteTodo(todo: ToDoEntity) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    fun clearAllTodos() {
        viewModelScope.launch {
            repository.clearTable()
        }
    }

    companion object {
        fun create(repository: ToDoRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(repository) as T
                }
            }
        }
    }
}
