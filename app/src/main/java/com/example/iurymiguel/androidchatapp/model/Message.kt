package com.example.iurymiguel.androidchatapp.model

import com.example.iurymiguel.androidchatapp.utils.Utils

data class Message(
    var key: String = "",
    var content: String = "",
    var seenByAll: Boolean = false,
    var dateTime: String,
    var senderUser: User = User(),
    var messageStatus: Int = Utils.MESSAGE_STATUS.SENT_NOT_CONFIRMED,
    var commited: Boolean = false,
    var receptorsSeen: HashMap<String, Boolean> = hashMapOf()
)