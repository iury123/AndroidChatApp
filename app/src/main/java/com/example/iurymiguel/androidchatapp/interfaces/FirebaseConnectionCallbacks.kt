package com.example.iurymiguel.androidchatapp.interfaces

import com.google.android.gms.tasks.Task

interface FirebaseConnectionCallbacks {
    fun <T> onSuccessConnection(args: Task<T>)
    fun <T> onFailedConnection(args: Task<T>)
}