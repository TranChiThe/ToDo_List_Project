package com.example.todo_list_v2.domain.use_cases

import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.repositories.TaskRepository

class GetTaskById(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): Task? {
        return taskRepository.getTaskById(taskId)
    }
}