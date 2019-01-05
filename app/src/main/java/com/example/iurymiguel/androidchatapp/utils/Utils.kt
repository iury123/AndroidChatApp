package com.example.iurymiguel.androidchatapp.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.example.iurymiguel.androidchatapp.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class Utils private constructor() {

    object MESSAGE_STATUS {
        const val NONE = 0
        const val SENT_NOT_CONFIRMED = 1
        const val SENT_CONFIRMED = 2
        const val SEEN_BY_ALL = 3
    }

    companion object {

        const val INCOMING_MSG = 0
        const val OUTGOING_MSG = 1

        const val MESSAGES = "messages"
        const val CONTENT = "content"
        const val DATETIME = "datetime"
        const val SENDER_NAME = "sender_name"
        const val SENDER_USER_KEY = "sender_user_key"
        const val SENDER_EMAIL = "sender_email"
        const val RECEPTORS_SEEN = "receptors_seen"
        const val SEEN_BY_ALL = "seen_by_all"

        const val SUBJECTS = "Subjects"
        const val SUBSCRIBERS = "subscribers"
        const val SUBJECT_NAME = "subject_name"

        const val USERS = "Users"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val USER_IS_ONLINE = "user_is_online"

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

        /**
         * Shows a alert dialog.
         * @param context the current context.
         * @param title the title of alert dialog.
         * @param message the message of alert dialog.
         * @param callback1ButtonText the first button text.
         * @param callback2ButtonText the second button text if exists.
         * @param callback1 the callback for first button.
         * @param callback2 the callback for second button if exists.
         */
        fun showAlert(
            context: Context?, title: String = "", message: String = "",
            callback1ButtonText: String = "", callback2ButtonText: String? = "",
            callback1: () -> Unit, callback2: () -> Unit?
        ) {

            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(callback1ButtonText) { _, _ ->
                callback1()
            }

            callback2.let {
                builder.setNeutralButton(callback2ButtonText) { _, _ ->
                    it()
                }
            }
            builder.create().show()
        }


        /**
         * Gets the current date and time.
         * @return a string with date and time.
         */
        fun getCurrentDateTime(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss")
            return dateFormat.format(calendar.time)
        }

    }
}