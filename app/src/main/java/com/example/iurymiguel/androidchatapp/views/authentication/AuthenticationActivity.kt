package com.example.iurymiguel.androidchatapp.views.authentication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.views.subjects.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val auth = FirebaseAuth.getInstance().currentUser
        auth?.let {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
