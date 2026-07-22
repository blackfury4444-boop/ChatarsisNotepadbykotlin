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
import com.catharsis.notepad.data.AppThemeMode
import com.catharsis.notepad.data.InterfaceColor

@Composable
fun SettingsScreen(
    currentThemeMode: AppThemeMode,
    currentInterfaceColor: InterfaceColor,
    onBackClick: () -> Unit,
    onNavigateToTrash: () -> Unit,
    onExportNotes: () -> Unit,
    onThemeSelected: (AppThemeMode) -> Unit,
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
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
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
            SettingsSectionHeader(title = "Theme Selection")

            AppThemeOption(
                label = "System Default",
                selected = currentThemeMode == AppThemeMode.SYSTEM,
                onSelect = { onThemeSelected(AppThemeMode.SYSTEM) }
            )
            AppThemeOption(
                label = "Light",
                selected = currentThemeMode == AppThemeMode.LIGHT,
                onSelect = { onThemeSelected(AppThemeMode.LIGHT) }
            )
            AppThemeOption(
                label = "Dark",
                selected = currentThemeMode == AppThemeMode.DARK,
                onSelect = { onThemeSelected(AppThemeMode.DARK) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section: Interface Color Selection
            SettingsSectionHeader(title = "Interface Color Selection")

            InterfaceColorOption(
                label = "Paper (Warm)",
                selected = currentInterfaceColor == InterfaceColor.PAPER,
                onSelect = { onInterfaceColorSelected(InterfaceColor.PAPER) }
            )
            InterfaceColorOption(
                label = "Monochrome (Minimal)",
                selected = currentInterfaceColor == InterfaceColor.MONOCHROME,
                onSelect = { onInterfaceColorSelected(InterfaceColor.MONOCHROME) }
            )
            InterfaceColorOption(
                label = "Sepia",
                selected = currentInterfaceColor == InterfaceColor.SEPIA,
                onSelect = { onInterfaceColorSelected(InterfaceColor.SEPIA) }
            )
            InterfaceColorOption(
                label = "Slate",
                selected = currentInterfaceColor == InterfaceColor.SLATE,
                onSelect = { onInterfaceColorSelected(InterfaceColor.SLATE) }
            )
            InterfaceColorOption(
                label = "Emerald",
                selected = currentInterfaceColor == InterfaceColor.EMERALD,
                onSelect = { onInterfaceColorSelected(InterfaceColor.EMERALD) }
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
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
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
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
        thickness = 1.dp
    )
}

@Composable
private fun AppThemeOption(
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
            color = MaterialTheme.colorScheme.onBackground
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
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
