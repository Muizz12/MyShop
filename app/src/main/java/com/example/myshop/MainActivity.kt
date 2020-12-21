package com.example.myshop

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences=getSharedPreferences(Constants.MYSHOP_PREFERNCES,Context.MODE_PRIVATE)
        val username=sharedPreferences.getString(Constants.LOGGED_IN_USERNAME," ")
        tv_main.text="Hello $username"
    }
}