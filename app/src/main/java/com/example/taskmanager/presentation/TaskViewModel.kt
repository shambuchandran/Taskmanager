package com.example.taskmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent

    init {
        viewModelScope.launch {
            repository.tasks.collect { _tasks.value = it }
        }
    }
    fun addTask(title: String, description: String) = viewModelScope.launch {
        val newTask = Task(
            title = title,
            description = description,
            isCompleted = false
        )
        repository.addTask(newTask)
        _uiEvent.emit("Task Added")
    }

    fun editTask(task: Task, newTitle: String, newDescription: String) = viewModelScope.launch {
        val updatedTask = task.copy(title = newTitle, description = newDescription)
        repository.editTask(updatedTask)
        _uiEvent.emit("Task Updated")
    }

    fun toggleTaskCompletion(task: Task) = viewModelScope.launch {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        if (updatedTask.isCompleted) {
            repository.markTaskCompleted(updatedTask)
        } else {
            repository.updateTask(updatedTask)
        }
        _uiEvent.emit("Task Marked Completed")
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
        _uiEvent.emit("Task Deleted")
    }

    fun syncWithRemote() = viewModelScope.launch {
        repository.fetchRemoteTasks()
        _uiEvent.emit("Tasks Synced with Server")
    }

    fun triggerTestCrash() {
        throw RuntimeException("Test crash triggered by user")
    }

    fun triggerDatabaseError() = viewModelScope.launch {
        repository.triggerDatabaseCrash()
        _uiEvent.emit("Error in database update")
    }
}