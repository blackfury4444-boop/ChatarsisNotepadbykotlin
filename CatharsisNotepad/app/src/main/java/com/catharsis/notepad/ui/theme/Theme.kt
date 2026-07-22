package com.catharsis.notepad.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.catharsis.notepad.data.AppThemeMode
import com.catharsis.notepad.data.InterfaceColor

@Composable
fun CatharsisNotepadTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    interfaceColor: InterfaceColor = InterfaceColor.PAPER,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val backgroundColor = if (darkTheme) {
        when (interfaceColor) {
            InterfaceColor.PAPER -> PaperDarkBg
            InterfaceColor.MONOCHROME -> MonoDarkBg
            InterfaceColor.SEPIA -> SepiaDarkBg
            InterfaceColor.SLATE -> SlateDarkBg
            InterfaceColor.EMERALD -> EmeraldDarkBg
        }
    } else {
        when (interfaceColor) {
            InterfaceColor.PAPER -> PaperLightBg
            InterfaceColor.MONOCHROME -> MonoLightBg
            InterfaceColor.SEPIA -> SepiaLightBg
            InterfaceColor.SLATE -> SlateLightBg
            InterfaceColor.EMERALD -> EmeraldLightBg
        }
    }

    val textColor = if (darkTheme) Color(0xFFEEEEEE) else Color(0xFF111111)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = textColor,
            onPrimary = backgroundColor,
            background = backgroundColor,
            surface = backgroundColor,
            onBackground = textColor,
            onSurface = textColor,
            surfaceVariant = backgroundColor,
            onSurfaceVariant = textColor
        )
    } else {
        lightColorScheme(
            primary = textColor,
            onPrimary = backgroundColor,
            background = backgroundColor,
            surface = backgroundColor,
            onBackground = textColor,
            onSurface = textColor,
            surfaceVariant = backgroundColor,
            onSurfaceVariant = textColor
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
