package com.example.catharsisnotepad

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object NoteManager {

    private const val INDEX_FILE_NAME = "index.txt"
    private const val NOTES_DIR = "notes"
    private const val TRASH_DIR = "trash"
    private const val DRAFT_FILE_NAME = "draft.txt"

    fun loadNotes(context: Context): List<Note> {
        val notes = mutableListOf<Note>()
        val indexFile = File(context.filesDir, INDEX_FILE_NAME)

        if (indexFile.exists()) {
            indexFile.forEachLine {
                val parts = it.split("|")
                if (parts.size == 6 && parts[4] == "active") {
                    val note = Note(
                        id = parts[0],
                        title = parts[1],
                        creationDate = parts[2],
                        lastModifiedDate = parts[3],
                        status = parts[4],
                        fileLocation = parts[5]
                    )
                    notes.add(note)
                }
            }
        }

        return notes
    }

    fun verifyCoreFunctionality(context: Context): Boolean {
        val testNote = Note(
            id = "test",
            title = "Test Note",
            content = "Test Content",
            creationDate = "2026-07-24",
            lastModifiedDate = "2026-07-24",
            status = "active",
            fileLocation = "notes/test.txt"
        )
        saveNote(context, testNote)
        val loadedNote = loadNote(context, "test")
        deleteNote(context, "test")
        return loadedNote.title == "Test Note" && loadedNote.content == "Test Content"
    }

    fun verifyFileSystem(context: Context): Boolean {
        val settingsFile = File(context.filesDir, SETTINGS_FILE_NAME)
        val indexFile = File(context.filesDir, INDEX_FILE_NAME)
        val notesDir = File(context.filesDir, NOTES_DIR)
        val trashDir = File(context.filesDir, TRASH_DIR)

        return settingsFile.exists() && indexFile.exists() && notesDir.exists() && trashDir.exists()
    }

    fun loadDeletedNotes(context: Context): List<Note> {
        val deletedNotes = mutableListOf<Note>()
        val indexFile = File(context.filesDir, INDEX_FILE_NAME)

        if (indexFile.exists()) {
            indexFile.forEachLine {
                val parts = it.split("|")
                if (parts.size == 6 && parts[4] == "deleted") {
                    val note = Note(
                        id = parts[0],
                        title = parts[1],
                        creationDate = parts[2],
                        lastModifiedDate = parts[3],
                        status = parts[4],
                        fileLocation = parts[5]
                    )
                    deletedNotes.add(note)
                }
            }
        }

        return deletedNotes
    }

    fun deleteNote(context: Context, noteId: String) {
        val indexFile = File(context.filesDir, INDEX_FILE_NAME)
        val indexContent = mutableListOf<String>()

        if (indexFile.exists()) {
            indexFile.forEachLine {
                val parts = it.split("|")
                if (parts[0] == noteId) {
                    val noteFile = File(context.filesDir, parts[5])
                    val trashDir = File(context.filesDir, TRASH_DIR)
                    if (!trashDir.exists()) {
                        trashDir.mkdirs()
                    }
                    val trashFile = File(trashDir, "${noteId}.txt")
                    noteFile.renameTo(trashFile)
                    indexContent.add("${parts[0]}|${parts[1]}|${parts[2]}|${parts[3]}|deleted|${TRASH_DIR}/${noteId}.txt")
                } else {
                    indexContent.add(it)
                }
            }
            indexFile.writeText(indexContent.joinToString("\n"))
        }
    }

    fun restoreNote(context: Context, noteId: String) {
        val indexFile = File(context.filesDir, INDEX_FILE_NAME)
        val indexContent = mutableListOf<String>()

        if (indexFile.exists()) {
            indexFile.forEachLine {
                val parts = it.split("|")
                if (parts[0] == noteId) {
                    val trashFile = File(context.filesDir, parts[5])
                    val notesDir = File(context.filesDir, NOTES_DIR)
                    if (!notesDir.exists()) {
                        notesDir.mkdirs()
                    }
                    val noteFile = File(notesDir, "${noteId}.txt")
                    trashFile.renameTo(noteFile)
                    indexContent.add("${parts[0]}|${parts[1]}|${parts[2]}|${parts[3]}|active|${NOTES_DIR}/${noteId}.txt")
                } else {
                    indexContent.add(it)
                }
            }
            indexFile.writeText(indexContent.joinToString("\n"))
        }
    }

    fun permanentDeleteNote(context: Context, noteId: String) {
        val indexFile = File(context.filesDir, INDEX_FILE_NAME)
        val indexContent = mutableListOf<String>()

        if (indexFile.exists()) {
            indexFile.forEachLine {
                val parts = it.split("|")
                if (parts[0] != noteId) {
                    indexContent.add(it)
                } else {
                    val trashFile = File(context.filesDir, parts[5])
                    trashFile.delete()
                }
            }
            indexFile.writeText(indexContent.joinToString("\n"))
        }
    }

    fun loadNote(context: Context, noteId: String): Note {
        val noteFile = File(context.filesDir, "$NOTES_DIR/$noteId.txt")
        val content = noteFile.readText()
        val lines = content.split("\n")
        val title = lines[0]
        val noteContent = if (lines.size > 1) lines.subList(1, lines.size).joinToString("\n") else ""

        return Note(
            id = noteId,
            title = title,
            content = noteContent,
            creationDate = "",
            lastModifiedDate = "",
            status = "active",
            fileLocation = "notes/$noteId.txt"
        )
    }

    fun saveNote(context: Context, note: Note) {
        val notesDir = File(context.filesDir, NOTES_DIR)
        if (!notesDir.exists()) {
            notesDir.mkdirs()
        }

        val noteFile = File(notesDir, "${note.id}.txt")
        noteFile.writeText("${note.title}\n${note.content}")

        updateIndex(context, note)
    }

    fun saveDraft(context: Context, noteId: String, content: String) {
        val draftFile = File(context.filesDir, DRAFT_FILE_NAME)
        draftFile.writeText("$noteId|$content")
    }

    fun loadDraft(context: Context): Pair<String, String>? {
        val draftFile = File(context.filesDir, DRAFT_FILE_NAME)
        if (draftFile.exists()) {
            val content = draftFile.readText()
            val parts = content.split("|")
            if (parts.size == 2) {
                return Pair(parts[0], parts[1])
            }
        }
        return null
    }

    private fun updateIndex(context: Context, note: Note) {
        val indexFile = File(context.filesDir, INDEX_FILE_NAME)
        val indexContent = mutableListOf<String>()

        if (indexFile.exists()) {
            indexFile.forEachLine {
                val parts = it.split("|")
                if (parts[0] != note.id) {
                    indexContent.add(it)
                }
            }
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        indexContent.add("${note.id}|${note.title}|${note.creationDate}|${currentDate}|${note.status}|${note.fileLocation}")
        indexFile.writeText(indexContent.joinToString("\n"))
    }
}