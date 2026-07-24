package com.example.catharsisnotepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrashAdapter(
    private val notes: MutableList<Note>,
    private val onNoteAction: (Note, Boolean) -> Unit
) : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trash_note, parent, false)
        return TrashViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.itemView.findViewById<Button>(R.id.restore_button).setOnClickListener {
            onNoteAction(note, true)
        }
        holder.itemView.findViewById<Button>(R.id.permanent_delete_button).setOnClickListener {
            onNoteAction(note, false)
        }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.note_title)
        private val creationDateTextView: TextView = itemView.findViewById(R.id.note_creation_date)
        private val modifiedDateTextView: TextView = itemView.findViewById(R.id.note_modified_date)

        fun bind(note: Note) {
            titleTextView.text = note.title
            creationDateTextView.text = note.creationDate
            modifiedDateTextView.text = note.lastModifiedDate
        }
    }
}