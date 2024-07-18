package com.rosario.notetakingcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rosario.notetakingcompose.home.HomeScreen
import com.rosario.notetakingcompose.login.LoginScreen
import com.rosario.notetakingcompose.login.LoginViewModel
import com.rosario.notetakingcompose.login.SignUpScreen
import com.rosario.notetakingcompose.ui.theme.NoteTakingComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)

            val loginViewModel: LoginViewModel = viewModel()
            NoteTakingComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(loginViewModel = loginViewModel)
                }
            }
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

@Preview(showSystemUi = true)
@Composable
fun PrevSignupScreen() {
    NoteTakingComposeTheme {
        SignUpScreen(onNavToHomePage = { /*TODO*/ }) {

        }
    }
}