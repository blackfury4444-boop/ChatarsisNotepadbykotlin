package com.catharsis.notepad.data

import android.content.Context
import android.content.SharedPreferences

enum class Theme {
    WHITE, BLACK, GRAY
}

enum class InterfaceColor {
    DEFAULT, TERMINAL_GREEN, AMBER_YELLOW, BLUE, RED
}

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("catharsis_settings", Context.MODE_PRIVATE)

    var theme: Theme
        get() {
            val value = prefs.getString(KEY_THEME, Theme.WHITE.name)
            return try {
                Theme.valueOf(value ?: Theme.WHITE.name)
            } catch (e: Exception) {
                Theme.WHITE
            }
        }
        set(value) {
            prefs.edit().putString(KEY_THEME, value.name).apply()
        }

    var interfaceColor: InterfaceColor
        get() {
            val value = prefs.getString(KEY_INTERFACE_COLOR, InterfaceColor.DEFAULT.name)
            return try {
                InterfaceColor.valueOf(value ?: InterfaceColor.DEFAULT.name)
            } catch (e: Exception) {
                InterfaceColor.DEFAULT
            }
        }
        set(value) {
            prefs.edit().putString(KEY_INTERFACE_COLOR, value.name).apply()
        }

    companion object {
        private const val KEY_THEME = "theme"
        private const val KEY_INTERFACE_COLOR = "interface_color"
    }
}
