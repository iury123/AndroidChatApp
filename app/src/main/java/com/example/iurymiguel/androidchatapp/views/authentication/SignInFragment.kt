package com.example.iurymiguel.androidchatapp.views.authentication

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil
            .inflate<FragmentSignInBinding>(inflater, R.layout.fragment_sign_in, container, false)

        binding.fragment = this

        return binding.root
    }

    /**
     * Buttons events which takes the user to sign up fragment.
     */
    fun goToSignUpFragment() {
        view!!.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}
