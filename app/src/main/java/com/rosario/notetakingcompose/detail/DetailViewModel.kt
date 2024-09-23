package com.rosario.notetakingcompose.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.*
import com.rosario.notetakingcompose.models.Note
import com.rosario.notetakingcompose.repository.Resources
import com.rosario.notetakingcompose.repository.StorageRepository
import kotlinx.coroutines.flow.Flow

class DetailViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {

    // by = delegation: delegare la logica di gestione di una proprietà a un altro oggetto o funzione
    // mutableStateOf -> ritorna un MutableState<T>

    // gestione dello stato reattivo -> all'aggiornamento di detailState si
    // aggiornano automaticamente tutti i @Composable che la stanno osservando
    var detailState by mutableStateOf(DetailState())
        private set

    private val hasUser:Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    fun onColorChange(colorIndex: Int){
        detailState = detailState.copy(colorIndex = colorIndex)
    }

    fun onNoteChange(note: String){
        detailState = detailState.copy(note = note)
    }

    fun onTitleChange(title: String){
        detailState = detailState.copy(title = title)
    }

    fun addNote(
        title: String,
        description: String,
        colorIndex: Int,
        onComplete: (Boolean) -> Unit
    ){
        if(hasUser){
            repository.addNote(
                userId = user!!.uid,
                title = detailState.title,
                description = detailState.note,
                colorIndex = detailState.colorIndex,
                timestamp = Timestamp.now()
            ){
                detailState = detailState.copy(noteAddedStatus = it)
            }
        }
    }

    // da capire
    fun setEditFields(note: Note){
        detailState = detailState.copy(
            colorIndex = note.colorIndex,
            title = note.title,
            note = note.description
        )
    }

    fun getNote(noteId: String) {
        repository.getNote(
            noteId = noteId,
            onError = {},
        ){
           detailState = detailState.copy(selectedNote = it)
           detailState.selectedNote?.let{ it1 -> setEditFields(it1) }
        }
    }

    fun updateNote(
        noteId: String
    ){
        repository.updateNote(
            title = detailState.title,
            description = detailState.note,
            noteId = noteId,
            color = detailState.colorIndex
        ){
            detailState = detailState.copy(updateNoteStatus = it)
        }
    }

    fun resetNoteAddedStatus(){
        detailState = detailState.copy(
            noteAddedStatus = false,
            updateNoteStatus = false
        )
    }

    fun resetState(){
        detailState = DetailState()
    }
}

data class DetailState(
    val colorIndex: Int = 0,
    val title: String = "",
    val note: String = "",
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedNote: Note? = null
)