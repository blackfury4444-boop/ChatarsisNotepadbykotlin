package com.catharsis.notepad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.catharsis.notepad.data.InterfaceColor
import com.catharsis.notepad.data.Theme

@Composable
fun CatharsisNotepadTheme(
    theme: Theme = Theme.WHITE,
    interfaceColor: InterfaceColor = InterfaceColor.DEFAULT,
    content: @Composable () -> Unit
) {
    // Theme determines background and surface colors
    val backgroundColor = when (theme) {
        Theme.WHITE -> WhiteBackground
        Theme.BLACK -> BlackBackground
        Theme.GRAY -> GrayBackground
    }

    val surfaceColor = when (theme) {
        Theme.WHITE -> WhiteSurface
        Theme.BLACK -> BlackSurface
        Theme.GRAY -> GraySurface
    }

    // Interface Color determines interactive UI element colors
    val onSurfaceColor = when (interfaceColor) {
        InterfaceColor.DEFAULT -> if (theme == Theme.BLACK || theme == Theme.GRAY) DefaultOnSurfaceDark else DefaultOnSurface
        InterfaceColor.TERMINAL_GREEN -> if (theme == Theme.BLACK || theme == Theme.GRAY) TerminalGreenDark else TerminalGreen
        InterfaceColor.AMBER_YELLOW -> if (theme == Theme.BLACK || theme == Theme.GRAY) AmberYellowDark else AmberYellow
        InterfaceColor.BLUE -> if (theme == Theme.BLACK || theme == Theme.GRAY) BlueDark else Blue
        InterfaceColor.RED -> if (theme == Theme.BLACK || theme == Theme.GRAY) RedDark else Red
    }

    // Create a custom color scheme with our theme and interface colors
    val colorScheme = androidx.compose.material3.lightColorScheme(
        primary = onSurfaceColor,
        onPrimary = backgroundColor,
        background = backgroundColor,
        surface = surfaceColor,
        onBackground = onSurfaceColor,
        onSurface = onSurfaceColor,
        surfaceVariant = surfaceColor,
        onSurfaceVariant = onSurfaceColor
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
