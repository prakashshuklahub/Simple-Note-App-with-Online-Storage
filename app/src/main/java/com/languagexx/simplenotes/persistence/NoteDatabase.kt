package com.languagexx.simplenotes.persistence

import androidx.room.Database
import androidx.room.RoomDatabase


// - Database Class
@Database(entities = [Note::class, LoginToken::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun loginDao(): LoginDao
}