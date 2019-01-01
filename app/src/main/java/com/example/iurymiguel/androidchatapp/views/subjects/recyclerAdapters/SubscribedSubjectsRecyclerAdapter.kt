package com.example.iurymiguel.androidchatapp.views.subjects.recyclerAdapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iurymiguel.androidchatapp.databinding.SubscribedSubjectItemListBinding
import com.example.iurymiguel.androidchatapp.model.Subject

class SubscribedSubjectsRecyclerAdapter : RecyclerView.Adapter<SubscribedSubjectsRecyclerAdapter.ViewHolder>() {

    private var mSubscribedSubjectsList: MutableList<Subject> = mutableListOf()
    private var mOnClickListenerCallback: ((Subject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SubscribedSubjectItemListBinding.inflate(layoutInflater, parent, false)
        val holder = ViewHolder(binding)
        binding.itemList.setOnClickListener {
            mOnClickListenerCallback?.let {
                it(mSubscribedSubjectsList[holder.adapterPosition])
            }
        }
        return holder
    }

    override fun getItemCount() = mSubscribedSubjectsList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subject = mSubscribedSubjectsList[position]
        holder.bind(subject)
    }

    /**
     * Sets the data to be displayed in list.
     * @param list the list to be displayed.
     */
    fun setDataSet(list: MutableList<Subject>?) {
        mSubscribedSubjectsList = list ?: mutableListOf()
        notifyDataSetChanged()
    }


    /**
     * Sets a click listener
     * @param callback the listener of click event.
     */
    fun setOnClickListener(callback: (Subject) -> Unit) {
        mOnClickListenerCallback = callback
    }


    inner class ViewHolder(val binding: SubscribedSubjectItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject) {
            binding.subject = subject
            binding.executePendingBindings()
        }
    }
}