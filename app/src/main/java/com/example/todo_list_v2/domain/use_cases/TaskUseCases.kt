package com.example.todo_list_v2.domain.use_cases

data class TaskUseCases(
    val getAllTask: GetAllTask,
    val getTaskById: GetTaskById,
    val addTask: AddTask,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask,
    val searchTask: SearchTask,
    val deleteTaskById: DeleteTaskById,
    val getFavoriteTask: GetFavoriteTask
)
