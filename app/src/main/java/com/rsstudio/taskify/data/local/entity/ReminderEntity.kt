package com.rsstudio.taskify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val timeStamp: Long,
    val reminderInterval: String,
    val fromApi: Boolean = false
)