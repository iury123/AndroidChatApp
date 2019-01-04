package com.example.iurymiguel.androidchatapp.model

import java.io.Serializable

data class Subject(
    var key: String, var name: String, var subscribers: HashMap<String, String>,
    var lastMessage: String = "", var numOfNotSeenMessages: Int = 0
) : Serializable