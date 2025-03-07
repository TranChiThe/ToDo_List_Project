package com.example.todo_list_v2.di

import android.content.Context
import com.example.todo_list_v2.data.repositories.TaskRepositoryImpl
import com.example.todo_list_v2.domain.model.MyObjectBox
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.repositories.TaskRepository
import com.example.todo_list_v2.domain.use_cases.AddTask
import com.example.todo_list_v2.domain.use_cases.DeleteTask
import com.example.todo_list_v2.domain.use_cases.DeleteTaskById
import com.example.todo_list_v2.domain.use_cases.GetAllTask
import com.example.todo_list_v2.domain.use_cases.GetTaskById
import com.example.todo_list_v2.domain.use_cases.GetFavoriteTask
import com.example.todo_list_v2.domain.use_cases.SearchTask
import com.example.todo_list_v2.domain.use_cases.TaskUseCases
import com.example.todo_list_v2.domain.use_cases.UpdateTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        return MyObjectBox.builder()
            .androidContext(context)
            .build()
    }

    @Provides
    fun provideTaskBox(boxStore: BoxStore): Box<Task> {
        return boxStore.boxFor(Task::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskBox: Box<Task>): TaskRepository {
        return TaskRepositoryImpl(taskBox)
    }

    @Provides
    @Singleton
    fun providerTaskUseCases(taskRepository: TaskRepository): TaskUseCases = TaskUseCases(
        getAllTask = GetAllTask(taskRepository),
        addTask = AddTask(taskRepository),
        updateTask = UpdateTask(taskRepository),
        deleteTask = DeleteTask(taskRepository),
        getTaskById = GetTaskById(taskRepository),
        deleteTaskById = DeleteTaskById(taskRepository),
        searchTask = SearchTask(taskRepository),
        getFavoriteTask = GetFavoriteTask(taskRepository)
    )
}