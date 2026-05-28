package com.lmartinez.miniaitana.core.data.db.mapper

import com.lmartinez.miniaitana.core.data.db.entity.ContactEntity
import com.lmartinez.miniaitana.core.domain.model.Contact

fun ContactEntity.toDomain(): Contact = Contact(
    id             = id,
    normalizedName = normalizedName,
    displayName    = displayName,
    isWhitelisted  = isWhitelisted,
)

fun Contact.toEntity(): ContactEntity = ContactEntity(
    id             = id,
    normalizedName = normalizedName,
    displayName    = displayName,
    isWhitelisted  = isWhitelisted,
)
