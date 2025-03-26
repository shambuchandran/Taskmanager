package com.example.taskmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    val tasks = repository.tasks

    fun addTask(title: String, description: String) = viewModelScope.launch {
        val newTask = Task(
            title = title,
            description = description,
            isCompleted = false
        )
        repository.addTask(newTask)
    }

    fun toggleTaskCompletion(task: Task) = viewModelScope.launch {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        repository.updateTask(updatedTask)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun syncWithRemote() = viewModelScope.launch {
        repository.fetchRemoteTasks()
    }

    fun triggerTestCrash() {
        throw RuntimeException("Test crash triggered by user")
    }

    fun triggerDatabaseError() = viewModelScope.launch {
        repository.triggerDatabaseCrash()
    }
}