package com.lmartinez.miniaitana.feature.whitelist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lmartinez.miniaitana.ui.theme.Charcoal
import com.lmartinez.miniaitana.ui.theme.DeepCarbon
import com.lmartinez.miniaitana.ui.theme.HackerCyan
import com.lmartinez.miniaitana.ui.theme.MutedGray
import com.lmartinez.miniaitana.ui.theme.TextPrimary
import com.lmartinez.miniaitana.ui.theme.TextSecondary

@Composable
fun WhitelistScreen(
    viewModel: WhitelistViewModel = hiltViewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    var newContactName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCarbon)
            .padding(16.dp)
    ) {
        Text(
            text = "Whitelist Management",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newContactName,
                onValueChange = { newContactName = it },
                label = { Text("Sender Name") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HackerCyan,
                    unfocusedBorderColor = MutedGray,
                    focusedLabelColor = HackerCyan,
                    unfocusedLabelColor = TextSecondary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = HackerCyan,
                    focusedContainerColor = Charcoal,
                    unfocusedContainerColor = Charcoal,
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newContactName.isNotBlank()) {
                        viewModel.addContact(newContactName)
                        newContactName = ""
                    }
                },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HackerCyan,
                    contentColor = DeepCarbon,
                )
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(contacts) { contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, MutedGray),
                    colors = CardDefaults.cardColors(
                        containerColor = Charcoal,
                        contentColor = TextPrimary,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = contact.displayName,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = contact.normalizedName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        IconButton(onClick = { viewModel.removeContact(contact) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
