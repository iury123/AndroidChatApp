package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.model.Message
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatViewModel : ViewModel() {

    lateinit var mSubject: Subject
    val mMessagesList: MutableList<Message> = mutableListOf()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Retreives the database reference to messages of a subject.
     */
    fun getMessagesReference() = FirebaseDatabase.getInstance().reference.child(Utils.SUBJECTS)
        .child(mSubject.key).child(Utils.MESSAGES)
}