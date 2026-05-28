package com.lmartinez.miniaitana.feature.prompt

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lmartinez.miniaitana.core.domain.model.PromptTemplate
import com.lmartinez.miniaitana.ui.theme.Charcoal
import com.lmartinez.miniaitana.ui.theme.DeepCarbon
import com.lmartinez.miniaitana.ui.theme.HackerCyan
import com.lmartinez.miniaitana.ui.theme.MutedGray
import com.lmartinez.miniaitana.ui.theme.TerminalTextStyle
import com.lmartinez.miniaitana.ui.theme.TextPrimary
import com.lmartinez.miniaitana.ui.theme.TextSecondary

@Composable
fun PromptScreen(
    viewModel: PromptViewModel = hiltViewModel()
) {
    val systemPrompt by viewModel.systemPrompt.collectAsState()
    val promptTemplates by viewModel.promptTemplates.collectAsState()
    var promptText by remember { mutableStateOf("") }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showVaultDialog by remember { mutableStateOf(false) }
    var editingTemplate by remember { mutableStateOf<PromptTemplate?>(null) }
    var pendingDeleteTemplate by remember { mutableStateOf<PromptTemplate?>(null) }

    LaunchedEffect(systemPrompt) {
        if (systemPrompt.isNotEmpty() && systemPrompt != promptText) {
            promptText = systemPrompt
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCarbon)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "System Prompt",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            OutlinedButton(
                onClick = { showVaultDialog = true },
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, HackerCyan),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = HackerCyan,
                )
            ) {
                Text("Vault")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = promptText,
            onValueChange = { promptText = it },
            label = { Text("System Prompt") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE,
            textStyle = TerminalTextStyle,
            shape = RoundedCornerShape(4.dp),
            colors = cyberOutlinedTextFieldColors(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { viewModel.updatePrompt(promptText) },
                enabled = promptText.isNotBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HackerCyan,
                    contentColor = DeepCarbon,
                    disabledContainerColor = MutedGray.copy(alpha = 0.48f),
                    disabledContentColor = TextSecondary,
                )
            ) {
                Text("Apply Prompt")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = { showSaveDialog = true },
                enabled = promptText.isNotBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (promptText.isNotBlank()) HackerCyan else MutedGray,
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = HackerCyan,
                    disabledContentColor = TextSecondary,
                )
            ) {
                Text("Save to Vault")
            }
        }
    }

    if (showSaveDialog) {
        SavePromptTemplateDialog(
            onDismiss = { showSaveDialog = false },
            onSave = { title ->
                viewModel.createTemplate(title, promptText)
                showSaveDialog = false
            }
        )
    }

    if (showVaultDialog) {
        PromptVaultDialog(
            factoryPrompt = viewModel.factoryDefaultPrompt,
            templates = promptTemplates,
            onDismiss = { showVaultDialog = false },
            onLoadFactory = {
                promptText = viewModel.factoryDefaultPrompt
                viewModel.loadFactoryDefaultPrompt()
                showVaultDialog = false
            },
            onLoadTemplate = { template ->
                promptText = template.content
                viewModel.updatePrompt(template.content)
                showVaultDialog = false
            },
            onEditTemplate = { template ->
                editingTemplate = template
            },
            onDeleteTemplate = { template ->
                pendingDeleteTemplate = template
            }
        )
    }

    editingTemplate?.let { template ->
        EditPromptTemplateDialog(
            template = template,
            onDismiss = { editingTemplate = null },
            onSave = { title, content ->
                viewModel.updateTemplate(template.id, title, content)
                editingTemplate = null
            }
        )
    }

    pendingDeleteTemplate?.let { template ->
        DeletePromptTemplateDialog(
            template = template,
            onDismiss = { pendingDeleteTemplate = null },
            onConfirm = {
                viewModel.deleteTemplate(template.id)
                pendingDeleteTemplate = null
            }
        )
    }
}

@Composable
private fun SavePromptTemplateDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    val canSave = title.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(4.dp),
        containerColor = Charcoal,
        tonalElevation = 0.dp,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = { Text("Save to Vault") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = cyberOutlinedTextFieldColors(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title) },
                enabled = canSave,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = HackerCyan,
                    disabledContentColor = TextSecondary,
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun PromptVaultDialog(
    factoryPrompt: String,
    templates: List<PromptTemplate>,
    onDismiss: () -> Unit,
    onLoadFactory: () -> Unit,
    onLoadTemplate: (PromptTemplate) -> Unit,
    onEditTemplate: (PromptTemplate) -> Unit,
    onDeleteTemplate: (PromptTemplate) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(4.dp),
        containerColor = Charcoal,
        tonalElevation = 0.dp,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = { Text("Prompt Vault") },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
            ) {
                item {
                    FactoryPromptRow(
                        prompt = factoryPrompt,
                        onLoad = onLoadFactory
                    )
                }

                items(
                    items = templates,
                    key = { it.id }
                ) { template ->
                    PromptTemplateRow(
                        template = template,
                        onLoad = { onLoadTemplate(template) },
                        onEdit = { onEditTemplate(template) },
                        onDelete = { onDeleteTemplate(template) }
                    )
                }

                if (templates.isEmpty()) {
                    item {
                        Text(
                            text = "No saved prompts",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = HackerCyan)
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun FactoryPromptRow(
    prompt: String,
    onLoad: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MutedGray),
        colors = CardDefaults.cardColors(
            containerColor = Charcoal,
            contentColor = TextPrimary,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Factory Default",
                style = MaterialTheme.typography.titleMedium,
                color = HackerCyan
            )
            Text(
                text = prompt.promptPreview(),
                style = TerminalTextStyle,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onLoad,
                    colors = ButtonDefaults.textButtonColors(contentColor = HackerCyan)
                ) {
                    Text("Load")
                }
            }
        }
    }
}

@Composable
private fun PromptTemplateRow(
    template: PromptTemplate,
    onLoad: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
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
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = template.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = template.content.promptPreview(),
                style = TerminalTextStyle,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                ) {
                    Text("Edit")
                }
                TextButton(
                    onClick = onLoad,
                    colors = ButtonDefaults.textButtonColors(contentColor = HackerCyan)
                ) {
                    Text("Load")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete ${template.title}",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun EditPromptTemplateDialog(
    template: PromptTemplate,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
) {
    var title by remember(template.id) { mutableStateOf(template.title) }
    var content by remember(template.id) { mutableStateOf(template.content) }
    val canSave = title.isNotBlank() && content.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(4.dp),
        containerColor = Charcoal,
        tonalElevation = 0.dp,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = { Text("Edit Template") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = cyberOutlinedTextFieldColors(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Prompt") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 220.dp, max = 320.dp),
                    maxLines = Int.MAX_VALUE,
                    textStyle = TerminalTextStyle,
                    shape = RoundedCornerShape(4.dp),
                    colors = cyberOutlinedTextFieldColors(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, content) },
                enabled = canSave,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = HackerCyan,
                    disabledContentColor = TextSecondary,
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun DeletePromptTemplateDialog(
    template: PromptTemplate,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(4.dp),
        containerColor = Charcoal,
        tonalElevation = 0.dp,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = { Text("Delete Template") },
        text = {
            Text(
                text = template.title,
                color = TextSecondary,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun cyberOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
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

private fun String.promptPreview(): String {
    val preview = lineSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .joinToString(" ")

    return if (preview.length <= 180) {
        preview
    } else {
        "${preview.take(177)}..."
    }
}
