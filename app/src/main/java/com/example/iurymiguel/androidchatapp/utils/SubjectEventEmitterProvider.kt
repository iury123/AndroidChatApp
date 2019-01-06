package com.example.iurymiguel.androidchatapp.utils

import com.example.iurymiguel.androidchatapp.model.Subject

class SubjectEventEmitterProvider {

    private var mOnSubjectChangedCallback: ((Subject) -> Unit)? = null
    private var mOnSubjectDeletedCallback: ((Subject) -> Unit)? = null

    /**
     * Listens to message changing event.
     */
    fun onSubjectChanged(callback: (Subject) -> Unit) {
        mOnSubjectChangedCallback = callback
    }

    /**
     * Publishes a events whenever a message is updated.
     * @param message the message changed.
     */
    fun publishSubjectChanged(subject: Subject) {
        mOnSubjectChangedCallback?.let {
            it(subject)
        }
    }

    /**
     * Listens to message deleting event.
     */
    fun onSubjectDeleted(callback: (Subject) -> Unit) {
        mOnSubjectDeletedCallback = callback
    }

    /**
     * Publishes a events whenever a message is deleted.
     * @param message the message changed.
     */
    fun publishSubjectDeleted(subject: Subject) {
        mOnSubjectDeletedCallback?.let {
            it(subject)
        }
    }

    /**
     * Removes all listeners.
     */
    fun removeAllListeners() {
        mOnSubjectChangedCallback = null
        mOnSubjectDeletedCallback = null
    }

}