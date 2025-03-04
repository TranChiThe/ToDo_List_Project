package com.example.todo_list_v2.presentation.navigation

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo_list_v2.presentation.screen.AddTaskScreen
import com.example.todo_list_v2.presentation.screen.CalendarScreen
import com.example.todo_list_v2.presentation.screen.FavoriteScreen
import com.example.todo_list_v2.presentation.screen.HomeScreen
import com.example.todo_list_v2.presentation.screen.SearchScreen
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
            composable("home") { HomeScreen(navController = navController) }
            composable("search") { SearchScreen(navController = navController) }
            composable("calendar") { CalendarScreen(navController = navController) }
            composable("favorite") { FavoriteScreen(navController = navController) }
            composable("addTask") { AddTaskScreen(navController = navController) }

        }

    }
}

