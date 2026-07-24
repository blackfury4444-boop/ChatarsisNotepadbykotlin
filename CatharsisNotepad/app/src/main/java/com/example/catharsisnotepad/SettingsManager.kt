package com.example.catharsisnotepad

import android.content.Context
import com.google.gson.Gson
import java.io.File

object SettingsManager {

    private const val SETTINGS_FILE_NAME = "settings.dat"

    fun loadSettings(context: Context): Settings {
        val settingsFile = File(context.filesDir, SETTINGS_FILE_NAME)
        if (settingsFile.exists()) {
            val json = settingsFile.readText()
            return Gson().fromJson(json, Settings::class.java)
        }
        val defaultSettings = Settings(
            theme = "White",
            accentColor = "Default"
        )
        saveSettings(context, defaultSettings)
        return defaultSettings
    }

    fun saveSettings(context: Context, settings: Settings) {
        val settingsFile = File(context.filesDir, SETTINGS_FILE_NAME)
        val json = Gson().toJson(settings)
        settingsFile.writeText(json)
    }
}