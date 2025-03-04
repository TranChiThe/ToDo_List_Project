package com.example.todo_list_v2.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_list_v2.domain.model.Task
import com.example.todo_list_v2.presentation.view_model.TaskViewModel

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier,
    onDelete: () -> Unit
) {
    var isChecked by remember { mutableStateOf(task.status == "Done") }
    val maxLength = 18
    val displayTitle = if (task.title.length > maxLength) {
        "${task.title.substring(0, maxLength)} ..."
    } else {
        task.title
    }
    Row(
        modifier = Modifier
            .padding(10.dp)
            .height(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 1000.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp))
                .background(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(20.dp)),
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .height(80.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Checkbox(checked = isChecked, onCheckedChange = {
                    isChecked = it
//                    viewModel.updateTask(task.copy(status = if (it) "Done" else "Pending"))
                })
                Text(
                    text = displayTitle,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                if (task.status == "Done") {
                    Icon(
                        tint = Color.Blue,
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                } else {
                    Icon(
                        tint = Color.Red,
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                }
                DeleteButton(
                    deleteTask = {
                        onDelete()
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteButton(deleteTask: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        IconButton(
            onClick = deleteTask,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task",
                tint = Color.Red
            )
        }
    }
}