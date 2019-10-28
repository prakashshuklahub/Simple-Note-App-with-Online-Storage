package com.languagexx.simplenotes

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.languagexx.simplenotes.helper.ButtonColor
import kotlinx.android.synthetic.main.activity_edit.*

//Activity to edit note
class EditActivity:AppCompatActivity() {

    var color:String? = null
    var id:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val tag = intent.getStringExtra("tag")
        color = intent.getStringExtra("color")
        id = intent.getStringExtra("id")

        //fill the edit text with note values
        editTitle.setText(title)
        editDescription.setText(description)
        editTag.setText(tag)
        cardVieweEdit.setCardBackgroundColor(Color.parseColor(color))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        saveNote()
        return true
    }

    override fun onBackPressed() {
        saveNote()
    }

   //button for color
    fun buttonColor(view: View) {
        val id = view.id
        val colorId = ButtonColor.buttonColor(id)
        color = "#"+Integer.toHexString(ContextCompat.getColor(this,colorId))
        cardVieweEdit.setBackgroundColor(Color.parseColor(color))
    }

    private fun saveNote(){
        val title = editTitle.text.toString()
        val description = editDescription.text.toString()
        val tag = editTag.text.toString()
        val color = color


        //sending values via intent
        val editIntent = intent
        editIntent.putExtra("title",title)
        editIntent.putExtra("description",description)
        editIntent.putExtra("tag",tag)
        editIntent.putExtra("id",id)
        if(color==null){
            editIntent.putExtra("color", "#ffffff")
        }
        else{
            editIntent.putExtra("color", color)
        }
        setResult(Activity.RESULT_OK,editIntent)
        finish()
    }
}