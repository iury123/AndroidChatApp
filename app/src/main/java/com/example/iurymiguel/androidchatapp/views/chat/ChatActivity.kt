package com.example.iurymiguel.androidchatapp.views.chat

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.ActivityChatBinding
import com.example.iurymiguel.androidchatapp.interfaces.MessageCallbacks
import com.example.iurymiguel.androidchatapp.model.Message
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.NetworkProvider
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.ChatViewModel
import com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters.ChatRecyclerAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_chat.*
import org.koin.android.ext.android.inject

private const val INDEX_OUT_OF_BOUNDS = -1

class ChatActivity : AppCompatActivity() {

    private lateinit var mViewModel: ChatViewModel
    private lateinit var mBinding: ActivityChatBinding
    private val mAdapter: ChatRecyclerAdapter by inject()
    private val mCurrentUser: User by inject()
    private val mNetworkProvider: NetworkProvider by inject()
    private val mChildEventListener = object : ChildEventListener {

        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            val message = buildMessageObject(dataSnapshot)
            addMessage(message)
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            val message = buildMessageObject(dataSnapshot)
            addMessage(message)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        mBinding.activity = this

        mViewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

        mViewModel.mSubject = intent.getSerializableExtra(getString(R.string.selected_subject_key)) as Subject

        title = mViewModel.mSubject.name

        mAdapter.setDataSet(mViewModel.mMessagesList)

        mAdapter.setCurrentUserEmail(mCurrentUser.email)

        mBinding.chatRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        addListChildEventListener()
        mViewModel.getMessagesReference().keepSynced(true)

    }


    override fun onDestroy() {
        super.onDestroy()
        removeListChildEventListener()
    }

    /**
     * Add a incoming message listener.
     */
    private fun addListChildEventListener() {
        mViewModel.getMessagesReference().addChildEventListener(mChildEventListener)
    }

    /**
     * Removes a incoming message listener.
     */
    private fun removeListChildEventListener() {
        mViewModel.getMessagesReference().removeEventListener(mChildEventListener)
    }


    /**
     * Sends a message.
     */
    fun sendMessage() {
        val messageContent = editMessage.text.toString().trim()

        if (messageContent.isEmpty()) {
            return
        }

        val message = Message(
            content = messageContent,
            senderUser = mCurrentUser,
            dateTime = Utils.getCurrentDateTime()
        )

        for ((key, value) in mViewModel.mSubject.subscribers) {
            if (key != mCurrentUser.key) {
                message.receptorsSeen[key] = false
            }
        }

        mViewModel.sendMessage(message, object : MessageCallbacks {

            override fun onMessageSentConfirmed(message: Message) {
                message.messageStatus = Utils.MESSAGE_STATUS.SENT_CONFIRMED
                mViewModel.mMessagesList += message
                mAdapter.notifyDataSetChanged()
            }

            override fun onMessageSentNotConfirmed(message: Message) {
                editMessage.text.clear()
            }
        })
    }

    /**
     * Builds message object from dataSnapshot sent by server.
     */
    private fun buildMessageObject(dataSnapshot: DataSnapshot): Message {
        val messageKey = dataSnapshot.key as String
        val value = dataSnapshot.value as HashMap<*, *>
        val content = value[Utils.CONTENT] as String
        val datetime = value[Utils.DATETIME] as String
        val seenByAll = value[Utils.SEEN_BY_ALL] as Boolean
        val receptorsSeen = value[Utils.RECEPTORS_SEEN] ?: hashMapOf<String, Boolean>()
        val senderUser = User(
            value[Utils.SENDER_USER_KEY] as String,
            value[Utils.SENDER_NAME] as String,
            value[Utils.SENDER_EMAIL] as String
        )

        return Message(
            messageKey,
            content,
            seenByAll,
            datetime,
            senderUser,
            receptorsSeen = receptorsSeen as HashMap<String, Boolean>
        )
    }

    /**
     * Adds incoming or outgoing message in the list.
     * @param message the message.
     */
    private fun addMessage(message: Message) {
        if (message.senderUser.email == mCurrentUser.email) {
            if (!message.seenByAll) {
                if (mNetworkProvider.isNetworkAvailable()) {
                    message.messageStatus = Utils.MESSAGE_STATUS.SENT_CONFIRMED
                }

                if (mViewModel.allSubscribersHasSeen(message)) {
                    mViewModel.updateMessageSeenStatus(message)
                }
            } else {
                message.messageStatus = Utils.MESSAGE_STATUS.SEEN_BY_ALL
            }
            updateList(message)
        } else if (message.receptorsSeen[mCurrentUser.key] != null) {
            if (!message.seenByAll) {
                mViewModel.updateReceptorsSeenStatus(message, mCurrentUser)
            }
            updateList(message)
        }
    }

    /**
     * Updates the list after an adding.
     * @param message the message added.
     */
    private fun updateList(message: Message) {
        mViewModel.mMessagesList += message
        mAdapter.notifyDataSetChanged()
        scrollToLastMessage()
    }

    /**
     * Adds a message in the list
     * @param message the message to be inserted.
     */
    operator fun MutableList<Message>.plusAssign(message: Message) {
        val index = this.indexInList(message)
        if (index > INDEX_OUT_OF_BOUNDS) {
            this[index] = message
        } else {
            this.add(message)
        }
    }

    /**
     * Gets the index of a message in list if exists.
     */
    private fun MutableList<Message>.indexInList(message: Message): Int {
        val messageKeys = this.map { it.key }
        return messageKeys.indexOf(message.key)
    }

    /**
     * As the last message is sent, scrolls the list to the last one.
     */
    private fun scrollToLastMessage() {
        mBinding.chatRecyclerView.scrollToPosition(mViewModel.mMessagesList.count() - 1)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_delete_all_messages) {
            mViewModel.deleteAllMessages()
            mAdapter.notifyDataSetChanged()
        }

        return super.onOptionsItemSelected(item)
    }


}
