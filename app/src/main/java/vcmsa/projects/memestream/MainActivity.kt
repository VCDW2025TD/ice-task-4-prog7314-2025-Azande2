package com.vcmsa.projects.memestream

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser
        val welcomeText = findViewById<TextView>(R.id.txtWelcome)
        welcomeText.text = "Welcome, ${user?.displayName ?: "User"}"
    }
}
