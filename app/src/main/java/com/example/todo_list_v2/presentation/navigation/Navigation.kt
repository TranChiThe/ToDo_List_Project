package com.example.todo_list_v2.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v2.presentation.screen.CalendarScreen
import com.example.todo_list_v2.presentation.screen.FavoriteScreen
import com.example.todo_list_v2.presentation.screen.HomeScreen
import com.example.todo_list_v2.presentation.screen.SearchScreen
import com.example.todo_list_v2.presentation.view_model.TaskViewModel

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val NavItemList = listOf(
        NavItem("Home", Icons.Default.Home, Color(0xFF1E88E5), Color(0xFF9E9E9E)),
        NavItem("Search", Icons.Default.Search, Color(0xFF1E88E5), Color(0xFF9E9E9E)),
        NavItem("Calendar", Icons.Default.DateRange, Color(0xFF1E88E5), Color(0xFF9E9E9E)),
        NavItem("Favorite", Icons.Default.Favorite, Color(0xFFFF0000), Color(0xFF9E9E9E))
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val viewModel: TaskViewModel = hiltViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            BadgedBox(badge = {
//                                Badge() {
//                                    Text(text = "2")
//                                }
                            }) {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = "",
                                    tint = if (selectedIndex == index) navItem.selectedColor else navItem.unselectedColor
                                )
                            }
                        },
                        label = { Text(text = navItem.lable) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0066FF),
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex,
        )
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    Box(modifier = modifier) {
        when (selectedIndex) {
            0 -> HomeScreen()
            1 -> SearchScreen()
            2 -> CalendarScreen()
            3 -> FavoriteScreen()
        }
    }
}
