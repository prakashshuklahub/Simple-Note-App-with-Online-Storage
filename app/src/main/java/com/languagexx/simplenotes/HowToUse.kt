package com.languagexx.simplenotes

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_howtouse.*
import android.os.Handler



class HowToUse:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howtouse)
        val rightSwipe = AnimationUtils.loadAnimation(this, R.anim.animation_exit)

        Handler().postDelayed(Runnable {
            deleteCard.startAnimation(rightSwipe)
        }, 3000)
    }
}