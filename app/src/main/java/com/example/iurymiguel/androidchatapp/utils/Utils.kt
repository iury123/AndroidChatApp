package com.example.iurymiguel.androidchatapp.utils

import android.content.Context
import android.widget.Toast
import com.example.iurymiguel.androidchatapp.R

class Utils private constructor() {

    companion object {

        const val SUBJECTS = "Subjects"
        const val SUBJECT_NAME = "subject_name"
        const val USERS = "Users"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        private const val ERROR_INVALID_EMAIL = "the email address is badly formatted."
        private const val ERROR_EMAIL_ALREADY_IN_USE = "the email address is already in use by another account."
        private const val ERROR_USER_NOT_FOUND =
            "There is no user record corresponding to this identifier. The user may have been deleted."
        private var toast: Toast? = null


        /**
         * Shows a toast on screen.
         * @param context the current context.
         * @param message the message to be shown.
         * @param duration the duration of the exhibition.
         */
        fun showToast(context: Context?, message: String, duration: Int = Toast.LENGTH_SHORT) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, message, duration)
            toast!!.show()
        }

        /**
         * Handles error showing the appropriate message.
         * @param context the current context.
         * @param errorMessage the message containing the error.
         */
        fun handleAuthError(context: Context?, errorMessage: String?, isRegisteringAccount: Boolean = false) {
            val invalidEmail = ERROR_INVALID_EMAIL.trim().toLowerCase()
            val emailInUse = ERROR_EMAIL_ALREADY_IN_USE.trim().toLowerCase()
            val userNotFound = ERROR_USER_NOT_FOUND.trim().toLowerCase()
            val message = when (errorMessage) {
                invalidEmail -> context!!.getString(R.string.invalid_email)
                emailInUse -> context!!.getString(R.string.email_already_used)
                userNotFound -> context!!.getString(R.string.user_not_found)
                else -> if (isRegisteringAccount) context!!.resources.getString(R.string.register_account_error)
                else context!!.getString(R.string.sign_in_error)
            }
            showToast(context, message, Toast.LENGTH_SHORT)
        }

    }
}