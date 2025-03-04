package com.example.todo_list_v2.presentation.view_model

import androidx.lifecycle.ViewModel
import com.example.todo_list_v2.domain.use_cases.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

}