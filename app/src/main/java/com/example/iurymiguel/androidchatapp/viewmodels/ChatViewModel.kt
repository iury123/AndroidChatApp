package com.example.iurymiguel.androidchatapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.example.iurymiguel.androidchatapp.interfaces.MessageCallbacks
import com.example.iurymiguel.androidchatapp.model.Message
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatViewModel : ViewModel() {

    lateinit var mSubject: Subject
    val mMessagesList: MutableList<Message> = mutableListOf()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mMessageReference: DatabaseReference? = null


    /**
     * Retreives the database reference to messages of a subject.
     */
    fun getMessagesReference(): DatabaseReference {
        if (mMessageReference == null) {
            mMessageReference = FirebaseDatabase.getInstance().reference.child(Utils.SUBJECTS)
                .child(mSubject.key).child(Utils.MESSAGES)
        }
        return mMessageReference!!
    }


    fun sendMessage(message: Message, callback: MessageCallbacks) {

        val hash: HashMap<String, Any> = hashMapOf()
        hash[Utils.CONTENT] = message.content
        hash[Utils.DATETIME] = message.dateTime
        hash[Utils.SEEN_BY_ALL] = message.seenByAll
        hash[Utils.SENDER_USER_KEY] = message.senderUser.key
        hash[Utils.SENDER_NAME] = message.senderUser.name
        hash[Utils.SENDER_EMAIL] = message.senderUser.email
        hash[Utils.RECEPTORS_SEEN] = message.receptorsSeen

        val messageKey = getMessagesReference().push().key!!

        message.key = messageKey

        getMessagesReference().child(message.key).setValue(hash)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onMessageSentConfirmed(message)
                }
            }
        callback.onMessageSentNotConfirmed(message)
    }


    /**
     * Deletes all messages.
     */
    fun deleteAllMessages() {
        getMessagesReference().removeValue()
        mMessagesList.clear()
    }
}