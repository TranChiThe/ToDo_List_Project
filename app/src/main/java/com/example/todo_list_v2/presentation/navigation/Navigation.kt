package com.example.todo_list_v2.presentation.navigation

import CalendarScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_list_v2.presentation.screen.AddTaskScreen
import com.example.todo_list_v2.presentation.screen.FavoriteScreen
import com.example.todo_list_v2.presentation.screen.HomeScreen
import com.example.todo_list_v2.presentation.screen.SearchScreen
import com.example.todo_list_v2.presentation.util.Screen
import com.example.todo_list_v2.presentation.view_model.TaskViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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

