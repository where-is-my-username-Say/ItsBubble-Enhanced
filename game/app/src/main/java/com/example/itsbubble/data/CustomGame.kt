package com.example.itsbubble.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_games")
data class CustomGame(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val displayName: String,
    val packageName: String,
    val iconUri: String?,
    val mapType: MapType,
    val mapSource: String,
    val createdAt: Long = System.currentTimeMillis()
)
