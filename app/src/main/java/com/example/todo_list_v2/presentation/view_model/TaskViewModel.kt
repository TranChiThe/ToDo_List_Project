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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    private val _taskState = mutableStateOf(emptyList<Task>())
    val taskState: State<List<Task>> = _taskState
    private var job: Job? = null
    private var deleteTask: Task? = null

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    taskUseCases.deleteTask(event.task)
                    getAllTask()
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

    fun getAllTask() {
        job?.cancel()
        job = viewModelScope.launch {
            taskUseCases.getAllTask().collect { tasks ->
                _taskState.value = tasks
            }
        }
    }

}