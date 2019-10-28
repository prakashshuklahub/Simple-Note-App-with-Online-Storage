package com.languagexx.simplenotes

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.languagexx.simplenotes.FirebaseDatabase.FireStore
import com.languagexx.simplenotes.adapter.NoteAdapter
import com.languagexx.simplenotes.adapter.NoteAdapter.RecycleClick
import com.languagexx.simplenotes.entity.Note
import com.languagexx.simplenotes.helper.EmptyNotes
import com.languagexx.simplenotes.viewmodel.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.net.Uri

class MainActivity : AppCompatActivity(), RecycleClick {

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
    lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = LoginClass.auth.getCurrentUser()?.uid
        databaseReference = FireStore.getDatabase(user.toString())!!

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
                    val position = viewHolder.adapterPosition
                    val note = allNotes.get(position)
                    val id = note.id

                    val builder = AlertDialog.Builder(this@MainActivity)
                    //set title for alert dialog
                    builder.setTitle("Permanent delete this note?")
                    //set message for alert dialog
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing positive action
                    builder.setPositiveButton("Yes") { dialogInterface, which ->
                        Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                        viewModel.delete(allNotes.get(position))
                        databaseReference.child(id.toString()).removeValue()
                        Toast.makeText(applicationContext, "Note Deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                    //performing negative action
                    builder.setNegativeButton("No") { dialogInterface, which ->
                        viewModel.insert(note)
                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
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
            val addIntent = Intent(this, AddActivity::class.java)
            startActivityForResult(addIntent, add)
        }


        //Firebase Listner

        val postListner = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                viewModel.deleteAllNotes()
                dataSnapshot.children.forEach {
                    val id = it.child("id").value.toString()
                    val color = it.child("color").value.toString()
                    val description = it.child("description").value.toString()
                    val tag = it.child("tag").value.toString()
                    val title = it.child("title").value.toString()
                    val note = Note(id.toInt(), title, description, color, tag)
                    viewModel.insert(note)
                }
            }
        }
        databaseReference.addValueEventListener(postListner)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {
            item?.itemId == R.id.item_backup -> sync()
            item?.itemId == R.id.logout -> {
                LoginClass.auth.signOut()
                LoginClass.mGoogleSignInClient.signOut()
                finish()
                startActivity(Intent(this, LoginClass::class.java))
            }
            item?.itemId == R.id.policy -> {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse(
                    "https://privacypolicydetails.blogspot.com/2019/10/simple-notes.html"
                )
                startActivity(openURL)
            }
            item?.itemId == R.id.howToUse -> {
                val howToUse = Intent(this, HowToUse::class.java)
                startActivity(howToUse)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //recycler view item click action
    //this method is part of interface implemented
    override fun onItemClick(position: Int) {
        val note = noteAdapeter.getNoteAt(position)
        val id = note.id

        val editIntent = Intent(this, EditActivity::class.java)
        val title = note.title
        val description = note.description
        val tag = note.tag
        val color = note.color

        editIntent.putExtra("title", title)
        editIntent.putExtra("description", description)
        editIntent.putExtra("tag", tag)
        editIntent.putExtra("id", id.toString())
        editIntent.putExtra("color", color)

        startActivityForResult(editIntent, edit)
    }

    //getting result back from add and edit activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == add && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra("title")
            val description = data?.getStringExtra("description")
            val tag = data?.getStringExtra("tag")
            val color = data?.getStringExtra("color")

            val empty = EmptyNotes.checkForEmptyNotes(title, description, tag, this)
            if (empty == false) {
                val note =
                    Note(
                        0,
                        title.toString(),
                        description.toString(),
                        color.toString(),
                        tag.toString()
                    )
                val id: Long? = viewModel.insert(note)
                if (id != null) {
                    val note = Note(
                        id.toInt(),
                        title.toString(),
                        description.toString(),
                        color.toString(),
                        tag.toString()
                    )
                    databaseReference.child(id.toString()).setValue(note)
                    Toast.makeText(applicationContext, "Note Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Note not Saved", Toast.LENGTH_SHORT).show()
                }
            }


        }
        if (requestCode == edit && resultCode == Activity.RESULT_OK) {

            val title = data?.getStringExtra("title")
            val description = data?.getStringExtra("description")
            val tag = data?.getStringExtra("tag")
            val id = data?.getStringExtra("id")
            val color = data?.getStringExtra("color")
            val empty = EmptyNotes.checkForEmptyNotes(title, description, tag, this)
            val note = Note(
                id?.toInt()!!,
                title.toString(),
                description.toString(),
                color.toString(),
                tag.toString()
            )
            if (empty == false) {
                viewModel.update(note)
                databaseReference.child(id.toString()).setValue(note)
                Toast.makeText(applicationContext, "Note Updated", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.delete(note)
                databaseReference.child(id.toString()).removeValue()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        //if the user is not logged in
        //opening the login activity
        if (LoginClass.auth.getCurrentUser() == null) {
            finish()
            startActivity(Intent(this, LoginClass::class.java))
        }
    }

    private fun sync() {
        if (netConnection() != true) {
            Toast.makeText(this, "Syncing failed !! No internet", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Syncing , will take few seconds", Toast.LENGTH_SHORT).show()
        }
    }

   private fun netConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null && ni.isConnected
    }
}
