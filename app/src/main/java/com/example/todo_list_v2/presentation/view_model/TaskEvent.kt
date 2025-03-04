package com.example.todo_list_v2.presentation.view_model

import com.example.todo_list_v2.domain.model.Task

sealed class TaskEvent {
    data class DeleteTask(val task: Task) : TaskEvent()
}