package com.languagexx.simplenotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.languagexx.simplenotes.adapter.NoteAdapter
import com.languagexx.simplenotes.adapter.NoteAdapter.RecycleClick
import com.languagexx.simplenotes.entity.Note
import com.languagexx.simplenotes.viewmodel.ViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :AppCompatActivity() , RecycleClick {

    //REQUEST_CODE_ADD
    companion object {
        val add = 1
        val edit = 2
    }

    //Object declaration
    lateinit var viewModel: ViewModel
    lateinit var noteAdapeter: NoteAdapter
    lateinit var getAllNotes: LiveData<List<Note>>
    lateinit var allNotes: List<Note>
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)
        databaseReference = firebaseDatabase.getReference("Prakash")
        databaseReference.keepSynced(true)

        //Swipe recycler view items on RIGHT
        val helper by lazy {
            object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    var position = viewHolder.adapterPosition
                    var note = allNotes.get(position)
                    var id = note.id
                    viewModel.delete(allNotes.get(position))
                    databaseReference.child(id.toString()).removeValue()
                    Toast.makeText(applicationContext, "Note Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }


        //Attaching ViewModel
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)


        //Live Data
        getAllNotes = viewModel.getAllNotes()
        getAllNotes.observe(this, Observer {

            //update RecyclerView
            allNotes = getAllNotes.value!!
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = NoteAdapter(allNotes, this)
            noteAdapeter = NoteAdapter(allNotes, this)
            val swipe = ItemTouchHelper(helper)
            swipe.attachToRecyclerView(recyclerView)
        })

        //Floating action button
        fab.setOnClickListener {
            var addIntent = Intent(this, AddActivity::class.java)
            startActivityForResult(addIntent, add)
        }

    }

    //recycler view item click action
    //this method is part of interface implemented
    override fun onItemClick(position: Int) {
        val note = noteAdapeter.getNoteAt(position)
        val id = note.id

        var editIntent = Intent(this, EditActivity::class.java)
        var title = note.title
        var description = note.description
        var tag = note.tag
        var color = note.color

        editIntent.putExtra("title", title)
        editIntent.putExtra("description", description)
        editIntent.putExtra("tag", tag)
        editIntent.putExtra("id", id.toString())
        editIntent.putExtra("color", color.toString())

        startActivityForResult(editIntent, edit)

        Log.e("MainActivityclick", title + description + tag + position)

    }

    //getting result back from add and edit activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==add&&resultCode== Activity.RESULT_OK){

            var title = data?.getStringExtra("title")
            var description =  data?.getStringExtra("description")
            var tag = data?.getStringExtra("tag")
            var color = data?.getStringExtra("color")
            val note = Note(0,title.toString(),description.toString(),color.toString(),tag.toString())
            val id:Long? = viewModel.insert(note)
            if (id!=null){
                val note = Note(id.toInt(),title.toString(),description.toString(),color.toString(),tag.toString())
                databaseReference.child(id.toString()).setValue(note)
                Toast.makeText(applicationContext,"Note Saved",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"Note not Saved",Toast.LENGTH_SHORT).show()
            }


        }
        if(requestCode== edit&&resultCode== Activity.RESULT_OK){

            var title = data?.getStringExtra("title")
            var description =  data?.getStringExtra("description")
            var tag = data?.getStringExtra("tag")
            var id = data?.getStringExtra("id")
            var color = data?.getStringExtra("color")
            Toast.makeText(applicationContext,"Note Updated",Toast.LENGTH_SHORT).show()
            val note  = Note(id?.toInt()!!,title.toString(),description.toString(),color.toString(),tag.toString())
            viewModel.update(note)
            databaseReference.child(id.toString()).setValue(note)
        }

    }
}
