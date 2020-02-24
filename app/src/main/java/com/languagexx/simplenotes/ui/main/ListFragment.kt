package com.languagexx.simplenotes.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.languagexx.simplenotes.R
import com.languagexx.simplenotes.persistence.Note
import com.languagexx.simplenotes.session.SessionManager
import com.languagexx.simplenotes.ui.adapter.NoteAdapter
import com.languagexx.simplenotes.ui.login.LoginActivity
import com.languagexx.simplenotes.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject


class ListFragment : DaggerFragment(),
    NoteAdapter.Interaction {

    private lateinit var noteAdapter: NoteAdapter

    private lateinit var noteViewModel: NoteViewModel

    @Inject
    lateinit var viewmodelProviderFactory: ViewModelProviderFactory

    lateinit var allNotes: List<Note>

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    @Inject
    lateinit var sessionManager: SessionManager

    var token: String? = null


    // Method #1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allNotes = arrayListOf()
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    // Method #2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)


        setupViewModel()    // Step 1
        initRecyclerView()  // Step 2
        getUserIdAndSetFireBaseReference() //Step 3
        observerLiveData()  // Step 4
    }

    // Method #3
    private fun observerLiveData() {
        noteViewModel.getAllNotes().observe(viewLifecycleOwner, Observer { lisOfNotes ->
            lisOfNotes?.let {
                allNotes = it
                noteAdapter.swap(it)
                firebaseDatabase.reference.setValue(allNotes)
            }
        })
        sessionManager.getToken().observe(viewLifecycleOwner, Observer {
            token = it.token
            Log.e("Debug", token)
        })
    }

    // Method #4
    private fun initRecyclerView() {
        recyclerView.apply {
            noteAdapter = NoteAdapter(
                allNotes,
                this@ListFragment
            )
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = noteAdapter
            val swipe = ItemTouchHelper(initSwipeToDelete())
            swipe.attachToRecyclerView(recyclerView)
        }
    }

    // Method #5
    private fun setupViewModel() {
        noteViewModel =
            ViewModelProvider(this, viewmodelProviderFactory).get(NoteViewModel::class.java)
    }

    // Method #6
    private fun initSwipeToDelete(): ItemTouchHelper.SimpleCallback {
        //Swipe recycler view items on RIGHT
        return object : ItemTouchHelper.SimpleCallback(
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
                noteViewModel.delete(allNotes.get(position))
            }
        }
    }

    // Method #7
    override fun onItemSelected(position: Int, item: Note) {
        val navDirection =
            ListFragmentDirections.actionListFragmentToEditFragment(
                item
            )
        findNavController().navigate(navDirection)
    }


    // Method #8
    private fun getUserIdAndSetFireBaseReference() {
        sessionManager.getToken().observe(viewLifecycleOwner, Observer {
            //Get reference to database node
            token = it.token
        })
    }

    // Method #9
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    // Method #10
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                sessionManager.deleteToken()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


/* This app is pushing database Notes list to firebase when live data detects changes ...


But this app will not satisfy need to accessing data in multiple devices hence that uses case is not satisfied

    1) To achieve use case of multiple device all data should be pushed to firebase independently
    --edit changes
    --add changes
    --delete changes

    and we can download the data from firebase on sync button by using addListenerForSingleValueEvent() firebase listner

    but still there can be conflict as primary key can be same if we use mutliple device ,, primary key are auto generated by room.
    in some cases data can be lost if

    1) device has generated a note data of primary key #54 and firebase already has data with id #54 ..data will be considered same and
    will be overwrite by firebase to device.

    for this use case different approach should be used

    Take insipiration from Google Keep , it is a example of multiple device notes App.
 */




