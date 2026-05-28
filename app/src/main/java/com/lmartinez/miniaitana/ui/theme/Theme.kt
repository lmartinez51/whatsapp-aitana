package com.lmartinez.miniaitana.ui.theme

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CyberDarkColorScheme = darkColorScheme(
    primary = WhatsAppCyberGreen,
    onPrimary = DeepCarbon,
    primaryContainer = GreenContainer,
    onPrimaryContainer = TextPrimary,
    inversePrimary = GreenPressed,
    secondary = TealEmeraldGreen,
    onSecondary = DeepCarbon,
    secondaryContainer = TealEmeraldGreen.copy(alpha = 0.24f),
    onSecondaryContainer = TextPrimary,
    tertiary = SuccessGreen,
    onTertiary = DeepCarbon,
    background = DeepCarbon,
    onBackground = TextPrimary,
    surface = Charcoal,
    onSurface = TextPrimary,
    surfaceVariant = MutedGray,
    onSurfaceVariant = TextSecondary,
    surfaceTint = WhatsAppCyberGreen,
    outline = MutedGray,
    outlineVariant = TealEmeraldGreen,
    inverseSurface = TextPrimary,
    inverseOnSurface = DeepCarbon,
    scrim = Color.Black,
    error = CriticalRed,
    onError = TextPrimary,
    errorContainer = CriticalContainer,
    onErrorContainer = TextPrimary,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    EnforceDarkSystemBars()

    MaterialTheme(
        colorScheme = CyberDarkColorScheme,
        typography = AppTypography,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            content = content,
        )
    }
}

@Composable
fun MiniAitanaTheme(content: @Composable () -> Unit) {
    AppTheme(content = content)
}

@Composable
private fun EnforceDarkSystemBars() {
    val view = LocalView.current
    if (view.isInEditMode) return

    SideEffect {
        val window = (view.context as? Activity)?.window ?: return@SideEffect
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }
}
