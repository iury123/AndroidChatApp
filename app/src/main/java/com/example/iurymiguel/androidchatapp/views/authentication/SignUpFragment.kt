package com.example.iurymiguel.androidchatapp.views.authentication

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.FragmentSignUpBinding
import com.example.iurymiguel.androidchatapp.interfaces.ConnectionCallbacks
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.viewmodels.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    private lateinit var mViewModel: AuthenticationViewModel

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


    fun registerUser() {
        val user = User(
            editName.text.toString().trim(),
            editEmail.text.toString().trim(),
            editPassword.text.toString().trim()
        )
        mViewModel.registerUser(user, object : ConnectionCallbacks {
            override fun <T> onSuccessConnection(args: T) {
                Toast.makeText(context, "Sucesso ao registrar", Toast.LENGTH_SHORT).show()
            }

            override fun <T> onFailedConnection(args: T) {
                Toast.makeText(context, "Erro ao registrar", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
