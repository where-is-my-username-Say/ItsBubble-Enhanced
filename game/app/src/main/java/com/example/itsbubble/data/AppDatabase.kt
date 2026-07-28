package com.example.itsbubble.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class Converters {
    @TypeConverter
    fun fromMapType(value: MapType): String = value.name

    @TypeConverter
    fun toMapType(value: String): MapType = MapType.valueOf(value)
}

@Database(entities = [CustomGame::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customGameDao(): CustomGameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "itsbubble_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
