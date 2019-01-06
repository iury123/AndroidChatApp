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
    private val mSubjectsReference = FirebaseDatabase.getInstance()
        .reference.child(Utils.SUBJECTS)


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
        hash[Utils.COMMITED] = message.commited
        hash[Utils.RECEPTORS_SEEN] = message.receptorsSeen

        val messageKey = getMessagesReference().push().key!!

        message.key = messageKey

        getMessagesReference().child(message.key).setValue(hash)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateMessageCommitStatus(message.key)
                    callback.onMessageSentConfirmed(message)
                }
            }
        callback.onMessageSentNotConfirmed(message)
    }

    /**
     * Confirms the database commit of a message.
     * @param messageKey the key of the message.
     */
    fun updateMessageCommitStatus(messageKey: String) {
        getMessagesReference().child(messageKey).child(Utils.COMMITED).setValue(true)
    }

    /**
     * Checks if all receptors have read the message.
     * @param message the message.
     * @return true or false.
     */
    fun allSubscribersHasSeen(message: Message): Boolean {

        if (message.receptorsSeen.isEmpty()) return false

        for ((key, hasSeen) in message.receptorsSeen) {
            if (!hasSeen) {
                return false
            }
        }
        return true
    }

    /**
     * Updates the message status showing that everybody has read it.
     * @param message the message seen.
     */
    fun updateMessageSeenByAllStatus(message: Message) {
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

    /**
     * Deletes the subject from database
     */
    fun deleteSubject() {
        mSubjectsReference.child(mSubject.key).removeValue()
    }

    /**
     * Unsubscribes the current user.
     * @param currentUser the current user data.
     */
    fun unsubscribeSubject(currentUser: User) {
        mSubjectsReference.child(mSubject.key).child(Utils.SUBSCRIBERS)
            .child(currentUser.key).removeValue()
    }


}