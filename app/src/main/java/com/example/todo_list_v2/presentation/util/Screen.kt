package com.example.todo_list_v2.presentation.util

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Calendar : Screen("calendar")
    object Favorite : Screen("favorite")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Long) = "edit_task/$taskId"
    }
}