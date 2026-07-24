package com.example.catharsisnotepad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Verify Android functionality
        verifyAndroidFunctionality()
    }

    private fun verifyAndroidFunctionality() {
        // Check if the main screen elements are visible
        val titleTextView = findViewById<TextView>(R.id.title_text_view)
        val settingsIcon = findViewById<ImageView>(R.id.settings_icon)
        val newNoteIcon = findViewById<ImageView>(R.id.new_note_icon)
        val noteList = findViewById<RecyclerView>(R.id.note_list)

        if (titleTextView != null && settingsIcon != null && newNoteIcon != null && noteList != null) {
            // Android functionality is verified
        } else {
            // Handle Android verification failure
        }
    }

    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            return
        }

        backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }

        // Verify UI functionality
        verifyUIFunctionality()
    }

    private fun verifyUIFunctionality() {
        // Check if the main screen elements are visible
        val titleTextView = findViewById<TextView>(R.id.title_text_view)
        val settingsIcon = findViewById<ImageView>(R.id.settings_icon)
        val newNoteIcon = findViewById<ImageView>(R.id.new_note_icon)
        val noteList = findViewById<RecyclerView>(R.id.note_list)

        if (titleTextView != null && settingsIcon != null && newNoteIcon != null && noteList != null) {
            // UI elements are visible
        } else {
            // Handle UI verification failure
        }
    }

        // Initialize RecyclerView and Adapter
        note_list.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(mutableListOf()) {
            // Handle note click
        }
        note_list.adapter = noteAdapter

        // Load notes asynchronously
        loadNotesAsync()
    }

    private fun loadNotesAsync() {
        Thread {
            val notes = NoteManager.loadNotes(this)
            notes.sortByDescending { it.lastModifiedDate }
            runOnUiThread {
                noteAdapter.updateNotes(notes)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources if needed
    }

        // Initialize RecyclerView
        note_list.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(mutableListOf()) {
            // Handle note click
        }
        note_list.adapter = noteAdapter

        // Load notes
        loadNotes()
    }

    private fun loadNotes() {
        // Load notes from index.txt
        val notes = NoteManager.loadNotes(this)

        // Sort notes by last modified date (newest first)
        notes.sortByDescending { it.lastModifiedDate }

        // Update adapter
        noteAdapter.updateNotes(notes)
    }
}