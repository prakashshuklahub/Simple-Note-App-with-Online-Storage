package com.languagexx.simplenotes

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.languagexx.simplenotes.helper.ButtonColor
import kotlinx.android.synthetic.main.activity_add.*

//Activity for adding new note
class AddActivity : AppCompatActivity() {


    var color:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //button for color
    fun buttonColor(view: View) {
        val id = view.id
        val colorId = ButtonColor.buttonColor(id)
        color = "#"+Integer.toHexString(ContextCompat.getColor(this,colorId))
        cardView.setBackgroundColor(Color.parseColor(color))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        saveNote()
        return true
    }

    override fun onBackPressed() {
        saveNote()
    }

    private fun saveNote(){
        //Geeting the values from editext
        val title = addTitle.text.toString()
        val description = addDescription.text.toString()
        val tag = addTag.text.toString()

        //sending the values via intent
        val addIntent = Intent()
        addIntent.putExtra("title", title)
        addIntent.putExtra("description", description)
        addIntent.putExtra("tag", tag)
        if(color==null){
            addIntent.putExtra("color", "#ffffff")
        }
        else{
            addIntent.putExtra("color", color)
        }

        setResult(Activity.RESULT_OK, addIntent)
        finish()
    }


}