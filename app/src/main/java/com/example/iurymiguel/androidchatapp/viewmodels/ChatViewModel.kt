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
    private var mMessageReference: DatabaseReference? = null


    /**
     * Retreives the database reference to messages of a subject.
     * @return the database reference.
     */
    fun getMessagesReference(): DatabaseReference {
        if (mMessageReference == null) {
            mMessageReference = FirebaseDatabase.getInstance().reference.child(Utils.SUBJECTS)
                .child(mSubject.key).child(Utils.MESSAGES)
        }
        return mMessageReference!!
    }

    /**
     * Sends a new message to a group.
     * @param message the message to be sent.
     * @param callback the callback which returns to the view.
     */
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
     * Checks if the current user is the last one who has seen the message.
     * @param message the message seen.
     * @param currentUser user data.
     */
    fun isCurrentUserLastToSeeMessage(message: Message, currentUser: User): Boolean {
        var isTheLast = false
        loop@ for ((key, hasSeen) in message.receptorsSeen) {
            if (!hasSeen) {
                when (key) {
                    currentUser.key -> isTheLast = true
                    else -> {
                        isTheLast = false
                        break@loop
                    }
                }
            }
        }
        return isTheLast
    }

    /**
     * Updates the message status showing that everybody has read it.
     * @param message the message seen.
     */
    fun updateMessageSeenStatus(message: Message) {
        getMessagesReference().child(message.key).child(Utils.SEEN_BY_ALL).setValue(true)
    }

    /**
     * Updates receptor status showing that user has read the message.
     * @param message the message seen.
     * @param currentUser user data.
     */
    fun updateReceptorsSeenStatus(message: Message, currentUser: User) {
        getMessagesReference().child(message.key).child(Utils.RECEPTORS_SEEN).child(currentUser.key)
            .setValue(true)
    }

    /**
     * Deletes all messages.
     */
    fun deleteAllMessages() {
        getMessagesReference().removeValue()
        mMessagesList.clear()
    }
}