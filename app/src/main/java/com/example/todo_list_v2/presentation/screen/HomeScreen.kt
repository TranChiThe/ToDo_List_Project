package com.example.todo_list_v2.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.todo_list_v2.presentation.task.TaskItem
import com.example.todo_list_v2.presentation.view_model.TaskEvent
import com.example.todo_list_v2.presentation.view_model.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var tasks = taskViewModel.taskState.value
    var showInputField by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            AddButton { navController.navigate("addTask") }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Task List",
                fontSize = 30.sp
            )
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            modifier = Modifier,
                            onDelete = {
                                taskViewModel.onEvent(TaskEvent.DeleteTask(task))
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(bottom = 10.dp, start = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.Black,
            modifier = Modifier.size(30.dp)
        )
    }
}






