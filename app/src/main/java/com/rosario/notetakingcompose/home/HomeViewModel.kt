package com.rosario.notetakingcompose.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.rosario.notetakingcompose.repository.AuthRepository

class HomeViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    val currentUser = repository.currentUser

    fun logout(){
        if(repository.hasUser()){
            repository.firebaseInstance.signOut()
        }
    }

}