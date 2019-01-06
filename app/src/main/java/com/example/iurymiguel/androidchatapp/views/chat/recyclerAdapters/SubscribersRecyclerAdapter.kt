package com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.SubscribersItemListBinding
import com.example.iurymiguel.androidchatapp.model.User

class SubscribersRecyclerAdapter : RecyclerView.Adapter<SubscribersRecyclerAdapter.ViewHolder>() {

    private var mSubscribedUsersList: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SubscribersItemListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = mSubscribedUsersList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subscribedUser = mSubscribedUsersList[position]
        holder.bind(subscribedUser)
    }

    /**
     * Sets the list for adapter.
     * @param list the list of subscribed users.
     */
    fun setDataSet(list: MutableList<User>?) {
        mSubscribedUsersList = list ?: mutableListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: SubscribersItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subscriber: User) {
            binding.subscriber = subscriber
            binding.executePendingBindings()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("setIcon")
        fun setIcon(imageView: ImageView, isOnline: Boolean) {
            val resource = when (isOnline) {
                true -> R.drawable.online_icon
                else -> R.drawable.offline_icon
            }
            imageView.setImageResource(resource)
        }
    }
}