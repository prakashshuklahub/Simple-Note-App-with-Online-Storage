package com.languagexx.simplenotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.languagexx.simplenotes.entity.Note
import com.languagexx.simplenotes.repository.Repository

class ViewModel(app:Application):AndroidViewModel(app) {

    var repository:Repository = Repository(app)

    //Database Operations in view model

    fun insert(note: Note):Long?{
        return repository.insert(note)
    }

    fun delete(note: Note){
        repository.delete(note)
    }

    fun update(note: Note){
        repository.update(note)
    }

    fun getAllNotes():LiveData<List<Note>>{
       return repository.getAllNotes()
    }
}