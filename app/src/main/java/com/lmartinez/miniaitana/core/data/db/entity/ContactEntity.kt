package com.lmartinez.miniaitana.core.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    indices = [Index(value = ["normalizedName"], unique = true)]
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val normalizedName: String,
    val displayName: String,
    val isWhitelisted: Boolean = false,
)
