package com.example.iurymiguel.androidchatapp.application

import android.app.Application
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.SubjectEventEmitterProvider
import com.example.iurymiguel.androidchatapp.utils.NetworkProvider
import com.example.iurymiguel.androidchatapp.utils.ProgressDialogProvider
import com.example.iurymiguel.androidchatapp.utils.TimeoutProvider
import com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters.ChatRecyclerAdapter
import com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters.SubscribersRecyclerAdapter
import com.example.iurymiguel.androidchatapp.views.subjects.recyclerAdapters.SubscribedSubjectsRecyclerAdapter
import com.example.iurymiguel.androidchatapp.views.subjects.recyclerAdapters.UnsubscribedSubjectsRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

class AndroidChatApplication : Application() {

    private val appModule = module {
        single { ProgressDialogProvider() }
        single { NetworkProvider(androidContext()) }
        single { UnsubscribedSubjectsRecyclerAdapter() }
        single { SubscribedSubjectsRecyclerAdapter() }
        single { SubscribersRecyclerAdapter() }
        single { ChatRecyclerAdapter() }
        single { User() }
        single { SubjectEventEmitterProvider() }
        single { TimeoutProvider() }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        startKoin(this, listOf(appModule))
    }
}