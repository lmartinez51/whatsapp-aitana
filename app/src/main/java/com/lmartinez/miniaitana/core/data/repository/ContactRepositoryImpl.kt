package com.lmartinez.miniaitana.core.data.repository

import com.lmartinez.miniaitana.core.data.db.dao.ContactDao
import com.lmartinez.miniaitana.core.data.db.mapper.toDomain
import com.lmartinez.miniaitana.core.data.db.mapper.toEntity
import com.lmartinez.miniaitana.core.domain.model.Contact
import com.lmartinez.miniaitana.core.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val dao: ContactDao,
) : ContactRepository {

    override fun getAllContacts(): Flow<List<Contact>> =
        dao.getAllContacts().map { list -> list.map { it.toDomain() } }

    override fun getWhitelistedContacts(): Flow<List<Contact>> =
        dao.getWhitelistedContacts().map { list -> list.map { it.toDomain() } }

    override suspend fun getContactByNormalizedName(normalizedName: String): Contact? =
        dao.getByNormalizedName(normalizedName)?.toDomain()

    override suspend fun upsertContact(contact: Contact) =
        dao.upsert(contact.toEntity())

    override suspend fun deleteContact(contact: Contact) =
        dao.delete(contact.toEntity())

    override suspend fun setWhitelisted(normalizedName: String, isWhitelisted: Boolean) =
        dao.setWhitelisted(normalizedName, isWhitelisted)
}
