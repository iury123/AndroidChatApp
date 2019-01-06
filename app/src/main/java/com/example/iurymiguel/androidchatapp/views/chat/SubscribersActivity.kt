package com.example.iurymiguel.androidchatapp.views.chat

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.model.User
import com.example.iurymiguel.androidchatapp.utils.SubjectEventEmitterProvider
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.SubscribersViewModel
import com.example.iurymiguel.androidchatapp.views.chat.recyclerAdapters.SubscribersRecyclerAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_subscribers.*
import org.koin.android.ext.android.inject

private const val INDEX_OUT_OF_BOUNDS = -1

class SubscribersActivity : AppCompatActivity() {

    private lateinit var mViewModel: SubscribersViewModel
    private val mSubjectEventEmitter: SubjectEventEmitterProvider by inject()
    private val mAdapter: SubscribersRecyclerAdapter by inject()
    private val mChildEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            val user = buildUserObject(dataSnapshot)
            mViewModel.mAllUsers += user
            filterSubscribedUsers(user)
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            val user = buildUserObject(dataSnapshot)
            mViewModel.mAllUsers += user
            filterSubscribedUsers(user)
        }

        override fun onChildRemoved(p0: DataSnapshot) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribers)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProviders.of(this).get(SubscribersViewModel::class.java)

        mViewModel.mSubject = intent.getSerializableExtra(getString(R.string.subject)) as Subject

        mAdapter.setDataSet(mViewModel.mSubscribedUsersList)

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        addChildEventListener()
        listenSubjectEvents()
    }


    override fun onDestroy() {
        super.onDestroy()
        removeChildEventListener()
        mSubjectEventEmitter.removeOnSubjectSubscribersChangedCallback()
    }


    /**
     * Listen to any event related to the list of subjects.
     */
    private fun addChildEventListener() {
        mViewModel.mUsersReference.addChildEventListener(mChildEventListener)
    }

    /**
     * Remove the listener which listens any list event.
     */
    private fun removeChildEventListener() {
        mViewModel.mUsersReference.addChildEventListener(mChildEventListener)
    }

    /**
     * Builds user object
     * @param dataSnapshot constains user data
     * @return user object.
     */
    private fun buildUserObject(dataSnapshot: DataSnapshot): User {
        val key = dataSnapshot.key as String
        val value = dataSnapshot.value as HashMap<*, *>
        val name = value[Utils.USER_NAME] as String
        val email = value[Utils.USER_EMAIL] as String
        val isOnline = value[Utils.USER_IS_ONLINE] as Boolean
        val user = User(key, name, email)
        user.isOnline = isOnline
        return user
    }

    /**
     * Check if user is subscribed.
     * @param user the user to be subscribed.
     */
    private fun filterSubscribedUsers(user: User) {
        mViewModel.mSubject.subscribers[user.key]?.let {
            mViewModel.mSubscribedUsersList += user
            mAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Adds a new user.
     * @param user the user.
     */
    operator fun MutableList<User>.plusAssign(user: User) {
        val index = this.indexInList(user)
        if(index > INDEX_OUT_OF_BOUNDS) {
            this[index] = user
        } else {
            this.add(user)
        }
    }

    /**
     * Removes a user.
     * @param user the user.
     */
    operator fun MutableList<User>.minusAssign(user: User) {
        val index = this.indexInList(user)
        if(index > INDEX_OUT_OF_BOUNDS) {
            this.removeAt(index)
        }
    }

    /**
     * Checks if user is in list.
     * @param user the user.
     */
    private fun MutableList<User>.indexInList(user: User): Int {
        val usersKeys = this.map { it.key }
        return usersKeys.indexOf(user.key)
    }

    /**
     * Listen to any changes about the current subject.
     */
    private fun listenSubjectEvents() {
        mSubjectEventEmitter.onSubjectSubscribersChanged {
            mViewModel.mSubject = it
            mViewModel.mSubscribedUsersList.clear()
            for(user in mViewModel.mAllUsers) {
                if(it.subscribers[user.key] != null) {
                    mViewModel.mSubscribedUsersList += user
                } else {
                    mViewModel.mSubscribedUsersList -= user
                }
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
