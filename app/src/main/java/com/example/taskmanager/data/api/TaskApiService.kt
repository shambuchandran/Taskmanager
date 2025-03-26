package com.example.taskmanager.data.api

import com.example.taskmanager.data.Task
import retrofit2.http.GET

interface TaskApiService {
    @GET("tasks")
    suspend fun fetchTasks(): List<Task>
}