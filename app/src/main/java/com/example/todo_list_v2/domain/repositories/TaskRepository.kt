package com.example.todo_list_v2.domain.repositories

import com.example.todo_list_v2.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTask(): Flow<List<Task>>
    suspend fun getTaskById(taskId: Long): Task?
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun searchTasks(query: String): List<Task>
    suspend fun deleteTaskById(taskId: Long)
}