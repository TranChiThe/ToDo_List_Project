package com.example.todo_list_v2.domain.use_cases

import com.example.todo_list_v2.domain.repositories.TaskRepository

class GetFavoriteTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(isFavorite: Boolean) = taskRepository.getFavoriteTask(isFavorite)

}