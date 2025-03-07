package com.example.todo_list_v2.presentation.screen

import LoadingOverlay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todo_list_v2.presentation.navigation.AppTopBar
import com.example.todo_list_v2.presentation.util.AddEditTaskEvent
import com.example.todo_list_v2.presentation.view_model.AddEditTaskViewModel
import com.example.todo_list_v2.presentation.view_model.TaskEvent
import com.example.todo_list_v2.presentation.view_model.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    taskId: Long? = null,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    val title = viewModel.title.value
    val content = viewModel.content.value
    val startTime = viewModel.startTime.value
    val endTime = viewModel.endTime.value
    val favorite = viewModel.favorite.value
    val context = LocalContext.current

    // State cho Date/Time Picker
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startTime)
    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().get(Calendar.MINUTE)
    )
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isStartTime by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var taskViewMode: TaskViewModel = hiltViewModel()

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTaskById(taskId)
        }
    }
    Scaffold(
        topBar = {
            AppTopBar(
                title = if (taskId == null) "Add Task" else "Edit Task",
                onBackClick = {
                    viewModel.onEvent(AddEditTaskEvent.cancelTask)
                    navController.popBackStack()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary,
                content = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            )
        }
    ) { innerPadding ->
        LoadingOverlay(isLoading = isLoading)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Phần chọn thời gian
            TimeSelectionSection(
                startTime = startTime,
                endTime = endTime,
                onStartTimeClick = {
                    showDatePicker = true
                    isStartTime = true
                },
                onEndTimeClick = {
                    showDatePicker = true
                    isStartTime = false
                }
            )

            // Phần nhập thông tin task
            TaskInputSection(
                taskTitle = title,
                taskContent = content,
                onTaskTitleChange = { viewModel.onEvent(AddEditTaskEvent.EnteredTitle(it)) },
                onTaskContentChange = { viewModel.onEvent(AddEditTaskEvent.EnteredContent(it)) },
                onCancel = {
                    viewModel.onEvent(AddEditTaskEvent.cancelTask)
                    navController.popBackStack()
                },
                onSave = {
                    coroutineScope.launch {
                        isLoading = true
                        delay(2000)
                        viewModel.onEvent(AddEditTaskEvent.saveTask)
                        isLoading = false
                        navController.popBackStack()
                    }
                },
                isFavorite = favorite,
                onFavorite = { viewModel.onEvent(AddEditTaskEvent.ToggleFavorite) },
                onDelete = {
                    taskId?.let {
                        viewModel.deleteTaskById(it)
                        navController.popBackStack()
                    }
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Task deleted",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            taskViewMode.onEvent(TaskEvent.RestoreTask)
                        }
                    }
//                    taskViewMode.onEvent(TaskEvent.UpdateTask(task))
                },
            )

            // Date Picker Dialog
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            showTimePicker = true
                        }) { Text("Next") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // Time Picker Dialog
            if (showTimePicker) {
                DatePickerDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis =
                                    datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                set(Calendar.MINUTE, timePickerState.minute)
                            }
                            val selectedTime = calendar.timeInMillis
                            if (isStartTime) {
                                viewModel.onEvent(AddEditTaskEvent.SetStartTime(selectedTime))
                            } else {
                                viewModel.onEvent(AddEditTaskEvent.SetEndTime(selectedTime))
                            }
                            showTimePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
                    }
                ) {
                    TimePicker(state = timePickerState)
                }
            }

        }
    }
}

@Composable
fun TimeSelectionSection(
    startTime: Long,
    endTime: Long,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Task Timing",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Start: ${
                        SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault()
                        ).format(startTime)
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = onStartTimeClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Start Time",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "End: ${
                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                            endTime
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = onEndTimeClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit End Time",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun TaskInputSection(
    taskTitle: String,
    taskContent: String,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
    onTaskTitleChange: (String) -> Unit,
    onTaskContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Row() {
                    IconButton(onClick = { onFavorite() }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            tint = Color.Red
                        )
                    }
                }
            }
            OutlinedTextField(
                value = taskTitle,
                onValueChange = onTaskTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Task Title") },
                placeholder = { Text("Enter title") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = taskContent,
                onValueChange = onTaskContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = { Text("Task Content") },
                placeholder = { Text("Enter content") },
                maxLines = 5,
                shape = RoundedCornerShape(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSave,
                    enabled = taskTitle.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Save", color = Color.White)
                }
            }
        }
    }
}