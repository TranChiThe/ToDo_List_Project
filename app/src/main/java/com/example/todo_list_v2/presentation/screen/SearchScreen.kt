package com.example.todo_list_v2.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todo_list_v2.presentation.task.TaskItem
import com.example.todo_list_v2.presentation.util.AppScaffold
import com.example.todo_list_v2.presentation.util.Screen
import com.example.todo_list_v2.presentation.view_model.SearchTaskViewModel
import com.example.todo_list_v2.presentation.view_model.TaskEvent
import com.example.todo_list_v2.presentation.view_model.TaskViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    searchTaskViewModel: SearchTaskViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var text by remember { mutableStateOf("") }
    val searchResults = searchTaskViewModel.searchResults.value
    val taskViewModel: TaskViewModel = hiltViewModel()

    AppScaffold(
        navController = navController,
        showFab = true // Hiển thị FAB
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    searchTaskViewModel.searchTasks(it)
                },
                label = { Text("Enter task title ...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (text.isNotEmpty()) {
                if (searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { task ->
                            TaskItem(
                                task = task,
                                modifier = Modifier,
                                onClick = {
                                    navController.navigate(Screen.EditTask.createRoute(task.id))
                                },
                                onFavorite = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                },
                                onCheckBox = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tasks found",
                            fontSize = 20.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No task",
                            fontSize = 20.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}