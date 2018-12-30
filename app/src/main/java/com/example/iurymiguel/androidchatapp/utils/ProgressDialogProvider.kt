package com.example.iurymiguel.androidchatapp.utils

import android.app.ProgressDialog
import android.content.Context
import com.example.iurymiguel.androidchatapp.R

class ProgressDialogProvider {

    private lateinit var mProgressDialog: ProgressDialog

    /**
     * Shows the progress dialog.
     * @param context the current context.
     */
    fun show(context: Context?) {
        mProgressDialog = ProgressDialog.show(context, "", context!!.getString(R.string.loading))
        mProgressDialog.setCancelable(false)
    }

    /**
     * Dismisses the progress dialog.
     */
    fun dismiss() {
        mProgressDialog.dismiss()
    }

}