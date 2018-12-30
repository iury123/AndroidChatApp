package com.example.iurymiguel.androidchatapp.views.authentication

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.FragmentSignInBinding
import com.example.iurymiguel.androidchatapp.interfaces.FirebaseConnectionCallbacks
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.ProgressDialogProvider
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.AuthenticationViewModel
import com.example.iurymiguel.androidchatapp.views.subjects.MainActivity
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.ext.android.inject

class SignInFragment : Fragment() {

    private lateinit var mViewModel: AuthenticationViewModel
    private val mProgress: ProgressDialogProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = activity?.run {
            ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
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


    fun signIn() {
        val email = editEmail.text.toString().trim()
        val password = editPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Utils.showToast(context, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT)
            return
        }

        val user = User(email = email, password = password)

        mProgress.show(context)

        mViewModel.signInUser(user, object : FirebaseConnectionCallbacks {
            override fun <T> onSuccessConnection(args: Task<T>) {
                mProgress.dismiss()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }

            override fun <T> onFailedConnection(args: Task<T>) {
                mProgress.dismiss()
                val errorMessage = args.exception?.message?.trim()?.toLowerCase()
                Utils.handleAuthError(context, errorMessage)
            }
        })
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
