package com.rosario.notetakingcompose.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosario.notetakingcompose.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
):ViewModel(){
    val currentUser = repository.currentUser

    val hasUser: Boolean
        get() = repository.hasUser()

    var loginState by mutableStateOf(LoginState()) // property
        private set // -> means that set method is only accessible from within this class

    fun onUserNameChange(userName: String){
        loginState = loginState.copy(userName = "userName")
    }

    fun onPasswordChange(password: String){
        loginState = loginState.copy(password = password)
    }

    fun onUserNameSignupChange(userNameSignup: String){
        loginState = loginState.copy(userNameSignUp = userNameSignup)
    }

    fun onPasswordSignupChange(passwordSignUp: String){
        loginState = loginState.copy(passwordSignUp = passwordSignUp)
    }

    fun onConfirmPasswordSignupChange(confirmPasswordSignUp: String){
        loginState = loginState.copy(confirmPasswordSignUp = confirmPasswordSignUp)
    }

    private fun validateLoginForm() =
        loginState.userName.isNotBlank() &&
                loginState.password.isNotBlank()

    private fun validateSignupForm() =
        loginState.userNameSignUp.isNotBlank() &&
                loginState.passwordSignUp.isNotBlank() &&
                loginState.confirmPasswordSignUp.isNotBlank()

    //create user with coroutines
    fun createUser(context: Context) = viewModelScope.launch{
        try{
            if(!validateSignupForm()){
                throw IllegalAccessException("email and password cannot be empty")
            }
            loginState = loginState.copy(isLoading = true)

            if(loginState.passwordSignUp !=
                loginState.confirmPasswordSignUp){
                throw IllegalArgumentException(
                    "Password do not match"
                )
            }
            loginState = loginState.copy(signUpError = null)
            repository.createUser(
                loginState.userNameSignUp,
                loginState.passwordSignUp
            ){ isSuccessful ->
                if(isSuccessful){
                    Toast.makeText(
                        context,
                        "Success Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginState = loginState.copy(isSuccessLogin = true)
                }else{
                    Toast.makeText(
                        context,
                        "Failed Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginState = loginState.copy(isSuccessLogin = false)
                }
            }

        }catch(e: Exception){
            loginState = loginState.copy(signUpError = e.localizedMessage)  // to extract message from exception
            e.printStackTrace()
        }finally{
            loginState = loginState.copy(isLoading = false)
        }

    }

    fun loginUser(context: Context) = viewModelScope.launch{
        try{
            if(!validateLoginForm()){
                throw IllegalAccessException("email and password cannot be empty")
            }
            loginState = loginState.copy(isLoading = true)
            loginState = loginState.copy(loginError = null)
            repository.loginUser(
                loginState.userName,
                loginState.password
            ){ isSuccessful ->
                if(isSuccessful){
                    Toast.makeText(
                        context,
                        "Success Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginState = loginState.copy(isSuccessLogin = true)
                }else{
                    Toast.makeText(
                        context,
                        "Failed Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginState = loginState.copy(isSuccessLogin = false)
                }
            }

        }catch(e: Exception){
            loginState = loginState.copy(loginError = e.localizedMessage) // to extract message from exception
            e.printStackTrace()
        }finally{
            loginState = loginState.copy(isLoading = false)
        }

    }
}