package com.languagexx.simplenotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.languagexx.simplenotes.adapter.UserAdapter
import com.languagexx.simplenotes.entity.Note
import com.languagexx.simplenotes.viewmodel.ViewModel
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    //Object declaration
    lateinit var viewModel:ViewModel
    lateinit var getAllNotes: LiveData<List<Note>>
    lateinit var allNotes: List<Note>
    lateinit var userAdapeter: UserAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        //Attaching ViewModel
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)


        //Live Data
        getAllNotes = viewModel.getAllNotes()
        getAllNotes.observe(this, Observer {

            //update RecyclerView
            allNotes = getAllNotes.value!!
            recyclerViewUser.layoutManager = LinearLayoutManager(this)
            recyclerViewUser.adapter = UserAdapter(allNotes)
            userAdapeter = UserAdapter(allNotes)

        })

    }

    override fun onStart() {
        super.onStart()
        //if the user is not logged in
        //opening the login activity
        if (LoginClass.auth.getCurrentUser() == null) {
            finish();
            startActivity(Intent(this, LoginClass::class.java))
        }

    }

}