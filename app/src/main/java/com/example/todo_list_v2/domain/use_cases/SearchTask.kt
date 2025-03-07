package com.example.todo_list_v2.domain.use_cases

import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.domain.repositories.TaskRepository

class SearchTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(searchTerm: String) = taskRepository.searchTasks(searchTerm)

}