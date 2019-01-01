package com.example.iurymiguel.androidchatapp.views.subjects

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.viewmodels.SubjectsViewModel

class SubscribedSubjectsFragment : Fragment() {

    private lateinit var mViewModel: SubjectsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscribed_subjects, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SubscribedSubjectsFragment()
    }
}
