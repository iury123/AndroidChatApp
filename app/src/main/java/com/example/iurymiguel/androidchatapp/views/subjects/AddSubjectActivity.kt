package com.example.iurymiguel.androidchatapp.views.subjects

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.iurymiguel.androidchatapp.R
import com.example.iurymiguel.androidchatapp.databinding.ActivityAddSubjectBinding
import com.example.iurymiguel.androidchatapp.interfaces.FirebaseConnectionCallbacks
import com.example.iurymiguel.androidchatapp.utils.ProgressDialogProvider
import com.example.iurymiguel.androidchatapp.utils.TimeoutProvider
import com.example.iurymiguel.androidchatapp.utils.Utils
import com.example.iurymiguel.androidchatapp.viewmodels.AddSubjectViewModel
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_add_subject.*
import org.koin.android.ext.android.inject

class AddSubjectActivity : AppCompatActivity() {

    private lateinit var mViewModel: AddSubjectViewModel
    private val mProgress: ProgressDialogProvider by inject()
    private val mContext = this
    private val mTimeout: TimeoutProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val binding = DataBindingUtil.setContentView<ActivityAddSubjectBinding>(this, R.layout.activity_add_subject)

        binding.activity = this

        mViewModel = ViewModelProviders.of(this).get(AddSubjectViewModel::class.java)

    }

    /**
     * Adds a new subject.
     */
    fun addNewSubject() {

        val newSubject = editNewSubject.text.toString().trim()

        if (newSubject.isEmpty()) {
            Utils.showToast(this, getString(R.string.insert_subject_name), Toast.LENGTH_SHORT)
            return
        }

        mProgress.show(this)

        mTimeout.startTimer {
            mProgress.dismiss()
            Utils.showToast(mContext, getString(R.string.timeout_warning), Toast.LENGTH_LONG)
        }

        mViewModel.addSubject(newSubject, object : FirebaseConnectionCallbacks {
            override fun <T> onSuccessConnection(args: Task<T>) {
                mTimeout.cancelTimer()
                mProgress.dismiss()
                Utils.showToast(mContext, getString(R.string.add_subject_success), Toast.LENGTH_SHORT)
                finish()
            }

            override fun <T> onFailedConnection(args: Task<T>) {
                mTimeout.cancelTimer()
                mProgress.dismiss()
                Utils.showToast(mContext, getString(R.string.add_subject_error), Toast.LENGTH_SHORT)
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
