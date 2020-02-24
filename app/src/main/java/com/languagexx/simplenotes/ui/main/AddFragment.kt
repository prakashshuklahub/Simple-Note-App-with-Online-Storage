package com.languagexx.simplenotes.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.languagexx.simplenotes.R
import com.languagexx.simplenotes.persistence.Note
import com.languagexx.simplenotes.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add.*
import javax.inject.Inject


class AddFragment : DaggerFragment() {

    @Inject
    lateinit var viewmodelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel


    // Method #1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    // Method #2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        btnAdd.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.container).popBackStack()
        }
    }

    // Method #3
    private fun saveNoteToDatabase() {

        /*  Save Notes WHEN
         1) app is minimized or back button is clicked [#onPause method will be called
         2) save button is cliked
        */


        /* VALIDATIONS FOR SAVING NOTES

        1)  if TITLE IS NULL -- Title = "Empty Title"

        2)  if all the property of notes is null i.e title , description , color and tag
        then note will note will saved
        show toast "Note is discarded"

         */

        (activity as MainActivity?)?.showFloatingButton()

        if (validations()) {
            Toast.makeText(activity, "Note is saved", Toast.LENGTH_SHORT).show()
            saveNote()
        } else
            Toast.makeText(activity, "Note is Discarded", Toast.LENGTH_SHORT).show()

    }

    // Method #4
    override fun onDestroyView() {
        super.onDestroyView()
        saveNoteToDatabase()
    }


    // Method #5
    private fun saveNote() {

        //If title is null set Empty Title

        val note = Note(
            0,
            addTitle.text.toString(),
            addDescription.text.toString(),
            addTag.text.toString()
        )

        //If title is null set Empty Title
        if (addTitle.text.isNullOrEmpty()) {
            note.title = "Empty Title"

            //Call viewmodel to save the data
            noteViewModel.insert(note)

        } else {
            //Call viewmodel to save the data
            noteViewModel.insert(note)

        }

    }

    // Method #6
    fun validations(): Boolean {
        return !(addTitle.text.isNullOrEmpty()
                && addDescription.text.isNullOrEmpty()
                && addTag.text.isNullOrEmpty())
    }


    // Method #7
    private fun setupViewModel() {
        noteViewModel =
            ViewModelProvider(this, viewmodelProviderFactory).get(NoteViewModel::class.java)
    }


}