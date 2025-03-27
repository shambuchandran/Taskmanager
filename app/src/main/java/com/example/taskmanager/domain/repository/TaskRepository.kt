package com.example.taskmanager.domain.repository

import com.example.taskmanager.data.Task
import com.example.taskmanager.data.api.TaskApiService
import com.example.taskmanager.data.room.TaskDao
import com.example.taskmanager.data.room.TaskEntity
import com.example.taskmanager.data.room.toEntity
import com.example.taskmanager.data.room.toTask
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.performance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService,
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) {
    val tasks: Flow<List<Task>> = taskDao.getAllTasks()
        .map { entities -> entities.map { it.toTask() } }
        .catch { e ->
            crashlytics.log("Database error in tasks flow")
            crashlytics.recordException(e)
            throw e
        }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
        analytics.logEvent("task_added") {
            param("task_id", task.id.toString())
            param("title", task.title)
        }
    }

    suspend fun editTask(task: Task) {
        taskDao.updateTask(task.toEntity())
        analytics.logEvent("task_edited") {
            param("task_id", task.id.toString())
            param("title", task.title)
        }
        taskDao.getAllTasks().first()
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
        analytics.logEvent("task_updated") {
            param("task_id", task.id.toString())
            param("title", task.title)
            param("completed_status", task.isCompleted.toString())
        }
    }

    suspend fun markTaskCompleted(task: Task) {
        taskDao.updateTask(task.copy(isCompleted = true).toEntity())
        analytics.logEvent("task_completed") {
            param("task_id", task.id.toString())
            param("title", task.title)
        }
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
        analytics.logEvent("task_deleted") {
            param("task_id", task.id.toString())
            param("task_title", task.title)
        }
    }
    class TaskSyncException(message: String, cause: Throwable? = null) : Exception(message, cause)

    suspend fun fetchRemoteTasks() {
        val trace = Firebase.performance.newTrace("fetch_tasks")
        trace.start()
        try {
            val remoteTasks = taskApiService.fetchTasks()
            trace.incrementMetric("tasks_received", remoteTasks.size.toLong())
            remoteTasks.forEach { taskDao.insertTask(it.toEntity()) }
        } catch (e: Exception) {
            trace.putAttribute("error", e.javaClass.simpleName)
            crashlytics.recordException(e)
            throw TaskSyncException("Failed to fetch tasks", e)
        } finally {
            trace.stop()
        }
    }
    suspend fun triggerDatabaseCrash() {
        try {
            // Attempt to insert duplicate task to the same primary key
            val entity = TaskEntity(id = 0, title = "Crash", description = "Invalid", isCompleted = false, createdAt = 0)
            taskDao.insertTask(entity)
            taskDao.insertTask(entity) // Second insert with same ID
        } catch (e: Exception) {
            crashlytics.recordException(e)
            throw e
        }
    }

}