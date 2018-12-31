package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.interfaces.FirebaseConnectionCallbacks
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.google.firebase.database.FirebaseDatabase

class AddSubjectViewModel : ViewModel() {

    private val mDatabaseReference = FirebaseDatabase.getInstance().reference

    /**
     * Inserts a new subject in database.
     * @param subjectName the name of subject.
     * @param callback the callback to return the result to view.
     */
    fun addSubject(subjectName: String, callback: FirebaseConnectionCallbacks) {
        val subjectsReference = mDatabaseReference.child(Utils.SUBJECTS)
        val key = subjectsReference.push().key
        subjectsReference.child(key!!).child(Utils.SUBJECT_NAME).setValue(subjectName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccessConnection(it)
                } else {
                    callback.onFailedConnection(it)
                }
            }
    }


}