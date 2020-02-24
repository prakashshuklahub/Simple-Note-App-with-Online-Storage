package com.languagexx.simplenotes.ui.main

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_edit.*
import javax.inject.Inject


class EditFragment : DaggerFragment() {

    @Inject
    lateinit var viewmodelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    // Method #1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        prepareNoteForEditing()
        setupViewModel()

        btnEdit.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.container).popBackStack()
        }
    }

    // Method #2
    private fun saveNoteToDatabase() {

        //Save Notes WHEN app is minimized or back button is clicked [#onPause method will be called]
        //Save Notes WHEN save button is cliked


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
            val id: Int = EditFragmentArgs.fromBundle(
                arguments!!
            ).note?.id!!
            Log.e("DEBUG", "saving note $id")

        } else {
            Toast.makeText(activity, "Note is Discarded", Toast.LENGTH_SHORT).show()
            //Delete the note if all fields are empty (this is done by user)
            val id: Int = EditFragmentArgs.fromBundle(
                arguments!!
            ).note?.id!!
            noteViewModel.deleteById(id)
            Log.e("DEBUG", "deleting note")
        }
    }

    // Method #3
    override fun onDestroyView() {
        super.onDestroyView()
        saveNoteToDatabase()
    }

    // Method #4
    private fun saveNote() {

        //getting the id from bundle , we are using that id to update/edit the note
        val id: Int? = EditFragmentArgs.fromBundle(
            arguments!!
        ).note?.id

        val note = Note(
            id!!,
            editTitle.text.toString(),
            editDescription.text.toString(),
            editTag.text.toString()
        )

        //If title is null set Empty Title
        if (editTitle.text.isNullOrEmpty()) {
            note.title = "Empty Title"

            //Call viewmodel to save the data
            noteViewModel.update(note)

        } else {
            //Call viewmodel to save the data
            Log.e("DEBUG", "saving note update is called")
            noteViewModel.update(note)
        }
    }

    // Method #5
    fun validations(): Boolean {
        return !(editTitle.text.isNullOrEmpty()
                && editDescription.text.isNullOrEmpty()
                && editTag.text.isNullOrEmpty())
    }


    // Method #6
    private fun setupViewModel() {
        noteViewModel =
            ViewModelProvider(this, viewmodelProviderFactory).get(NoteViewModel::class.java)
    }


    // Method #7
    private fun prepareNoteForEditing() {
        // Getting the note from the bundle
        //Save args plugin is used as i believe bundle is not good for sending large data
        arguments?.let {
            val safeArgs =
                EditFragmentArgs.fromBundle(
                    it
                )
            val note = safeArgs.note
            editTitle.setText(note?.title.toString())
            editDescription.setText(note?.description.toString())
            editTag.setText(note?.tag.toString())
        }

    }
}

