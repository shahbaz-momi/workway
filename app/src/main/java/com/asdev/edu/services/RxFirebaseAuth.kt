package com.asdev.edu.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import io.reactivex.Observable

object RxFirebaseAuth {

    fun getToken() =
        Observable.create<String> {
            emitter ->
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            if(user == null) {
                emitter.onError(IllegalStateException("No user signed in to retrieve token for"))
                return@create
            }

            val lock = java.lang.Object()

            // add cancellation by notifying the sleeping thread
            emitter.setCancellable {
                synchronized(lock) {
                    lock.notify()
                }
            }

            val task = user.getToken(true)
            // wait for task to complete
            task.await(lock)

            // if successful, trigger on next
            // otherwise error it
            if(task.isSuccessful && task.isComplete) {
                val token = task.result.token
                if(token != null) {
                    emitter.onNext(token)
                    emitter.onComplete()
                } else {
                    emitter.onError(IllegalStateException("Unable to retrieve token"))
                }
            } else {
                emitter.onError(task.exception?: Exception("An exception occurred while getting the user token"))
            }
        }

}

fun <T> Task<T>.await(lock: java.lang.Object) {

    addOnCompleteListener {
        // notify the thread of the lock release
        synchronized(lock) {
            lock.notify()
        }
    }

    // stall this tread on the lock
    if(!isComplete) {
        synchronized(lock) {
            lock.wait()
        }
    }
}