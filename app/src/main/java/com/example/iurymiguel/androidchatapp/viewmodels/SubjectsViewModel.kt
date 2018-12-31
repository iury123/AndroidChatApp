package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.model.Subject
import com.google.firebase.auth.FirebaseAuth

class SubjectsViewModel : ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mSubscribedSubjects: MutableLiveData<MutableList<Subject>>
    private lateinit var mUnubscribedSubjects: MutableLiveData<MutableList<Subject>>


    /**
     * Retreives the subscribed subjects live data.
     * @return the live data.
     */
    fun getSubscribedSubjects(): MutableLiveData<MutableList<Subject>> {
        if (!::mSubscribedSubjects.isInitialized) {
            mSubscribedSubjects = MutableLiveData()
        }
        return mSubscribedSubjects
    }

    /**
     * Retreives the unsubscribed subjects live data.
     * @return the live data.
     */
    fun getUnsubscribedSubjects(): MutableLiveData<MutableList<Subject>> {
        if (!::mUnubscribedSubjects.isInitialized) {
            mUnubscribedSubjects = MutableLiveData()
        }
        return mUnubscribedSubjects
    }

    /**
     * Signs out from the application.
     */
    fun logout() {
        mAuth.signOut()
    }

}