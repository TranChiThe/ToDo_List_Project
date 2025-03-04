package com.example.todo_list_v2.data.dao

import com.example.todo_list_v2.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}