package com.example.catharsisnotepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onViewRecycled(holder: NoteViewHolder) {
        super.onViewRecycled(holder)
        // Clean up resources if needed
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        // Clean up resources if needed
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.itemView.setOnClickListener { onNoteClick(note) }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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