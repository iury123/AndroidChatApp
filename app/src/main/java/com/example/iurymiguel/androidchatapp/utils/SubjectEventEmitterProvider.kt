package com.example.iurymiguel.androidchatapp.utils

import com.example.iurymiguel.androidchatapp.model.Subject

class SubjectEventEmitterProvider {

    private var mOnSubjectChangedCallback: ((Subject) -> Unit)? = null
    private var mOnSubjectDeletedCallback: ((Subject) -> Unit)? = null
    private var mOnSubjectSubscribersChangedCallback: ((Subject) -> Unit)? = null

    /**
     * Listens to subject changing event.
     * @param callback the callback to view.
     */
    fun onSubjectChanged(callback: (Subject) -> Unit) {
        mOnSubjectChangedCallback = callback
    }

    /**
     * Publishes a events whenever a subject is updated.
     * @param subject the subject changed.
     */
    fun publishSubjectChanged(subject: Subject) {
        mOnSubjectChangedCallback?.let {
            it(subject)
        }
        mOnSubjectSubscribersChangedCallback?.let {
            it(subject)
        }
    }

    /**
     * Listens to subject deleting event.
     * @param callback the callback to view.
     */
    fun onSubjectDeleted(callback: (Subject) -> Unit) {
        mOnSubjectDeletedCallback = callback
    }

    /**
     * Listens to any subject changings in SubscribersActivity class.
     * @param callback the callback to view.
     */
    fun onSubjectSubscribersChanged(callback: (Subject) -> Unit) {
        mOnSubjectSubscribersChangedCallback = callback
    }


    /**
     * Publishes a events whenever a subject is deleted.
     * @param subject the subject changed.
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

    /**
     * Removes the callback.
     */
    fun removeOnSubjectSubscribersChangedCallback() {
        mOnSubjectSubscribersChangedCallback = null
    }

}