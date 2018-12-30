package com.example.iurymiguel.androidchatapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.iurymiguel.androidchatapp.R

class SharedPreferencesProvider(val context: Context) {

    private val mSharedPreferences: SharedPreferences = context
        .getSharedPreferences(context.getString(R.string.shared_preferences), Context.MODE_PRIVATE)

    /**
     * Saves the logged email.
     * @param email the current email to be saved.
     */
    fun saveLoggedEmail(email: String) {
        val editor = mSharedPreferences.edit()
        editor.putString(context.getString(R.string.current_email_key), email)
        editor.apply()
    }

    /**
     * Retreives the current email
     * @return the email if exists.
     */
    fun getLoggedEmail() = mSharedPreferences
        .getString(context.getString(R.string.current_email_key), "")

}