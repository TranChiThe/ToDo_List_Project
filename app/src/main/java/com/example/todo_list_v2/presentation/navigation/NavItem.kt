package com.example.todo_list_v2.presentation.navigation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val lable: String,
    val icon: ImageVector,
    val selectedColor: Color,
    val unselectedColor: Color
)