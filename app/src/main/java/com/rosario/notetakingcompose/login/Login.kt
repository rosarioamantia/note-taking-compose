package com.rosario.notetakingcompose.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage:() -> Unit,
    onNavToSignupPage:() -> Unit,
){
        val loginState = loginViewModel?.loginState
}