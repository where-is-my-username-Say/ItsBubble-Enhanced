package com.example.itsbubble.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomGameDao {
    @Query("SELECT * FROM custom_games ORDER BY displayName ASC")
    fun getAll(): Flow<List<CustomGame>>

    @Query("SELECT * FROM custom_games WHERE packageName = :pkg LIMIT 1")
    suspend fun findByPackage(pkg: String): CustomGame?

    @Query("SELECT * FROM custom_games WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): CustomGame?

    @Insert
    suspend fun insert(game: CustomGame): Long

    @Update
    suspend fun update(game: CustomGame)

    @Delete
    suspend fun delete(game: CustomGame)

    @Query("SELECT COUNT(*) FROM custom_games WHERE packageName = :pkg")
    suspend fun countByPackage(pkg: String): Int
}
