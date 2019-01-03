package com.example.iurymiguel.androidchatapp.model

data class Message(var key: String = "",
                   var content: String = "",
                   var senderUser: User = User(),
                   var receptorsSeen: HashMap<String, Boolean> = hashMapOf()
                   )