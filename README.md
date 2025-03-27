# Task Manager Android App

A modern Android task management application with local persistence, Firebase integration, 
and real-time synchronization capabilities.

![App Screenshot](Taskmanager/blob/main/appscreen1.jpg)

## Features

- **Task Management**
  - Create, edit, delete tasks
  - Mark tasks as completed
  - Real-time updates with Room Database
- **Synchronization**
  - Manual sync with mock server
  - Performance monitoring for sync operations
- **Analytics & Monitoring**
  - Firebase Analytics for user actions
  - Crashlytics integration for error tracking
  - Network performance tracing
- **Debug Tools**
  - Crash simulation
  - Database error testing

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 34 (Android 14)
- Java 17 JDK

### Installation
1. **Clone Repository**
   ```bash
   git clone https://github.com/yourusername/task-manager-android.git

## Third-Party Libraries
Library	Version	Purpose
- Room	2.6.1	Local database persistence
- Firebase	32.7.0	Analytics, Crash Reporting, Performance Monitoring
- Hilt	2.48	Dependency Injection
- Kotlin Coroutines	1.7.3	Asynchronous operations
- Jetpack Compose	1.5.4	Modern UI implementation
- Material3	1.1.2	Theming and UI components

##Architecture & Design Decisions
1. Layered Architecture

UI Layer (Composable) → ViewModel → Repository → Data Sources (Room, Firebase)
Why: Separation of concerns, testability, and maintainability

2. Reactive Data Flow
   
Uses Kotlin Flow for real-time database updates
val tasks: Flow<List<Task>> = taskDao.getAllTasks().map { entities → ... }
Benefit: Automatic UI updates on data changes

4. Offline-First Approach
   
Room database as single source of truth
Manual sync button for mock server updates
suspend fun fetchRemoteTasks() {
    // Clear local data before mock sync
    taskDao.clearAllTasks()
    remoteTasks.forEach { taskDao.insertTask(it.toEntity()) }
}
5. Error Handling Strategy
   
Centralized error reporting via Crashlytics
.catch { e →
    crashlytics.log("Database error")
    crashlytics.recordException(e)
    throw e
}
Custom exception hierarchy for domain errors

7. Performance Optimization
   
Firebase Performance Monitoring integration
val trace = Firebase.performance.newTrace("fetch_tasks")
trace.putMetric("tasks_received", remoteTasks.size.toLong())
Asynchronous operations with Coroutines

8. Modern UI Patterns
Jetpack Compose for declarative UI

Material3 theming system

State hoisting for testable components

Testing Features

Simulated Network Calls

Mock API service with configurable delay:

delay(Random.nextLong(500, 1500)) // Simulate network latency

Debug Tools

Forced crash testing
fun triggerTestCrash() {
    throw RuntimeException("Test crash")
}

Database constraint violation test
suspend fun triggerDatabaseCrash() {
    taskDao.insertTask(entity)
    taskDao.insertTask(entity) // Force duplicate ID
}

Firebase Integration Details
Tracked Events
Event Name	Parameters
task_added	task_id, title
task_edited	task_id, title
task_completed	task_id, title
task_deleted	task_id, task_title
sync_attempted	-
Performance Metrics
Trace Name: fetch_tasks

Metrics:

tasks_received (count)

duration (ms)

Attributes:

status (success/failed)

error_type (if failed)
