package com.rosario.notetakingcompose.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    val currentUser: FirebaseUser? = Firebase.auth.currentUser

    //check if User logged or not
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    //manage of null value with orEmpty
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    suspend fun createUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{

            }
    }
}