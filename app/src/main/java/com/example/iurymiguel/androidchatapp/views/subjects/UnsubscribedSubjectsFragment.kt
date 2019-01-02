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
import android.widget.Toast

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.FragmentUnsubscribedSubjectsBinding
import com.example.iurymiguel.androidchatapp.interfaces.FirebaseConnectionCallbacks
import com.example.iurymiguel.androidchatapp.utils.ProgressDialogProvider
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.AuthenticationViewModel
import com.example.iurymiguel.androidchatapp.viewmodels.SubjectsViewModel
import com.example.iurymiguel.androidchatapp.views.subjects.recyclerAdapters.UnsubscribedSubjectsRecyclerAdapter
import com.google.android.gms.tasks.Task
import org.koin.android.ext.android.inject

class UnsubscribedSubjectsFragment : Fragment() {


    private lateinit var mViewModel: SubjectsViewModel
    private lateinit var mBinding: FragmentUnsubscribedSubjectsBinding
    private val mAdapter: UnsubscribedSubjectsRecyclerAdapter by inject()
    private val mProgress: ProgressDialogProvider by inject()

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

        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_unsubscribed_subjects, container, false
        )

        mBinding.unsubscribedSubjectsList = mViewModel.getUnsubscribedSubjectsLiveData().value
        mAdapter.setDataSet(mViewModel.getUnsubscribedSubjectsLiveData().value)

        mAdapter.setOnClickListener {
            mViewModel.mSelectedSubject = it
            Utils.showAlert(
                context, getString(R.string.subject) + " ${mViewModel.mSelectedSubject.name}",
                getString(R.string.subject_ask_action), getString(R.string.subscribe), getString(R.string.delete),
                ::subscribeSubject, ::deleteSubject
            )
        }

        val recyclerView: RecyclerView = mBinding.unsubscribedSubjectsRecyclerView

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        return mBinding.root
    }

    /**
     * Subscribes in a selected subject.
     */
    private fun subscribeSubject() {
        mProgress.show(context)
        mViewModel.subscribesInSubject(object : FirebaseConnectionCallbacks {
            override fun <T> onSuccessConnection(args: Task<T>) {
                mProgress.dismiss()
                Utils.showToast(
                    context, getString(R.string.subscribed_subject) + " ${mViewModel.mSelectedSubject.name}",
                    Toast.LENGTH_SHORT
                )
            }

            override fun <T> onFailedConnection(args: Task<T>) {
                mProgress.dismiss()
                Utils.showToast(
                    context, getString(R.string.subscribe_error),
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    /**
     * Deletes a selected subject.
     */
    private fun deleteSubject() {
        mProgress.show(context)
        mViewModel.deleteSubject(object : FirebaseConnectionCallbacks {
            override fun <T> onSuccessConnection(args: Task<T>) {
                mProgress.dismiss()
                Utils.showToast(
                    context, getString(R.string.subject) +
                            " ${mViewModel.mSelectedSubject.name} " +
                            getString(R.string.deleted),
                    Toast.LENGTH_SHORT
                )
            }

            override fun <T> onFailedConnection(args: Task<T>) {
                mProgress.dismiss()
                Utils.showToast(
                    context, getString(R.string.delete_subject_error),
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = UnsubscribedSubjectsFragment()
    }
}
