package com.example.todo_list_v2.domain.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Task(
    @Id var id: Long = 0,
    var title: String,
    var status: String,
    var createdAt: Long = System.currentTimeMillis(),
    var updateAt: Long = System.currentTimeMillis()
)