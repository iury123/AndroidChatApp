package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.interfaces.FirebaseConnectionCallbacks
import com.example.iurymiguel.androidchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthenticationViewModel : ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mUsersReference = FirebaseDatabase
        .getInstance().reference.child(Utils.USERS)

    /**
     * Registers a user in firebase authentication.
     * @param user the user to be registered.
     * @param callback Callback to return data to view.
     */
    fun registerUser(user: User, callback: FirebaseConnectionCallbacks) {
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    saveAuthenticatedUserInDatabase(user, callback)
                } else {
                    callback.onFailedConnection(it)
                }
            }
    }

    /**
     * Saves the registered user in firebase realtime database.
     * @param user the user to be registered.
     * @param callback Callback to return data to view.
     */
    private fun saveAuthenticatedUserInDatabase(user: User, callback: FirebaseConnectionCallbacks) {
        val userId = mAuth.currentUser!!.uid
        val userReference = mUsersReference.child(userId)

        val hash = HashMap<String, Any>()

        hash[Utils.USER_NAME] = user.name
        hash[Utils.USER_EMAIL] = user.email
        hash[Utils.USER_IS_ONLINE] = true

        userReference.setValue(hash)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccessConnection(it)
                } else {
                    callback.onFailedConnection(it)
                }
            }
    }

    /**
     * Signs in the system.
     * @param user the user to be signed in.
     * @param callback Callback to return data to view.
     */
    fun signInUser(user: User, callback: FirebaseConnectionCallbacks) {
        mAuth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userId = mAuth.currentUser!!.uid
                    mUsersReference.child(userId).child(Utils.USER_IS_ONLINE).setValue(true)
                    callback.onSuccessConnection(it)
                } else {
                    callback.onFailedConnection(it)
                }
            }
    }

}