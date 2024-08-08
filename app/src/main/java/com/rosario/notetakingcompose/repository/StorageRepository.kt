package com.rosario.notetakingcompose.repository

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rosario.notetakingcompose.models.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Objects

const val NOTES_COLLECTION_REF = "notes"

class StorageRepository {
    val user = Firebase.auth.currentUser
    fun hasUser():Boolean = Firebase.auth.currentUser != null
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val notesReference:CollectionReference = Firebase
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
            trySend(Resources.Error(throwable = e.cause))
            e.printStackTrace()
        }

        awaitClose{
            snapshotStateListener?.remove() // removing listening of data
        }
    }

    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Note?) -> Unit
    ){
        notesReference
            .document(noteId)
            .get()
            .addOnSuccessListener{
                onSuccess.invoke(it.toObject(Note::class.java))
            }.addOnFailureListener{ e ->
                onError.invoke(e.cause)
            }
    }

    fun getNote(
        documentId: String
    ) : Flow<Resources<Note>> = callbackFlow{
        var snapshotStateListener: ListenerRegistration? = null

        try{
            snapshotStateListener = notesReference
                .addSnapshotListener{ snapshot, e ->
                    val response = if(snapshot != null){
                        val note = snapshot.toObjects(Note::class.java).single()
                        Resources.Success<Note>(data = note)
                    }else{
                        Resources.Error(throwable = e?.cause)
                    }

                }
        }catch(e: Exception){
            trySend(Resources.Error(throwable = e.cause))
            e.printStackTrace()
        }

        awaitClose{
            snapshotStateListener?.remove() // removing listening of data
        }
    }

    fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        colorIndex: Int = 0,
        onComplete: (Boolean) -> Unit
    ){
        val documentId = notesReference.document().id
        val note = Note(
            userId,
            title,
            description,
            timestamp,
            colorIndex,
            documentId)

        notesReference
            .document(documentId)
            .set(note) // add data or update if already exists
            .addOnCompleteListener{result ->
                onComplete.invoke(result.isSuccessful) //passing parameter to hh function
            }
    }

    fun deleteNote(
        noteId: String,
        onDelete: (Boolean) -> Unit
    ){
        notesReference
            .document(noteId)
            .delete()
            .addOnCompleteListener{ result ->
                onDelete.invoke(result.isSuccessful)
            }
    }

    fun updateNote(
        title: String,
        description: String,
        color: Int,
        noteId: String,
        onResult: (Boolean) -> Unit
    ){
        val updateData = hashMapOf<String, Any>(
            "colorIndex" to color,
            "description" to description,
            "title" to title
        )

        notesReference
            .document(noteId)
            .update(updateData)
            .addOnCompleteListener{ result ->
                onResult.invoke(result.isSuccessful)
            }
    }


}

// Resource class to help manage the state of getting the data
// used only inside class (above)
sealed class Resources<T>(
    val data:T? = null,
    val throwable: Throwable? = null,
){
    class Loading<T>: Resources<T>()
    class Success<T>(data: T?): Resources<T>(data = data)
    class Error<T>(throwable: Throwable?): Resources<T>(throwable = throwable)
}