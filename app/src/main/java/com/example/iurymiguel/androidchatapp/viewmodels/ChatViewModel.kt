package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.model.Message
import com.google.firebase.auth.FirebaseAuth

class ChatViewModel : ViewModel() {

    val mMessagesList: MutableList<Message> = mutableListOf()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()



}