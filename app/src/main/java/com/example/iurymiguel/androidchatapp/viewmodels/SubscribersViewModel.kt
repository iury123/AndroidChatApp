package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.google.firebase.database.FirebaseDatabase

class SubscribersViewModel : ViewModel() {

    lateinit var mSubject: Subject
    val mSubscribedUsersList: MutableList<User> = mutableListOf()
    val mUsersReference = FirebaseDatabase.getInstance().reference.child(Utils.USERS)

}