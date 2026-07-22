package com.catharsis.notepad.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.catharsis.notepad.data.InterfaceColor
import com.catharsis.notepad.data.Theme

@Composable
fun SettingsScreen(
    currentTheme: Theme,
    currentInterfaceColor: InterfaceColor,
    onBackClick: () -> Unit,
    onNavigateToTrash: () -> Unit,
    onExportNotes: () -> Unit,
    onThemeSelected: (Theme) -> Unit,
    onInterfaceColorSelected: (InterfaceColor) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section: Storage & Tools
            SettingsSectionHeader(title = "Storage")

            SettingsRowItem(
                title = "Trash",
                icon = Icons.Default.DeleteSweep,
                onClick = onNavigateToTrash
            )

            SettingsRowItem(
                title = "Export Notes (.txt / zip)",
                icon = Icons.Default.IosShare,
                onClick = onExportNotes
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section: Theme Selection
            SettingsSectionHeader(title = "Theme")

            ThemeOption(
                label = "White",
                selected = currentTheme == Theme.WHITE,
                onSelect = { onThemeSelected(Theme.WHITE) }
            )
            ThemeOption(
                label = "Black",
                selected = currentTheme == Theme.BLACK,
                onSelect = { onThemeSelected(Theme.BLACK) }
            )
            ThemeOption(
                label = "Gray",
                selected = currentTheme == Theme.GRAY,
                onSelect = { onThemeSelected(Theme.GRAY) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section: Interface Color Selection
            SettingsSectionHeader(title = "Interface Color")

            InterfaceColorOption(
                label = "Default",
                selected = currentInterfaceColor == InterfaceColor.DEFAULT,
                onSelect = { onInterfaceColorSelected(InterfaceColor.DEFAULT) }
            )
            InterfaceColorOption(
                label = "Terminal Green",
                selected = currentInterfaceColor == InterfaceColor.TERMINAL_GREEN,
                onSelect = { onInterfaceColorSelected(InterfaceColor.TERMINAL_GREEN) }
            )
            InterfaceColorOption(
                label = "Amber Yellow",
                selected = currentInterfaceColor == InterfaceColor.AMBER_YELLOW,
                onSelect = { onInterfaceColorSelected(InterfaceColor.AMBER_YELLOW) }
            )
            InterfaceColorOption(
                label = "Blue",
                selected = currentInterfaceColor == InterfaceColor.BLUE,
                onSelect = { onInterfaceColorSelected(InterfaceColor.BLUE) }
            )
            InterfaceColorOption(
                label = "Red",
                selected = currentInterfaceColor == InterfaceColor.RED,
                onSelect = { onInterfaceColorSelected(InterfaceColor.RED) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingsRowItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
        thickness = 1.dp
    )
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun InterfaceColorOption(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
