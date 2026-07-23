package com.catharsis.notepad.data

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class NotesRepository(private val context: Context) {

    private val notesDir: File
        get() {
            val dir = File(context.filesDir, "notes")
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

    private val trashDir: File
        get() {
            val dir = File(context.filesDir, "trash")
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

    fun getAllNotes(): List<Note> {
        val files = notesDir.listFiles { _, name -> name.endsWith(".txt") } ?: emptyArray()
        return files.mapNotNull { file ->
            readNoteFromFile(file, isTrashed = false)
        }.sortedByDescending { it.fileName }
    }

    fun getTrashNotes(): List<Note> {
        val files = trashDir.listFiles { _, name -> name.endsWith(".txt") } ?: emptyArray()
        return files.mapNotNull { file ->
            readNoteFromFile(file, isTrashed = true)
        }.sortedByDescending { it.fileName }
    }

    fun getNoteById(id: String): Note? {
        val file = File(notesDir, "$id.txt")
        if (file.exists()) {
            return readNoteFromFile(file, isTrashed = false)
        }
        val trashFile = File(trashDir, "$id.txt")
        if (trashFile.exists()) {
            return readNoteFromFile(trashFile, isTrashed = true)
        }
        return null
    }

    fun createNote(content: String = ""): Note {
        val id = UUID.randomUUID().toString()
        val file = File(notesDir, "$id.txt")
        file.writeText(content)
        val title = extractTitle(content)
        return Note(
            id = id,
            fileName = "$id.txt",
            content = content,
            title = title,
            isTrashed = false
        )
    }

    fun saveNote(id: String, content: String): Note {
        val file = File(notesDir, "$id.txt")
        file.writeText(content)
        val title = extractTitle(content)
        return Note(
            id = id,
            fileName = "$id.txt",
            content = content,
            title = title,
            isTrashed = false
        )
    }

    fun moveToTrash(id: String): Boolean {
        val sourceFile = File(notesDir, "$id.txt")
        if (!sourceFile.exists()) return false
        val destFile = File(trashDir, "$id.txt")
        return sourceFile.renameTo(destFile)
    }

    fun restoreFromTrash(id: String): Boolean {
        val sourceFile = File(trashDir, "$id.txt")
        if (!sourceFile.exists()) return false
        val destFile = File(notesDir, "$id.txt")
        return sourceFile.renameTo(destFile)
    }

    fun deletePermanently(id: String): Boolean {
        val trashFile = File(trashDir, "$id.txt")
        if (trashFile.exists()) {
            return trashFile.delete()
        }
        val noteFile = File(notesDir, "$id.txt")
        if (noteFile.exists()) {
            return noteFile.delete()
        }
        return false
    }

    fun emptyTrash(): Boolean {
        val files = trashDir.listFiles() ?: return true
        var allDeleted = true
        for (file in files) {
            if (!file.delete()) allDeleted = false
        }
        return allDeleted
    }

    fun exportNotesToZipFile(): File? {
        val notes = getAllNotes()
        if (notes.isEmpty()) return null

        val exportDir = File(context.cacheDir, "exports")
        if (!exportDir.exists()) exportDir.mkdirs()

        val zipFile = File(exportDir, "CatharsisNotes_${System.currentTimeMillis()}.zip")
        try {
            ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
                val nameCounters = mutableMapOf<String, Int>()

                for (note in notes) {
                    val rawTitle = note.title.ifBlank { "Untitled" }
                    val sanitizedTitle = rawTitle.replace(Regex("[^a-zA-Z0-9._-]"), "_").take(40)

                    val count = nameCounters.getOrDefault(sanitizedTitle, 0)
                    nameCounters[sanitizedTitle] = count + 1

                    val entryName = if (count == 0) {
                        "$sanitizedTitle.txt"
                    } else {
                        "${sanitizedTitle}_$count.txt"
                    }

                    val entry = ZipEntry(entryName)
                    zos.putNextEntry(entry)
                    zos.write(note.content.toByteArray(Charsets.UTF_8))
                    zos.closeEntry()
                }
            }
            return zipFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun readNoteFromFile(file: File, isTrashed: Boolean): Note? {
        return try {
            val id = file.nameWithoutExtension
            val content = file.readText()
            val title = extractTitle(content)
            Note(
                id = id,
                fileName = file.name,
                content = content,
                title = title,
                isTrashed = isTrashed
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun extractTitle(content: String): String {
        val firstLine = content.lineSequence().firstOrNull { it.isNotBlank() }
        return firstLine?.trim() ?: "Untitled Note"
    }
}
