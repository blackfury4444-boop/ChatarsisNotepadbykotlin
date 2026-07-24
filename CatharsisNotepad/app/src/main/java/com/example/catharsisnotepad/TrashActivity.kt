package com.example.catharsisnotepad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_trash.*

class TrashActivity : AppCompatActivity() {

    private lateinit var trashAdapter: TrashAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)

        // Initialize RecyclerView
        trash_list.layoutManager = LinearLayoutManager(this)
        trashAdapter = TrashAdapter(mutableListOf()) { note, isRestore ->
            if (isRestore) {
                NoteManager.restoreNote(this, note.id)
                loadDeletedNotes()
            } else {
                showConfirmationDialog(note.id)
            }
        }
        trash_list.adapter = trashAdapter

        // Load deleted notes
        loadDeletedNotes()
    }

    private fun loadDeletedNotes() {
        val deletedNotes = NoteManager.loadDeletedNotes(this)
        trashAdapter.updateNotes(deletedNotes)
    }

    private fun showConfirmationDialog(noteId: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Permanent Delete")
            .setMessage("Are you sure you want to permanently delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                NoteManager.permanentDeleteNote(this, noteId)
                loadDeletedNotes()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}