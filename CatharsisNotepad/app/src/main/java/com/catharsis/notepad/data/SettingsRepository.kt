package com.catharsis.notepad.data

import android.content.Context
import android.content.SharedPreferences

enum class AppThemeMode {
    SYSTEM, LIGHT, DARK
}

enum class InterfaceColor {
    PAPER, MONOCHROME, SEPIA, SLATE, EMERALD
}

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("catharsis_settings", Context.MODE_PRIVATE)

    var themeMode: AppThemeMode
        get() {
            val value = prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.name)
            return try {
                AppThemeMode.valueOf(value ?: AppThemeMode.SYSTEM.name)
            } catch (e: Exception) {
                AppThemeMode.SYSTEM
            }
        }
        set(value) {
            prefs.edit().putString(KEY_THEME_MODE, value.name).apply()
        }

    var interfaceColor: InterfaceColor
        get() {
            val value = prefs.getString(KEY_INTERFACE_COLOR, InterfaceColor.PAPER.name)
            return try {
                InterfaceColor.valueOf(value ?: InterfaceColor.PAPER.name)
            } catch (e: Exception) {
                InterfaceColor.PAPER
            }
        }
        set(value) {
            prefs.edit().putString(KEY_INTERFACE_COLOR, value.name).apply()
        }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_INTERFACE_COLOR = "interface_color"
    }
}
