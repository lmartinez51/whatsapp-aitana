package com.lmartinez.miniaitana.feature.model

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.lmartinez.miniaitana.ui.theme.TerminalTextStyle
import com.lmartinez.miniaitana.ui.theme.TextPrimary
import com.lmartinez.miniaitana.ui.theme.TextSecondary

@Composable
fun ModelScreen(
    viewModel: ModelViewModel = hiltViewModel()
) {
    val configState by viewModel.config.collectAsState()
    val isCopying by viewModel.isCopyingModel.collectAsState()
    val config = configState ?: return // Show loading or empty if null

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.updateModelPath(it)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCarbon)
            .padding(16.dp)
    ) {
        Text(
            text = "Model & Service Config",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, MutedGray),
            colors = CardDefaults.cardColors(
                containerColor = Charcoal,
                contentColor = TextPrimary,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Model File",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (config.modelFilePath.isEmpty()) "No file selected" else config.modelFilePath,
                    style = TerminalTextStyle,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isCopying) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = HackerCyan,
                            trackColor = MutedGray,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Copying model to internal storage, please wait...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            filePickerLauncher.launch(arrayOf("*/*"))
                        },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HackerCyan,
                            contentColor = DeepCarbon,
                        )
                    ) {
                        Text("Select Model File")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
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
                        text = "Auto-Pilot Service",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = if (config.serviceEnabled) "Armed & Running" else "Disabled",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (config.serviceEnabled) HackerCyan else TextSecondary
                    )
                }
                Switch(
                    checked = config.serviceEnabled,
                    onCheckedChange = { viewModel.setServiceEnabled(it) },
                    enabled = !isCopying, // Disable switch while copying
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = DeepCarbon,
                        checkedTrackColor = HackerCyan,
                        checkedBorderColor = HackerCyan,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = Charcoal,
                        uncheckedBorderColor = MutedGray,
                        disabledCheckedThumbColor = DeepCarbon,
                        disabledCheckedTrackColor = HackerCyan.copy(alpha = 0.38f),
                        disabledUncheckedThumbColor = TextSecondary.copy(alpha = 0.38f),
                        disabledUncheckedTrackColor = Charcoal,
                        disabledUncheckedBorderColor = MutedGray,
                    )
                )
            }
        }
    }
}
