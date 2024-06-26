package com.rosario.notetakingcompose.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rosario.notetakingcompose.ui.theme.NoteTakingComposeTheme

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage:() -> Unit,
    onNavToSignupPage:() -> Unit,
){
    val loginState = loginViewModel?.loginState
    val isError = loginState?.loginError != null
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
           text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.error
        )

        if(isError){
            Text(
                text = loginState?.loginError ?: "Unknown Error", //Elvis operator
                color = Color.Red
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen() {
    NoteTakingComposeTheme {
        LoginScreen(onNavToHomePage = { /*TODO*/ }) {

        }
    }
}