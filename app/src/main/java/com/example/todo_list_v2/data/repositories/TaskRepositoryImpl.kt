package com.example.todo_list_v2.data.repositories

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.todo_list_v2.data.dao.TaskDao
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.model.Task_
import com.example.todo_list_v2.domain.model.Task_.*
import com.example.todo_list_v2.domain.repositories.TaskRepository
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.regex.Pattern.matches
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

    override fun getAllTask(): Flow<List<Task>> = callbackFlow {
        val query = taskBox.query().build()
        val subscription = query.subscribe().observer { tasks ->
            trySend(tasks)
        }
        awaitClose { subscription.cancel() }
    }

    override suspend fun getTaskById(taskId: Long): Task = withContext((Dispatchers.IO)) {
        taskBox.get(taskId)
    }


    override suspend fun addTask(task: Task) = withContext(Dispatchers.IO) {
        taskBox.put(task)
        refreshTask()
    }

    override suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        taskBox.put(task)
        refreshTask()
    }

    override suspend fun deleteTask(task: Task) = withContext(Dispatchers.IO) {
        taskBox.remove(task)
        refreshTask()

    }

    override suspend fun searchTasks(searchTerm: String): List<Task> = withContext(Dispatchers.IO) {
        taskBox.query()
            .contains(Task_.title, searchTerm, QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build()
            .find()
    }

    override suspend fun deleteTaskById(taskId: Long) {
        taskBox.remove(taskId)
    }

    override suspend fun getFavoriteTask(isFavorite: Boolean): Flow<List<Task>> = callbackFlow {
        val query = taskBox.query(favorite.equal(isFavorite)).build()
        val subscription = query.subscribe().observer { tasks ->
            trySend(tasks)
        }
        awaitClose { subscription.cancel() }
    }

}

