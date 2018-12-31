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
import com.example.iurymiguel.androidchatapp.databinding.FragmentUnsubscribedSubjectsBinding
import com.example.iurymiguel.androidchatapp.viewmodels.AuthenticationViewModel
import com.example.iurymiguel.androidchatapp.viewmodels.SubjectsViewModel
import com.example.iurymiguel.androidchatapp.views.subjects.recyclerAdapters.UnsubscribedSubjectsRecyclerAdapter
import org.koin.android.ext.android.inject

class UnsubscribedSubjectsFragment : Fragment() {


    private lateinit var mViewModel: SubjectsViewModel
    private lateinit var mBinding: FragmentUnsubscribedSubjectsBinding
    private val mAdapter: UnsubscribedSubjectsRecyclerAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = activity?.run {
            ViewModelProviders.of(this).get(SubjectsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        mViewModel.getUnsubscribedSubjectsLiveData().observe(this, Observer {
            mBinding.unsubscribedSubjectsList = it
            mAdapter.setDataSet(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate<FragmentUnsubscribedSubjectsBinding>(
            inflater,
            R.layout.fragment_unsubscribed_subjects, container, false
        )

        mBinding.unsubscribedSubjectsList = mViewModel.getUnsubscribedSubjectsLiveData().value
        mAdapter.setDataSet(mViewModel.getUnsubscribedSubjectsLiveData().value)

        mAdapter.setOnClickListener {
            mViewModel.mSelectedSubject = it
        }

        val recyclerView: RecyclerView = mBinding.unsubscribedSubjectsRecyclerView

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = UnsubscribedSubjectsFragment()
    }
}
