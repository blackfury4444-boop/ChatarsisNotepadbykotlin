package com.catharsis.notepad

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import com.catharsis.notepad.data.InterfaceColor
import com.catharsis.notepad.data.NotesRepository
import com.catharsis.notepad.data.SettingsRepository
import com.catharsis.notepad.data.Theme
import com.catharsis.notepad.ui.screens.MainScreen
import com.catharsis.notepad.ui.screens.NoteEditorScreen
import com.catharsis.notepad.ui.screens.SettingsScreen
import com.catharsis.notepad.ui.screens.TrashScreen
import com.catharsis.notepad.ui.theme.CatharsisNotepadTheme

sealed class Screen {
    object Main : Screen()
    data class NoteEditor(val noteId: String? = null) : Screen()
    object Settings : Screen()
    object Trash : Screen()
}

class MainActivity : ComponentActivity() {

    private lateinit var notesRepository: NotesRepository
    private lateinit var settingsRepository: SettingsRepository
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        notesRepository = NotesRepository(applicationContext)
        settingsRepository = SettingsRepository(applicationContext)

        setContent {
            var currentTheme by remember { mutableStateOf(settingsRepository.theme) }
            var currentInterfaceColor by remember { mutableStateOf(settingsRepository.interfaceColor) }

            CatharsisNotepadTheme(
                theme = currentTheme,
                interfaceColor = currentInterfaceColor
            ) {
                CatharsisApp(
                    notesRepository = notesRepository,
                    currentTheme = currentTheme,
                    currentInterfaceColor = currentInterfaceColor,
                    onThemeChange = { newTheme ->
                        settingsRepository.theme = newTheme
                        currentTheme = newTheme
                    },
                    onInterfaceColorChange = { newColor ->
                        settingsRepository.interfaceColor = newColor
                        currentInterfaceColor = newColor
                    },
                    onExportNotes = { shareExportedZip() },
                    onDoubleBackToExit = { handleDoubleBackPress() }
                )
            }
        }
    }

    private fun handleDoubleBackPress() {
        val currentTime = System.currentTimeMillis()
        if (backPressedTime + 2000 > currentTime) {
            finish()
            if (::toast.isInitialized) toast.cancel()
        } else {
            backPressedTime = currentTime
            toast = Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun shareExportedZip() {
        val zipFile = notesRepository.exportNotesToZipFile()
        if (zipFile != null && zipFile.exists()) {
            try {
                val uri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    zipFile
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/zip"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, "Export Notes"))
            } catch (e: Exception) {
                Toast.makeText(this, "Exported to: ${zipFile.absolutePath}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "No notes available to export", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun CatharsisApp(
    notesRepository: NotesRepository,
    currentTheme: Theme,
    currentInterfaceColor: InterfaceColor,
    onThemeChange: (Theme) -> Unit,
    onInterfaceColorChange: (InterfaceColor) -> Unit,
    onExportNotes: () -> Unit,
    onDoubleBackToExit: () -> Unit
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
    var notesList by remember { mutableStateOf(notesRepository.getAllNotes()) }
    var trashList by remember { mutableStateOf(notesRepository.getTrashNotes()) }

    fun refreshNotes() {
        notesList = notesRepository.getAllNotes()
        trashList = notesRepository.getTrashNotes()
    }

    when (val screen = currentScreen) {
        is Screen.Main -> {
            MainScreen(
                notes = notesList,
                onNewNoteClick = {
                    val newNote = notesRepository.createNote("")
                    refreshNotes()
                    currentScreen = Screen.NoteEditor(newNote.id)
                },
                onSettingsClick = {
                    currentScreen = Screen.Settings
                },
                onNoteClick = { note ->
                    currentScreen = Screen.NoteEditor(note.id)
                },
                onBackPress = {
                    onDoubleBackToExit()
                }
            )
        }

        is Screen.NoteEditor -> {
            val noteId = screen.noteId
            val existingNote = noteId?.let { notesRepository.getNoteById(it) }
            val initialContent = existingNote?.content ?: ""

            NoteEditorScreen(
                initialContent = initialContent,
                onSaveAndExit = { text ->
                    if (noteId != null) {
                        if (text.isBlank() && initialContent.isBlank()) {
                            notesRepository.deletePermanently(noteId)
                        } else {
                            notesRepository.saveNote(noteId, text)
                        }
                    }
                    refreshNotes()
                    currentScreen = Screen.Main
                },
                onDiscardAndExit = {
                    refreshNotes()
                    currentScreen = Screen.Main
                },
                onDeleteNote = {
                    if (noteId != null) {
                        notesRepository.moveToTrash(noteId)
                    }
                    refreshNotes()
                    currentScreen = Screen.Main
                }
            )
        }

        is Screen.Settings -> {
            SettingsScreen(
                currentTheme = currentTheme,
                currentInterfaceColor = currentInterfaceColor,
                onBackClick = { currentScreen = Screen.Main },
                onNavigateToTrash = {
                    refreshNotes()
                    currentScreen = Screen.Trash
                },
                onExportNotes = onExportNotes,
                onThemeSelected = onThemeChange,
                onInterfaceColorSelected = onInterfaceColorChange
            )
        }

        is Screen.Trash -> {
            TrashScreen(
                trashedNotes = trashList,
                onBackClick = { currentScreen = Screen.Settings },
                onRestoreNote = { id ->
                    notesRepository.restoreFromTrash(id)
                    refreshNotes()
                },
                onDeletePermanently = { id ->
                    notesRepository.deletePermanently(id)
                    refreshNotes()
                },
                onEmptyTrash = {
                    notesRepository.emptyTrash()
                    refreshNotes()
                }
            )
        }
    }
}
