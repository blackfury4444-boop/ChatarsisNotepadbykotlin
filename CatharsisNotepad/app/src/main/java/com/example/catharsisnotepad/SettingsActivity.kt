package com.example.catharsisnotepad

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeAdapter: ArrayAdapter<String>
    private lateinit var accentColorAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize theme spinner
        val themes = arrayOf("White", "Royal Blue", "Black", "Gray", "AMOLED Black")
        themeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themes)
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        theme_spinner.adapter = themeAdapter

        // Initialize accent color spinner
        val accentColors = arrayOf("Default", "Terminal Green", "Amber Yellow", "Red", "Purple", "Orange", "Blue", "Light Blue")
        accentColorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, accentColors)
        accentColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        accent_color_spinner.adapter = accentColorAdapter

        // Load current settings
        loadSettings()

        // Save settings
        save_settings_button.setOnClickListener {
            saveSettings()
        }

        // Cancel settings
        cancel_settings_button.setOnClickListener {
            finish()
        }
    }

    private fun loadSettings() {
        val settings = SettingsManager.loadSettings(this)
        theme_spinner.setSelection(themeAdapter.getPosition(settings.theme))
        accent_color_spinner.setSelection(accentColorAdapter.getPosition(settings.accentColor))
    }

    private fun saveSettings() {
        val selectedTheme = theme_spinner.selectedItem.toString()
        val selectedAccentColor = accent_color_spinner.selectedItem.toString()

        val settings = Settings(
            theme = selectedTheme,
            accentColor = selectedAccentColor
        )

        SettingsManager.saveSettings(this, settings)
        finish()
    }
}