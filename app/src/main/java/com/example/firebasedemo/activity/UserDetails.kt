package com.example.firebasedemo.activity

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.firebasedemo.R

class UserDetails : AppCompatActivity() {
    var user_name: TextView? = null
    var user_message: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        user_name = findViewById(R.id.user_name)
        user_message = findViewById(R.id.user_message)

        if (intent != null) {
            user_message?.text = intent.getStringExtra("message")
            user_name?.text  = intent.getStringExtra("name")
        }


    }
}