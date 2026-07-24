package com.example.catharsisnotepad

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note_editor.*

class NoteEditorActivity : AppCompatActivity() {

    private var noteId: String? = null
    private var isNewNote: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        noteId = intent.getStringExtra("NOTE_ID")
        isNewNote = noteId == null

        originalContent = note_content.text.toString()

        // Restore draft if available
        val draft = NoteManager.loadDraft(this)
        if (draft != null && noteId == draft.first) {
            note_content.setText(draft.second)
        } else if (!isNewNote) {
            val note = NoteManager.loadNote(this, noteId!!)
            note_content.setText("${note.title}\n${note.content}")
        }

        note_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Auto-save draft
                if (!isNewNote) {
                    val content = s.toString()
                    NoteManager.saveDraft(this@NoteEditorActivity, noteId!!, content)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        save_button.setOnClickListener {
            saveNote()
        }

        cancel_button.setOnClickListener {
            finish()
        }

        export_button.setOnClickListener {
            exportNote()
        }
    }

    override fun onBackPressed() {
        if (note_content.text.toString() != originalContent) {
            AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Do you want to save them?")
                .setPositiveButton("Save") { _, _ ->
                    saveNote()
                    super.onBackPressed()
                }
                .setNegativeButton("Discard") { _, _ ->
                    super.onBackPressed()
                }
                .setNeutralButton("Cancel", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }

        save_button.setOnClickListener {
            saveNote()
        }

        cancel_button.setOnClickListener {
            finish()
        }

        export_button.setOnClickListener {
            exportNote()
        }
    }

    private fun saveNote() {
        val content = note_content.text.toString()
        val lines = content.split("\n")
        val title = if (lines.isNotEmpty()) lines[0] else "Untitled"
        val noteContent = if (lines.size > 1) lines.subList(1, lines.size).joinToString("\n") else ""

        if (isNewNote) {
            val note = Note(
                id = generateNoteId(),
                title = title,
                content = noteContent,
                creationDate = getCurrentDate(),
                lastModifiedDate = getCurrentDate(),
                status = "active",
                fileLocation = "notes/${note.id}.txt"
            )
            NoteManager.saveNote(this, note)
        } else {
            val note = Note(
                id = noteId!!,
                title = title,
                content = noteContent,
                creationDate = NoteManager.loadNote(this, noteId!!).creationDate,
                lastModifiedDate = getCurrentDate(),
                status = "active",
                fileLocation = "notes/${noteId}.txt"
            )
            NoteManager.saveNote(this, note)
        }

        finish()
    }

    private fun exportNote() {
        val content = note_content.text.toString()
        val lines = content.split("\n")
        val title = if (lines.isNotEmpty()) lines[0] else "Untitled"
        val noteContent = if (lines.size > 1) lines.subList(1, lines.size).joinToString("\n") else ""

        val note = Note(
            id = noteId ?: generateNoteId(),
            title = title,
            content = noteContent,
            creationDate = getCurrentDate(),
            lastModifiedDate = getCurrentDate(),
            status = "active",
            fileLocation = ""
        )

        ExportManager.exportNote(this, note)
    }

    private fun generateNoteId(): String {
        return System.currentTimeMillis().toString()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}