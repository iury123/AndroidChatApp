package com.example.iurymiguel.androidchatapp.views.subjects

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.Menu
import android.view.MenuItem
import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.model.Subject
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.SubjectsViewModel
import com.example.iurymiguel.androidchatapp.views.authentication.AuthenticationActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.arch.lifecycle.MutableLiveData
import com.example.iurymiguel.androidchatapp.model.User
import com.google.firebase.database.ValueEventListener


import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

private const val INDEX_OUT_OF_BOUNDS = -1

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: SubjectsViewModel
    private val mCurrentUser: User by inject()
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val mChildEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            val subject = buildSubjectObject(dataSnapshot)
            handleSubjectInLists(subject)
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            val subject = buildSubjectObject(dataSnapshot)
            addSubject(subject)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            val subject = buildSubjectObject(dataSnapshot)
            removeSubject(subject)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        viewPager.adapter = mSectionsPagerAdapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        fab.setOnClickListener {
            val intent = Intent(this, AddSubjectActivity::class.java)
            startActivity(intent)
        }

        mViewModel = ViewModelProviders.of(this).get(SubjectsViewModel::class.java)
        mViewModel.mSubjectsReference.keepSynced(true)
        addListChildEventListener()
        retreiveCurrentUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListChildEventListener()
    }


    /**
     * Listen to any event related to the list of subjects.
     */
    private fun addListChildEventListener() {
        mViewModel.mSubjectsReference.addChildEventListener(mChildEventListener)
    }

    /**
     * Remove the listener which listens any list event.
     */
    private fun removeListChildEventListener() {
        mViewModel.mSubjectsReference.removeEventListener(mChildEventListener)
    }

    /**
     * Builds a subject object given the dataSnapshot retreived in the callback.
     * @param dataSnapshot the data about the returned subject.
     */
    private fun buildSubjectObject(dataSnapshot: DataSnapshot): Subject {
        val subjectKey = dataSnapshot.key as String
        val value = dataSnapshot.value as HashMap<*, *>
        val subjectName = value[Utils.SUBJECT_NAME] as String
        val subjectSubscribers = value[Utils.SUBSCRIBERS] ?: hashMapOf<String, String>()
        return Subject(subjectKey, subjectName, subjectSubscribers as HashMap<String, String>)
    }

    /**
     * Adds subject in subscribed or unsubscribed list.
     */
    private fun addSubject(subject: Subject) {
        val userId = mViewModel.mAuth.currentUser!!.uid
        subject.subscribers[userId]?.let {
            mViewModel.getSubscribedSubjectsLiveData() += subject
        } ?: run {
            mViewModel.getUnsubscribedSubjectsLiveData() += subject
        }
    }

    /**
     * Removes subject from subscribed or unsubscribed ones.
     */
    private fun removeSubject(subject: Subject) {
        val userId = mViewModel.mAuth.currentUser!!.uid
        subject.subscribers[userId]?.let {
            mViewModel.getSubscribedSubjectsLiveData() -= subject
        } ?: run {
            mViewModel.getUnsubscribedSubjectsLiveData() -= subject
        }
    }

    /**
     * Adds a new item in the list.
     * @param subject the new subject to be inserted.
     */
    operator fun MutableLiveData<MutableList<Subject>>.plusAssign(subject: Subject) {
        val value = this.value ?: mutableListOf()
        val index = this.indexOf(subject)
        when (index > INDEX_OUT_OF_BOUNDS) {
            true -> value[index] = subject
            else -> value.add(subject)
        }
        this.value = value
    }


    /**
     * Removes a item from the list.
     * @param subject the new subject to be inserted.
     */
    operator fun MutableLiveData<MutableList<Subject>>.minusAssign(subject: Subject) {
        val value = this.value ?: mutableListOf()
        val index = this.indexOf(subject)
        if (index > INDEX_OUT_OF_BOUNDS) {
            value.removeAt(index)
            this.value = value
        }
    }

    /**
     * Handles subject in both lists when the user subscribes or unsubscribes a subject.
     * @param subject the subject selected by user.
     */
    private fun handleSubjectInLists(subject: Subject) {
        val userId = mViewModel.mAuth.currentUser!!.uid
        subject.subscribers[userId]?.let {
            mViewModel.getUnsubscribedSubjectsLiveData() -= subject
            mViewModel.getSubscribedSubjectsLiveData() += subject
        } ?: run {
            mViewModel.getSubscribedSubjectsLiveData() -= subject
            mViewModel.getUnsubscribedSubjectsLiveData() += subject
        }
    }

    /**
     * Checks if a subject exists in list.
     * @param subject the subject selected by user.
     */
    private fun MutableLiveData<MutableList<Subject>>.indexOf(subject: Subject): Int {
        val value = this.value ?: mutableListOf()
        val subjectKeys = value.map { it.key }
        return subjectKeys.indexOf(subject.key)
    }


    /**
     * Retreives the current user name.
     */
    private fun retreiveCurrentUser() {
        val userId = mViewModel.mAuth.currentUser!!.uid
        val currentUserReference = mViewModel.mUsersReferences.child(userId)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value as HashMap<*, *>
                mCurrentUser.key = dataSnapshot.key as String
                mCurrentUser.name = value[Utils.USER_NAME] as String
                mCurrentUser.email = value[Utils.USER_EMAIL] as String
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        currentUserReference.addListenerForSingleValueEvent(valueEventListener)
    }

    /**
     * Creates the options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Handles selection events of options menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_logout) {
            mViewModel.logout()
            val intent = Intent(this, AuthenticationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem--
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val twoFragments = 2

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SubscribedSubjectsFragment.newInstance()
                else -> UnsubscribedSubjectsFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return twoFragments
        }
    }
}
