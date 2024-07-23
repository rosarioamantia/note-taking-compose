package com.rosario.notetakingcompose.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rosario.notetakingcompose.models.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val NOTES_COLLECTION_REF = "notes"

class StorageRepository {
    val user = Firebase.auth.currentUser
    fun hasUser():Boolean = Firebase.auth.currentUser != null
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val notesReference: CollectionReference = Firebase
        .firestore.collection(NOTES_COLLECTION_REF)

    // good coroutines usage example
    fun getUserNotes(
        userId: String
    ): Flow<Resources<List<Note>>> = callbackFlow { // coroutines
        var snapshotStateListener: ListenerRegistration? = null // a reference to Firestore listener

        try {
            snapshotStateListener = notesReference
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener{ snapshot, e ->
                    val response = if(snapshot != null){
                        val notes = snapshot.toObjects(Note::class.java)
                        Resources.Success(data = notes)
                    }else{
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response) //try to send response to Flow
                }

        }catch(e: Exception){
            trySend(Resources.Error(throwable = e?.cause))
            e.printStackTrace()
        }

        awaitClose{
            snapshotStateListener?.remove()
        }
    }
}

// Resource class to help manage the state of getting the data
// used only inside class (up)
sealed class Resources<T>(
    val data:T? = null,
    val throwable: Throwable? = null,
){
    class Loading<T>: Resources<T>()
    class Success<T>(data: T?): Resources<T>(data = data)
    class Error<T>(throwable: Throwable?): Resources<T>(throwable = throwable)
}