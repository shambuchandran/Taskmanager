package com.example.taskmanager.data.api

import com.example.taskmanager.data.Task
import retrofit2.http.GET

//interface TaskApiService {
//    @GET("tasks")
//    suspend fun fetchTasks(): List<Task>
//}

interface TaskApiService {
    @GET("tasks.json")
    suspend fun fetchTasks(): List<Task>
}