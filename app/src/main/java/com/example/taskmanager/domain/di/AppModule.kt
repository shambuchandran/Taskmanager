package com.example.taskmanager.domain.di

import android.content.Context
import androidx.room.Room
import com.example.taskmanager.data.api.MockTaskApiService
import com.example.taskmanager.data.room.AppDatabase
import com.example.taskmanager.data.room.TaskDao
import com.example.taskmanager.domain.repository.TaskRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "task_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
    @Provides
    @Singleton
    fun provideCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskApiService(): MockTaskApiService {
        return MockTaskApiService()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao,
        taskApiService: MockTaskApiService,
        analytics: FirebaseAnalytics,
        crashlytics: FirebaseCrashlytics
    ): TaskRepository {
        return TaskRepository(taskDao, taskApiService, analytics,crashlytics)
    }
}