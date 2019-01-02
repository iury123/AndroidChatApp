package com.example.iurymiguel.androidchatapp.views.subjects

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.FragmentSubscribedSubjectsBinding
import com.example.iurymiguel.androidchatapp.viewmodels.SubjectsViewModel
import com.example.iurymiguel.androidchatapp.views.subjects.recyclerAdapters.SubscribedSubjectsRecyclerAdapter
import org.koin.android.ext.android.inject

class SubscribedSubjectsFragment : Fragment() {

    private lateinit var mViewModel: SubjectsViewModel
    private lateinit var mBinding: FragmentSubscribedSubjectsBinding
    private val mAdapter: SubscribedSubjectsRecyclerAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = activity?.run {
            ViewModelProviders.of(this).get(SubjectsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        mViewModel.getSubscribedSubjectsLiveData().observe(this, Observer {
            mBinding.subscribedSubjectsList = it
            mAdapter.setDataSet(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_subscribed_subjects, container, false
        )

        mBinding.subscribedSubjectsList = mViewModel.getSubscribedSubjectsLiveData().value
        mAdapter.setDataSet(mViewModel.getSubscribedSubjectsLiveData().value)

        mAdapter.setOnClickListener {
            mViewModel.mSelectedSubject = it
        }

        val recyclerView: RecyclerView = mBinding.subscribedSubjectsRecyclerView

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SubscribedSubjectsFragment()
    }
}
