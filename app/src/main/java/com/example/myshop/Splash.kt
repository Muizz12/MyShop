package com.example.myshop

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_splash.*


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }, 2000)


    }
    }


