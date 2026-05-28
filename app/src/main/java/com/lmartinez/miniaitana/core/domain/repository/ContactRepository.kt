package com.lmartinez.miniaitana.core.domain.repository

import com.lmartinez.miniaitana.core.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<Contact>>
    fun getWhitelistedContacts(): Flow<List<Contact>>
    suspend fun getContactByNormalizedName(normalizedName: String): Contact?
    suspend fun upsertContact(contact: Contact)
    suspend fun deleteContact(contact: Contact)
    suspend fun setWhitelisted(normalizedName: String, isWhitelisted: Boolean)
}
