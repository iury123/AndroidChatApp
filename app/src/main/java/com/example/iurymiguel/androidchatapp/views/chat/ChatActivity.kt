package com.example.iurymiguel.androidchatapp.views.chat

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.ActivityChatBinding
import com.example.iurymiguel.androidchatapp.model.Message
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.ChatViewModel
import com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters.ChatRecyclerAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_chat.*
import org.koin.android.ext.android.inject

class ChatActivity : AppCompatActivity() {

    private lateinit var mViewModel: ChatViewModel
    private lateinit var mBinding: ActivityChatBinding
    private val mAdapter: ChatRecyclerAdapter by inject()
    private lateinit var mCurrentUserEmail: String
    private val mChildEventListener = object : ChildEventListener {

        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {

        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {

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

        mCurrentUserEmail = mViewModel.mAuth.currentUser!!.email!!

        mAdapter.setCurrentUserEmail(mCurrentUserEmail)

        mBinding.chatRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        addListChildEventListener()
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

        val user = User(email = mCurrentUserEmail)

        val message = Message(
            content = messageContent,
            senderUser = user,
            dateTime = Utils.getCurrentDateTime()
        )

        mViewModel.mMessagesList += message
        mAdapter.notifyDataSetChanged()

        editMessage.text.clear()
        scrollToLastMessage()
    }

    /**
     * As the last message is sent, scrolls the list to the last one.
     */
    private fun scrollToLastMessage() {
        mBinding.chatRecyclerView.scrollToPosition(mViewModel.mMessagesList.count() - 1)
    }
}
