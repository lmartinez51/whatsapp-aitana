package com.lmartinez.miniaitana.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lmartinez.miniaitana.core.data.db.entity.PromptTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromptTemplateDao {

    @Query("SELECT * FROM prompt_templates ORDER BY title COLLATE NOCASE ASC")
    fun getAllTemplates(): Flow<List<PromptTemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(template: PromptTemplateEntity): Long

    @Update
    suspend fun update(template: PromptTemplateEntity)

    @Query("DELETE FROM prompt_templates WHERE id = :id")
    suspend fun deleteById(id: Long)
}
