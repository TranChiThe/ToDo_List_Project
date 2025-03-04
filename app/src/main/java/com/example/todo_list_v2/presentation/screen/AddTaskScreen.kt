package com.example.todo_list_v2.presentation.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_list_v2.presentation.view_model.AddTaskViewModel
import androidx.compose.material3.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    addTaskViewModel: AddTaskViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val title = addTaskViewModel.title.value
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Title is empty", Toast.LENGTH_SHORT).show()
                    }
                    addTaskViewModel.addTask(
                        title = title,
                        status = "",
                        createdAt = System.currentTimeMillis()
                    )
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { newValue -> addTaskViewModel.onTitleChange(newValue) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Tile")
                }
            )
        }
    }
}