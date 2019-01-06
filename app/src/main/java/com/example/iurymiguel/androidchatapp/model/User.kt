package com.example.iurymiguel.androidchatapp.model

data class User(
    var key: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = ""
) {
    var isOnline: Boolean = false
}
