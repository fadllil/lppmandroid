package com.example.fadllil.lppmandroid

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class StartActivity : AppCompatActivity() {

    internal lateinit var app_splash: Animation
    internal lateinit var btt: Animation
    internal lateinit var app_logo: ImageView
    internal lateinit var app_subtitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash)
        btt = AnimationUtils.loadAnimation(this, R.anim.btt)

        //load elemen
        app_logo = findViewById(R.id.app_logo)
        app_subtitle = findViewById(R.id.app_subtitle)

        //run animation
        app_logo.startAnimation(app_splash)
        app_subtitle.startAnimation(btt)

        val handler = Handler()
        handler.postDelayed({
            //merubah activity ke activity lain
            val getstarted = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(getstarted)
            finish()
        }, 2000) //2000 ms = 2s
    }
}
