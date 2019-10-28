package com.languagexx.simplenotes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.languagexx.simplenotes.entity.Note


//interface Dao for Note table sql operations
@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("select * from tbl_note")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("delete from tbl_note")
    fun deleteAllNotes()

}