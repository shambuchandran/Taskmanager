package com.example.taskmanager.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmanager.data.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: Long
)

// Mapper functions
fun TaskEntity.toTask() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)