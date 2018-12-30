package com.example.iurymiguel.androidchatapp.views.authentication

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.FragmentSignUpBinding
import com.example.iurymiguel.androidchatapp.interfaces.FirebaseConnectionCallbacks
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.ProgressDialogProvider
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.AuthenticationViewModel
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.ext.android.inject

class SignUpFragment : Fragment() {

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
            .inflate<FragmentSignUpBinding>(inflater, R.layout.fragment_sign_up, container, false)

        binding.fragment = this

        return binding.root
    }


    /**
     * Registers a new user.
     */
    fun registerUser() {

        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val password = editPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Utils.showToast(context, "Preencha todos os campos.", Toast.LENGTH_SHORT)
            return
        }

        val user = User(name, email, password)

        mProgress.show(context)

        mViewModel.registerUser(user, object : FirebaseConnectionCallbacks {
            override fun <T> onSuccessConnection(args: Task<T>) {
                mProgress.dismiss()
                Toast.makeText(context, getString(R.string.register_account_success), Toast.LENGTH_SHORT).show()
                view!!.findNavController().popBackStack()
            }
            override fun <T> onFailedConnection(args: Task<T>) {
                mProgress.dismiss()
                val errorMessage = args.exception?.message?.trim()?.toLowerCase()
                handleError(errorMessage)
            }
        })

    }

    /**
     * Handles error showing the appropriate message.
     * @param errorMessage the message containing the error.
     */
    private fun handleError(errorMessage: String?) {
        val invalidEmail = Utils.ERROR_INVALID_EMAIL.trim().toLowerCase()
        val emailInUse = Utils.ERROR_EMAIL_ALREADY_IN_USE.trim().toLowerCase()
        val message = when (errorMessage) {
            invalidEmail -> getString(R.string.invalid_email)
            emailInUse -> getString(R.string.email_already_used)
            else -> getString(R.string.register_account_error)
        }
        Utils.showToast(context, message, Toast.LENGTH_SHORT)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
