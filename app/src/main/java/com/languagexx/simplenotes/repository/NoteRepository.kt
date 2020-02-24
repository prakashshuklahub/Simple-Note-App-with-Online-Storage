package com.languagexx.simplenotes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.languagexx.simplenotes.persistence.Note
import com.languagexx.simplenotes.persistence.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteRepository @Inject constructor(val noteDao: NoteDao) {

    // Method #1
    //function to insert note in database
    fun insert(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.insert(note)
        }
    }

    // Method #2
    //function to delete note in database
    fun delete(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(note)
        }
    }

    // Method #3
    //function to delete note by Id in database
    fun deleteById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.deleteById(id)
        }
    }

    // Method #4
    //function to update note in database
    fun update(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.update(note)
            Log.e("DEBUG", "update is called in repo")

        }
    }

    // Method #5
    //function to get all notes in database
    fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

}