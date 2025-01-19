package com.rsstudio.taskify.data.models.response

data class ReminderResponse(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
)