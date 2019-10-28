package com.languagexx.simplenotes.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.languagexx.simplenotes.dao.NoteDao
import com.languagexx.simplenotes.database.NoteDatabase
import com.languagexx.simplenotes.entity.Note

class Repository(app:Application) {

    //Initilization
    var noteDao:NoteDao? = NoteDatabase.getDatabase(app)?.noteDao()

    //function to insert note in database
    fun insert(note: Note): Long? {
        return insertAsync(noteDao).execute(note).get()
    }

    //function to delete note in database
    fun delete(note: Note){
        deleteAsync(noteDao).execute(note)
    }

    //function to update note in database
    fun update(note: Note){
        updateAsync(noteDao).execute(note)
    }

    fun deleteAllNotes(){
        deleteAllAsync(noteDao).execute()
    }

    //function to get all notes in database
    fun getAllNotes():LiveData<List<Note>>{
       return getAllNotesAsync(noteDao).execute().get()
    }



    //background operation to insert note
    class insertAsync(noteDao: NoteDao?):AsyncTask<Note,Void,Long?>(){
        var noteDao = noteDao
        override fun doInBackground(vararg params: Note): Long? {
            return noteDao?.insert(params[0])
        }
    }

    //background operation to delete note
    class deleteAsync(noteDao: NoteDao?):AsyncTask<Note,Void,Unit>(){
        var noteDao = noteDao
        override fun doInBackground(vararg params: Note){
            noteDao?.delete(params[0])
        }
    }

    //background operation to  note
    class updateAsync(noteDao: NoteDao?):AsyncTask<Note,Void,Unit>(){
        var noteDao = noteDao
        override fun doInBackground(vararg params: Note){
            noteDao?.update(params[0])
        }
    }

    //background operation to  note
    class deleteAllAsync(noteDao: NoteDao?):AsyncTask<Unit,Unit,Unit>(){
        var noteDao = noteDao
        override fun doInBackground(vararg params:Unit){
            noteDao?.deleteAllNotes()
        }
    }

    //background operation to get all nots
    class getAllNotesAsync(noteDao: NoteDao?):AsyncTask<Unit,Void,LiveData<List<Note>>>(){

        var noteDao = noteDao
        override fun doInBackground(vararg params: Unit?): LiveData<List<Note>>? {
            return noteDao?.getAllNotes()
        }
    }





}