package com.languagexx.simplenotes.helper

import android.view.View
import androidx.cardview.widget.CardView
import com.languagexx.simplenotes.R
import kotlinx.android.synthetic.main.activity_edit.*

class ButtonColor {
    companion object {
        fun buttonColor(id: Int): Int {
            var color = R.id.twenty
            if (id == R.id.one) {
                color = R.color.one
            } else if (id == R.id.two) {
                color = R.color.two
            } else if (id == R.id.three) {
                color = R.color.three
            } else if (id == R.id.four) {
                color = R.color.four
            } else if (id == R.id.five) {
                color = R.color.five
            } else if (id == R.id.six) {
                color = R.color.six
            } else if (id == R.id.seven) {
                color = R.color.seven
            } else if (id == R.id.eight) {
                color = R.color.eight
            } else if (id == R.id.nine) {
                color = R.color.nine
            } else if (id == R.id.ten) {
                color = R.color.ten
            } else if (id == R.id.eleven) {
                color = R.color.eleven
            } else if (id == R.id.twelve) {
                color = R.color.twelve
            } else if (id == R.id.thirteen) {
                color = R.color.thirteen
            } else if (id == R.id.fourteen) {
                color = R.color.fourteen
            } else if (id == R.id.fifteen) {
                color = R.color.fifteen
            } else if (id == R.id.sixteen) {
                color = R.color.sixteen
            } else if (id == R.id.seventeen) {
                color = R.color.seventeen
            } else if (id == R.id.eighteen) {
                color = R.color.eighteen
            } else if (id == R.id.nineteen) {
                color = R.color.nineteen
            } else if (id == R.id.twenty) {
                color = R.color.twenty
            }
            return color
        }
    }
}