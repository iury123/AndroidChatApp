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
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.ChatViewModel
import com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters.ChatRecyclerAdapter
import kotlinx.android.synthetic.main.activity_chat.*
import org.koin.android.ext.android.inject

class ChatActivity : AppCompatActivity() {

    private lateinit var mViewModel: ChatViewModel
    private lateinit var mBinding: ActivityChatBinding
    private val mAdapter: ChatRecyclerAdapter by inject()
    private lateinit var mCurrentUserEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        mBinding.activity = this

        mViewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

        mAdapter.setDataSet(mViewModel.mMessagesList)

        mCurrentUserEmail = mViewModel.mAuth.currentUser!!.email!!

        mAdapter.setCurrentUserEmail(mCurrentUserEmail)


        mBinding.chatRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


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


    private fun scrollToLastMessage() {
        mBinding.chatRecyclerView.scrollToPosition(mViewModel.mMessagesList.count() - 1)
    }
}
