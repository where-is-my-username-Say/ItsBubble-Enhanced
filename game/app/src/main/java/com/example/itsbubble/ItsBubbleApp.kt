package com.example.itsbubble

import android.app.Application
import com.example.itsbubble.data.AppDatabase

class ItsBubbleApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
