package com.example.iurymiguel.androidchatapp.application

import android.app.Application
import com.example.iurymiguel.androidchatapp.utils.ProgressDialogProvider
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class AndroidChatApplication : Application() {

    val appModule = module {
        single { ProgressDialogProvider() }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        startKoin(this, listOf(appModule))
    }
}