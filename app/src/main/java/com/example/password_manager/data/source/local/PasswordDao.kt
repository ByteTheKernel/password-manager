package com.example.password_manager.data.source.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.password_manager.data.model.PasswordEntity
import com.example.password_manager.data.model.PasswordPreviewEntity

@Dao
interface PasswordDao {

    @Query("SELECT id, title, url FROM passwords WHERE title LIKE '%' || :query || '%' ORDER BY id DESC")
    fun pagingSource(query: String): PagingSource<Int, PasswordPreviewEntity>

    @Query("SELECT id, title, url FROM passwords ORDER BY id DESC")
    fun getAllPreviews(): Flow<List<PasswordPreviewEntity>>

    @Query("SELECT id, title, url FROM passwords WHERE title LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchPreviewsByTitle(query: String): Flow<List<PasswordPreviewEntity>>

    @Query("SELECT * FROM passwords WHERE id = :id")
    suspend fun getById(id: Long): PasswordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordEntity)

    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun deleteById(id: Long)
}