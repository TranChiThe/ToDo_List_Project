package com.example.todo_list_v2.data.repositories

import com.example.todo_list_v2.data.dao.TaskDao
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.repositories.TaskRepository
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskBox: Box<Task>) : TaskRepository {

    private val _taskFlow = MutableStateFlow<List<Task>>(emptyList())
    private val taskFlow = _taskFlow.asStateFlow()

    init {
        refreshTask()
    }

    fun refreshTask() {
        _taskFlow.value = taskBox.all
    }

    override fun getAllTask(): Flow<List<Task>> = taskFlow


    override suspend fun addTask(task: Task) {
        taskBox.put(task)
        refreshTask()
    }

    override suspend fun updateTask(task: Task) {
        taskBox.put(task)
        refreshTask()
    }

    override suspend fun deleteTask(task: Task) {
        taskBox.remove(task)
    }


}

