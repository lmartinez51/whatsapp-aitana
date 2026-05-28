package com.lmartinez.miniaitana.core.domain.usecase

import com.lmartinez.miniaitana.core.common.AutoPilotReplySanitizer
import com.lmartinez.miniaitana.core.domain.model.Contact
import com.lmartinez.miniaitana.core.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WhitelistUseCase @Inject constructor(
    private val contactRepository: ContactRepository
) {
    fun getContacts(): Flow<List<Contact>> = contactRepository.getAllContacts()

    suspend fun addContact(displayName: String) {
        val normalized = AutoPilotReplySanitizer.normalizeContactName(displayName)
        if (normalized.isEmpty()) return
        
        val contact = Contact(
            normalizedName = normalized,
            displayName = displayName,
            isWhitelisted = true
        )
        contactRepository.upsertContact(contact)
    }

    suspend fun removeContact(contact: Contact) {
        contactRepository.deleteContact(contact)
    }
}
