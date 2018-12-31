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


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: SubjectsViewModel
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val mChildEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            val key = dataSnapshot.key
            val value = dataSnapshot.value as HashMap<*, *>
            val subjectKey = key as String
            val subjectName = value[Utils.SUBJECT_NAME] as String
            val subjectSubscribers =
                if (value[Utils.SUBSCRIBERS] != null) value[Utils.SUBSCRIBERS]
                else hashMapOf<String, String>()
            val subject = Subject(subjectKey, subjectName, subjectSubscribers as HashMap<String, String>)
            filterSubjectsLists(subject)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
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
        addListChildEventListener()
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
     * Filters each subject if the current user is subscribed or not.
     */
    private fun filterSubjectsLists(subject: Subject) {
        val userId = mViewModel.mAuth.currentUser!!.uid
        subject.subscribers[userId]?.let {
            mViewModel.getSubscribedSubjectsLiveData() += subject
        } ?: run {
            mViewModel.getUnsubscribedSubjectsLiveData() += subject
        }
    }

    /**
     * Adds a new item in the list.
     * @param subject the new subject to be inserted.
     */
    operator fun MutableLiveData<MutableList<Subject>>.plusAssign(subject: Subject) {
        val value = this.value ?: mutableListOf()
        val subjectKeys = value.map { it.key }
        val subjectKeyFound = subjectKeys.find { it == subject.key }
        subjectKeyFound?.let {
            value[subjectKeys.indexOf(it)] = subject
        } ?: value.add(subject)
        this.value = value
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
