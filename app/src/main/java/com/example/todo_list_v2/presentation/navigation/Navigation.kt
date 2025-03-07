package com.example.todo_list_v2.presentation.navigation

import CalendarScreen
import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_list_v2.presentation.screen.AddTaskScreen
import com.example.todo_list_v2.presentation.screen.FavoriteScreen
import com.example.todo_list_v2.presentation.screen.HomeScreen
import com.example.todo_list_v2.presentation.screen.SearchScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            startDestination = "home",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }, // Slide từ phải sang trái
                    animationSpec = tween(durationMillis = 300) // Thời gian animation: 300ms
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }, // Slide ra bên trái
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth }, // Slide từ trái sang phải khi quay lại
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }, // Slide ra bên phải khi quay lại
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.Search.route) { SearchScreen(navController = navController) }
            composable(Screen.Calendar.route) { CalendarScreen(navController = navController) }
            composable(Screen.Favorite.route) { FavoriteScreen(navController = navController) }
            composable(Screen.AddTask.route) {
                AddTaskScreen(navController = navController, taskId = null)
            }
            composable(
                route = Screen.EditTask.route,
                arguments = listOf(navArgument("taskId") { type = NavType.LongType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getLong("taskId")
                AddTaskScreen(navController = navController, taskId = taskId) // Chỉnh sửa
            }
        }
    }
}



