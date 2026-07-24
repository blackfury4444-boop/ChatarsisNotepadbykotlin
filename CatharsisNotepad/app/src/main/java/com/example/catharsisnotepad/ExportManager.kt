package com.example.catharsisnotepad

import android.content.Context
import java.io.File

object ExportManager {

    fun exportNote(context: Context, note: Note) {
        val fileName = "${note.title.replace(" ", "_")}.txt"
        val file = File(context.getExternalFilesDir(null), fileName)
        file.writeText("${note.title}\n${note.content}")

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        ))
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "Export Note"))
    }
}