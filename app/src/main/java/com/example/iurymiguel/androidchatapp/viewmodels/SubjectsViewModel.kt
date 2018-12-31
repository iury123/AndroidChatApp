package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SubjectsViewModel: ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()

    /**
     * Signs out from the application.
     */
    fun logout() {
        mAuth.signOut()
    }

}