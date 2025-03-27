package com.example.taskmanager.data.api

import com.example.taskmanager.data.Task
import kotlinx.coroutines.delay
import kotlin.random.Random

//class MockTaskApiService : TaskApiService {
//    override suspend fun fetchTasks(): List<Task> {
//        delay(Random.nextLong(500, 1500))
//
//        return listOf(
//            Task(
//                id = 1,
//                title = "Complete Project Presentation",
//                description = "Prepare slides for the quarterly review meeting",
//                isCompleted = true,
//                createdAt = System.currentTimeMillis() - 259200000 // 3 days ago
//            ),
//            Task(
//                id = 2,
//                title = "Schedule Team Meeting",
//                description = "Coordinate with team members for sprint planning",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 172800000 // 2 days ago
//            ),
//            Task(
//                id = 3,
//                title = "Review Code Changes",
//                description = "Check pull requests from the development team",
//                isCompleted = true,
//                createdAt = System.currentTimeMillis() - 86400000 // 1 day ago
//            ),
//            Task(
//                id = 4,
//                title = "Update Documentation",
//                description = "Add new API endpoints to the documentation",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 43200000 // 12 hours ago
//            ),
//            Task(
//                id = 5,
//                title = "Fix Bug #1234",
//                description = "Address crash in profile section",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 21600000 // 6 hours ago
//            ),
//            Task(
//                id = 6,
//                title = "Send Weekly Report",
//                description = "Compile and send progress report to stakeholders",
//                isCompleted = true,
//                createdAt = System.currentTimeMillis() - 18000000 // 5 hours ago
//            ),
//            Task(
//                id = 7,
//                title = "Setup Development Environment",
//                description = "Configure new development machine",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 14400000 // 4 hours ago
//            ),
//            Task(
//                id = 8,
//                title = "Update Dependencies",
//                description = "Upgrade third-party libraries to latest versions",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 10800000 // 3 hours ago
//            ),
//            Task(
//                id = 9,
//                title = "Write Unit Tests",
//                description = "Add tests for new features",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 7200000 // 2 hours ago
//            ),
//            Task(
//                id = 10,
//                title = "Optimize Database Queries",
//                description = "Improve performance of slow queries",
//                isCompleted = true,
//                createdAt = System.currentTimeMillis() - 3600000 // 1 hour ago
//            ),
//            Task(
//                id = 11,
//                title = "Design UI Mockups",
//                description = "Create wireframes for new features",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 1800000 // 30 mins ago
//            ),
//            Task(
//                id = 12,
//                title = "Setup CI/CD Pipeline",
//                description = "Configure automated deployment workflow",
//                isCompleted = true,
//                createdAt = System.currentTimeMillis() - 900000 // 15 mins ago
//            ),
//            Task(
//                id = 13,
//                title = "Implement User Feedback",
//                description = "Address user suggestions from latest survey",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 600000 // 10 mins ago
//            ),
//            Task(
//                id = 14,
//                title = "Security Audit",
//                description = "Review and fix security vulnerabilities",
//                isCompleted = false,
//                createdAt = System.currentTimeMillis() - 300000 // 5 mins ago
//            ),
//            Task(
//                id = 15,
//                title = "Backup Database",
//                description = "Create backup of production database",
//                isCompleted = true,
//                createdAt = System.currentTimeMillis() - 60000 // 1 min ago
//            )
//        )
//    }
//}