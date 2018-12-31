package com.example.iurymiguel.androidchatapp.application

import android.app.Application
import android.content.Intent
import com.example.iurymiguel.androidchatapp.utils.ProgressDialogProvider
import com.example.iurymiguel.androidchatapp.views.subjects.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class AndroidChatApplication : Application() {

    private val appModule = module {
        single { ProgressDialogProvider() }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        startKoin(this, listOf(appModule))
        val auth = FirebaseAuth.getInstance().currentUser
        auth?.let {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}