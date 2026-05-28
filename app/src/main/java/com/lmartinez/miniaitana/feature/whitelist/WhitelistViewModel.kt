package com.lmartinez.miniaitana.feature.whitelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmartinez.miniaitana.core.domain.model.Contact
import com.lmartinez.miniaitana.core.domain.usecase.WhitelistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhitelistViewModel @Inject constructor(
    private val whitelistUseCase: WhitelistUseCase
) : ViewModel() {

    val contacts = whitelistUseCase.getContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addContact(name: String) {
        viewModelScope.launch {
            whitelistUseCase.addContact(name)
        }
    }

    fun removeContact(contact: Contact) {
        viewModelScope.launch {
            whitelistUseCase.removeContact(contact)
        }
    }
}
