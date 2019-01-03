package com.example.iurymiguel.androidchatapp.model

data class Message(
    var key: String = "",
    var content: String = "",
    var dateTime: String,
    var senderUser: User = User(),
    var receptorsSeen: HashMap<String, Boolean> = hashMapOf(),
    var messageStatus: Int = 0
)