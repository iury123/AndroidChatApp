package com.example.iurymiguel.androidchatapp.utils

import android.content.Context
import android.widget.Toast

class Utils private constructor() {
    companion object {
        const val USERS = "Users"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val ERROR_INVALID_EMAIL = "the email address is badly formatted."
        const val ERROR_EMAIL_ALREADY_IN_USE = "the email address is already in use by another account."
        private var toast: Toast? = null

        fun showToast(context: Context?, message: String, duration: Int = Toast.LENGTH_SHORT) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, message, duration)
            toast!!.show()
        }


    }
}