package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.interfaces.FirebaseConnectionCallbacks
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SubjectsViewModel : ViewModel() {

    val mSubjectsReference = FirebaseDatabase.getInstance().reference.child(Utils.SUBJECTS)
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mSelectedSubject: Subject
    private lateinit var mSubscribedSubjects: MutableLiveData<MutableList<Subject>>
    private lateinit var mUnsubscribedSubjects: MutableLiveData<MutableList<Subject>>

    /**
     * Retreives the subscribed subjects live data.
     * @return the live data.
     */
    fun getSubscribedSubjectsLiveData(): MutableLiveData<MutableList<Subject>> {
        if (!::mSubscribedSubjects.isInitialized) {
            mSubscribedSubjects = MutableLiveData()
        }
        return mSubscribedSubjects
    }

    /**
     * Retreives the unsubscribed subjects live data.
     * @return the live data.
     */
    fun getUnsubscribedSubjectsLiveData(): MutableLiveData<MutableList<Subject>> {
        if (!::mUnsubscribedSubjects.isInitialized) {
            mUnsubscribedSubjects = MutableLiveData()
        }
        return mUnsubscribedSubjects
    }


    /**
     * Subscribes in a subject
     * @param callback callback which returns to view.
     */
    fun subscribesInSubject(callback: FirebaseConnectionCallbacks) {
        val userId = mAuth.currentUser!!.uid
        val userEmail = mAuth.currentUser!!.email
        mSubjectsReference.child(mSelectedSubject.key).child(Utils.SUBSCRIBERS)
            .child(userId).setValue(userEmail)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccessConnection(it)
                } else {
                    callback.onFailedConnection(it)
                }
            }
    }

    /**
     * Deletes a subject.
     * @param callback callback which returns to view.
     */
    fun deleteSubject(callback: FirebaseConnectionCallbacks) {
        mSubjectsReference.child(mSelectedSubject.key).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccessConnection(it)
                } else {
                    callback.onFailedConnection(it)
                }
            }
    }

    /**
     * Signs out from the application.
     */
    fun logout() {
        val userId = mAuth.currentUser!!.uid
        val usersReference = FirebaseDatabase.getInstance().reference.child(Utils.USERS)
        usersReference.child(userId).child(Utils.USER_IS_ONLINE).setValue(false)
        mAuth.signOut()
    }

}