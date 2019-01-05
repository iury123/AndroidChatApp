package com.example.iurymiguel.androidchatapp.interfaces

import com.example.iurymiguel.androidchatapp.model.Message

interface MessageCallbacks {
    fun onMessageSentConfirmed(message: Message)
    fun onMessageSentNotConfirmed(message: Message)
}