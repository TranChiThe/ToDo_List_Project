import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.Display.Mode
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todo_list_v2.presentation.view_model.TaskViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.style.TextAlign
import com.example.todo_list_v2.presentation.task.TaskItem
import com.example.todo_list_v2.presentation.util.Screen
import com.example.todo_list_v2.presentation.view_model.TaskEvent
import java.time.Instant
import java.time.Instant.ofEpochMilli
import java.time.LocalDate
import java.time.ZoneId


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    taskViewModel: TaskViewModel = hiltViewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        yearRange = 2000..2030
    )
    val tasks by taskViewModel.taskFlow.collectAsState(initial = emptyList())
    val selectedDate = datePickerState.selectedDateMillis?.toLocalDate()
    val tasksForSelectedDay = if (selectedDate != null) {
        tasks.filter { task ->
            task.startTime.toLocalDate() == selectedDate
        }
    } else {
        emptyList()
    }
    LaunchedEffect(Unit) {
        taskViewModel.loadTasks()
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Task Favorite",
                fontSize = 30.sp,
                fontWeight = FontWeight(300)
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
        ) {
            DatePicker(
                state = datePickerState,
                title = { Text(text = "") },
                showModeToggle = true
            )
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Column(
            modifier = Modifier
                .padding(top = 510.dp),
        )
        {
            if (selectedDate != null) {
                if (tasksForSelectedDay.isEmpty()) {
                    Text(
                        text = "There are no missions for this day.",
                        textAlign = TextAlign.Center,
                    )
                } else {
                    LazyColumn {
                        items(tasksForSelectedDay) { task ->
                            TaskItem(
                                task = task,
                                onFavorite = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                },
                                onCheckBox = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                },
                                onClick = {
                                    navController.navigate(Screen.EditTask.createRoute(task.id))
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

@SuppressLint("NewApi")
fun Long.toLocalDate(): LocalDate {
    return ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
