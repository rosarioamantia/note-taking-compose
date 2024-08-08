package com.rosario.notetakingcompose.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.rosario.notetakingcompose.repository.AuthRepository
import com.rosario.notetakingcompose.repository.StorageRepository

class HomeViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val storageRepository: StorageRepository = StorageRepository()
) : ViewModel() {
    val currentUser = authRepository.currentUser

    fun logout(){
        if(authRepository.hasUser()){
            authRepository.firebaseInstance.signOut()
        }
    }


    //chiamare metodi del repo per settare gli attributi dello stato in modo asincrono con le coroutines

}