package com.example.iurymiguel.androidchatapp.interfaces

interface ConnectionCallbacks {

    fun <T> onSuccessConnection(args: T)
    fun <T> onFailedConnection(args: T)
}