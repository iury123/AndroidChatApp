package com.example.iurymiguel.androidchatapp.utils

import android.app.ProgressDialog
import android.content.Context
import com.example.iurymiguel.androidchatapp.R

class ProgressDialogProvider {

    private lateinit var mProgressDialog: ProgressDialog

    fun show(context: Context?) {
        mProgressDialog = ProgressDialog.show(context, "", context!!.getString(R.string.loading))
        mProgressDialog.setCancelable(false)
    }

    fun dismiss() {
        mProgressDialog.dismiss()
    }

}