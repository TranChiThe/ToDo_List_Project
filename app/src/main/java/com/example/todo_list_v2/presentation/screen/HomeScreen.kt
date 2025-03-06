package com.example.todo_list_v2.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.presentation.task.TaskItem
import com.example.todo_list_v2.presentation.util.Screen
import com.example.todo_list_v2.presentation.view_model.AddEditTaskViewModel
import com.example.todo_list_v2.presentation.view_model.TaskEvent
import com.example.todo_list_v2.presentation.view_model.TaskViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val tasks by taskViewModel.taskFlow.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddButton { navController.navigate(Screen.AddTask.route) }
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(data)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Task List",
                fontSize = 30.sp,
                fontWeight = FontWeight(300)
            )
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Add,
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
            } else {
                val groupedTasks = groupTasksByDate(tasks)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    val orderedKeys = listOf("Today", "Yesterday", "One week ago", "Older")
                    orderedKeys.forEach { key ->
                        groupedTasks[key]?.let { taskList ->
                            item {
                                Text(
                                    text = key,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            items(taskList) { task ->
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
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun groupTasksByDate(tasks: List<Task>): Map<String, List<Task>> {
    val calendar = Calendar.getInstance()
    val today = calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val yesterday = calendar.apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }.timeInMillis

    val oneWeekAgo = calendar.apply {
        add(Calendar.DAY_OF_YEAR, -6)
    }.timeInMillis

    val grouped = tasks.groupBy { task ->
        val taskDate = Calendar.getInstance().apply {
            timeInMillis = task.createdAt
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        when {
            taskDate == today -> "Today"
            taskDate == yesterday -> "Yesterday"
            taskDate >= oneWeekAgo -> "One week ago"
            else -> "Older"
        }
    }

    return grouped.mapValues { entry ->
        entry.value.sortedByDescending { it.createdAt }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        modifier = Modifier.padding(bottom = 10.dp, start = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.Black,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun CustomSnackbar(data: SnackbarData) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        androidx.compose.material3.Snackbar(
            action = {
                Row {
                    TextButton(onClick = { data.performAction() }) {
                        Text("Undo", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { data.dismiss() }) {
                        Text("Cancel", color = Color.White)
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(data.visuals.message)
        }
    }
}