package com.example.iurymiguel.androidchatapp.views.authentication

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil
            .inflate<FragmentSignUpBinding>(inflater, R.layout.fragment_sign_up, container, false)

        binding.fragment = this

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
