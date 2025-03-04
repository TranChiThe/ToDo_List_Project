package com.example.todo_list_v2.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_list_v2.domain.model.Task
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
    taskViewModel: TaskViewModel = hiltViewModel()
) {
//    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    var tasks = taskViewModel.taskState.value
    var showInputField by rememberSaveable { mutableStateOf(false) }
    var newTaskTitle by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    LaunchedEffect(Unit) {
//        viewModel.loadTasks()
//    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            AddButton { showInputField = true }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        LaunchedEffect(showInputField) {
            if (showInputField) {
                withContext(Dispatchers.Main) {
                    delay(100)
                    focusRequester.requestFocus()
                }
            }
        }
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


@Composable
fun TaskInputField(
    taskTitle: String,
    onTaskTitleChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(5.dp))
            .shadow(4.dp, RoundedCornerShape(5.dp))
            .padding(8.dp)
    ) {
        TextField(
            value = taskTitle,
            onValueChange = onTaskTitleChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .border(
                    width = 1.dp,
                    color = if (taskTitle.isEmpty()) Color.Gray else MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            label = { Text("Enter new task...", style = MaterialTheme.typography.bodyMedium) },
            placeholder = { Text("Type your task here", color = Color.LightGray) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (taskTitle.isNotEmpty()) {
                    IconButton(onClick = { onTaskTitleChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text(
                    "Cancel",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = taskTitle.isNotBlank()
            ) {
                Text("Save", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}



