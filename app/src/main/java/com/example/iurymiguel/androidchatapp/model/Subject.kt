package com.example.iurymiguel.androidchatapp.model

data class Subject(
    var key: String, var name: String, var subscribers: HashMap<String, String>,
    var lastMessage: String = "", var numOfNotSeenMessages: Int = 0
)