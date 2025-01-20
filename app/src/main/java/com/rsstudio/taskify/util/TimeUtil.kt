package com.rsstudio.taskify.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {
    fun formatTimeInMillisTo12HourFormat(timeMillis: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = Date(timeMillis)
        return sdf.format(date)
    }

    fun getTimeStamp() = System.currentTimeMillis()
}