package com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iurymiguel.androidchatapp.databinding.IncomingMsgLayoutBinding
import com.example.iurymiguel.androidchatapp.databinding.OutgoingMsgLayoutBinding
import com.example.iurymiguel.androidchatapp.model.Message
import com.example.iurymiguel.androidchatapp.utils.Utils

class ChatRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mMessagesList: MutableList<Message> = mutableListOf()
    private lateinit var mCurrentUserEmail: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Utils.INCOMING_MSG -> {
                val binding = IncomingMsgLayoutBinding.inflate(layoutInflater, parent, false)
                IncomingMessageViewHolder(binding)
            }
            else -> {
                val binding = OutgoingMsgLayoutBinding.inflate(layoutInflater, parent, false)
                OutgoingMessageViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = mMessagesList.count()

    override fun getItemViewType(position: Int): Int {
        val userEmail = mMessagesList[position].senderUser.email
        return when (userEmail) {
            mCurrentUserEmail -> Utils.OUTGOING_MSG
            else -> Utils.INCOMING_MSG
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            Utils.INCOMING_MSG -> {
                val incomingMsgHolder = holder as IncomingMessageViewHolder
                incomingMsgHolder.bind(mMessagesList[position])
            }
            else -> {
                val outgoingMsgHolder = holder as OutgoingMessageViewHolder
                outgoingMsgHolder.bind(mMessagesList[position])
            }
        }
    }

    fun setDataSet(list: MutableList<Message>?) {
        mMessagesList = list ?: mutableListOf()
        notifyDataSetChanged()
    }

    fun setCurrentUserEmail(email: String) {
        mCurrentUserEmail = email
    }

    inner class IncomingMessageViewHolder(val binding: IncomingMsgLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
        }
    }

    inner class OutgoingMessageViewHolder(val binding: OutgoingMsgLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
        }
    }

}