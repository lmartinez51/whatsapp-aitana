package com.lmartinez.miniaitana.core.data.db.dao

import androidx.room.*
import com.lmartinez.miniaitana.core.data.db.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY displayName ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE isWhitelisted = 1 ORDER BY displayName ASC")
    fun getWhitelistedContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE normalizedName = :normalizedName LIMIT 1")
    suspend fun getByNormalizedName(normalizedName: String): ContactEntity?

    @Upsert
    suspend fun upsert(contact: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)

    @Query("UPDATE contacts SET isWhitelisted = :isWhitelisted WHERE normalizedName = :normalizedName")
    suspend fun setWhitelisted(normalizedName: String, isWhitelisted: Boolean)
}
