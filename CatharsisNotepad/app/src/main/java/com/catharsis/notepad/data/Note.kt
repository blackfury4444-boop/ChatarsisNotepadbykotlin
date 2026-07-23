package com.catharsis.notepad.data

data class Note(
    val id: String,          // Filename without extension or unique file ID
    val fileName: String,    // e.g. "my_note.txt"
    val content: String,     // Text content
    val title: String,       // Title extracted from first line or filename
    val isTrashed: Boolean = false
)
