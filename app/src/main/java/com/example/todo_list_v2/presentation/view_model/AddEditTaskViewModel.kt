package com.example.todo_list_v2.presentation.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.use_cases.TaskUseCases
import com.example.todo_list_v2.presentation.util.AddEditTaskEvent
import com.example.todo_list_v2.presentation.util.TaskEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    private val _title = mutableStateOf("")
    val title: State<String> = _title
    private val _content = mutableStateOf("")
    val content: State<String> = _content
    private val _startTime = mutableStateOf(System.currentTimeMillis())
    val startTime: State<Long> = _startTime
    private val _endTime = mutableStateOf(System.currentTimeMillis())
    val endTime: State<Long> = _endTime
    private val _taskId = mutableStateOf<Long?>(null)
    private val _favorite = mutableStateOf(false)
    val favorite: State<Boolean> = _favorite
    private var job: Job? = null

    fun onEvent(event: AddEditTaskEvent) {
        when (event) {
            is AddEditTaskEvent.EnteredTitle -> {
                _title.value = event.title
            }

            is AddEditTaskEvent.EnteredContent -> {
                _content.value = event.content
            }

            is AddEditTaskEvent.saveTask -> {
                viewModelScope.launch {
                    val task = Task(
                        id = _taskId.value ?: 0L,
                        title = title.value,
                        content = content.value,
                        status = "Pending",
                        favorite = favorite.value,
                        startTime = startTime.value,
                        endTime = endTime.value,
                    )
                    if (_taskId.value == null) {
                        taskUseCases.addTask(task)
                    } else {
                        taskUseCases.updateTask(task)
                    }
                    _title.value = ""
                    _content.value = ""
                    _startTime.value = System.currentTimeMillis()
                    _endTime.value = System.currentTimeMillis()
                    _taskId.value = null
                }
            }

            is AddEditTaskEvent.cancelTask -> {
                _title.value = ""
                _content.value = ""
                _startTime.value = System.currentTimeMillis()
                _endTime.value = System.currentTimeMillis()
                _taskId.value = null
            }

            is AddEditTaskEvent.ToggleFavorite -> {
                _favorite.value = !_favorite.value
            }

            is AddEditTaskEvent.SetStartTime -> _startTime.value = event.time
            is AddEditTaskEvent.SetEndTime -> _endTime.value = event.time
        }
    }

    fun loadTaskById(id: Long) {
        viewModelScope.launch {
            val task = taskUseCases.getTaskById(id)
            task?.let {
                _taskId.value = it.id
                _title.value = it.title
                _content.value = it.content
                _favorite.value = it.favorite
                _startTime.value = it.startTime
                _endTime.value = it.endTime
            }
        }
    }

    fun deleteTaskById(id: Long) {
        viewModelScope.launch {
            taskUseCases.deleteTaskById(id)
            TaskEventBus.sendEvent()
        }
    }
}