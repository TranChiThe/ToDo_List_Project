package com.example.todo_list_v2.presentation.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.repositories.TaskRepository
import com.example.todo_list_v2.domain.use_cases.TaskUseCases
import com.example.todo_list_v2.presentation.util.TaskEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    private val _taskFlow = MutableStateFlow<List<Task>>(emptyList())
    val taskFlow: StateFlow<List<Task>> = _taskFlow
    private var job: Job? = null
    private var deleteTask: Task? = null

    init {
        loadTasks()
        observeTaskEvents()
    }

    private fun observeTaskEvents() {
        viewModelScope.launch {
            TaskEventBus.eventFlow.collect {
                loadTasks()
            }
        }
    }

    fun loadTasks() {
        job?.cancel()
        job = viewModelScope.launch {
            taskUseCases.getAllTask().collect { tasks ->
                _taskFlow.value = tasks
            }
        }
    }

    fun getFavoriteTask() {
        job?.cancel()
        job = viewModelScope.launch {
            taskUseCases.getFavoriteTask().collect { tasks ->
                _taskFlow.value = tasks
            }
        }
    }


    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    taskUseCases.deleteTask(event.task)
                    loadTasks()

                }
                deleteTask = event.task
            }

            is TaskEvent.UpdateTask -> {
                viewModelScope.launch {
                    taskUseCases.updateTask(event.task)
                }
            }

            is TaskEvent.RestoreTask -> {
                viewModelScope.launch {
                    taskUseCases.addTask(deleteTask!!)
                }
            }
        }

    }
}

