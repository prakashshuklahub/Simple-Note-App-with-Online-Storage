package com.languagexx.simplenotes.FirebaseDatabase

import android.provider.ContactsContract
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.languagexx.simplenotes.database.NoteDatabase

class FireStore {

    companion object{
        var instance: FirebaseDatabase? = null
        var databaseReference:DatabaseReference?=null

        fun getDatabase(user:String): DatabaseReference?{
            if (instance==null){
                instance = FirebaseDatabase.getInstance()
                instance?.setPersistenceEnabled(true)
                databaseReference?.keepSynced(true)
            }
            return instance?.getReference(user)
        }
    }

}