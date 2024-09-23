package com.rosario.notetakingcompose.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rosario.notetakingcompose.models.Note
import com.rosario.notetakingcompose.repository.AuthRepository
import com.rosario.notetakingcompose.repository.Resources
import com.rosario.notetakingcompose.repository.StorageRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val storageRepository: StorageRepository = StorageRepository()
) : ViewModel() {
    var homeState by mutableStateOf(HomeState())

    val user = storageRepository.user()
    val hasUser: Boolean
        get() = storageRepository.hasUser() //custom get() -> ricalcolato nuovamente ogni volta che viene letto (doc section: Properties)
    private val userId: String
        get() = storageRepository.getUserId()

    fun loadNotes(){
        if(hasUser){
            if(userId.isNotBlank()){
                getUserNotes(userId)
            }
        }else{
            homeState = homeState.copy(notesList = Resources.Error(
                    throwable = Throwable(message = "User is not logged")
                )
            )
        }
    }

    private fun getUserNotes(userId: String) = viewModelScope.launch{
        storageRepository.getUserNotes(userId).collect{
            homeState = homeState.copy(notesList = it)
        }
    }

    fun deleteNote(noteId: String) = storageRepository.deleteNote(noteId){
        homeState = homeState.copy(noteDeletedStatus = it)
    }

    fun signOut() = storageRepository.signOut()


}

data class HomeState(
    val notesList: Resources<List<Note>> = Resources.Loading(),
    val noteDeletedStatus: Boolean = false
)