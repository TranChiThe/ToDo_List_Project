package com.example.todo_list_v2.presentation.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.use_cases.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    private val _title = mutableStateOf("")
    val title: State<String> = _title

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun addTask(title: String, status: String, createdAt: Long) {
        viewModelScope.launch {
            taskUseCases.addTask(
                Task(
                    title = title,
                    status = status,
                    createdAt = createdAt
                )
            )
            _title.value = ""
        }
    }
}