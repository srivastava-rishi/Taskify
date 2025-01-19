package com.rsstudio.taskify.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rsstudio.taskify.data.local.dao.ReminderDao
import com.rsstudio.taskify.data.local.entity.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}