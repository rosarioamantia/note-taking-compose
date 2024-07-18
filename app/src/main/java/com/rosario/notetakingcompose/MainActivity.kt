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
import com.rosario.notetakingcompose.login.LoginScreen
import com.rosario.notetakingcompose.login.SignUpScreen
import com.rosario.notetakingcompose.ui.theme.NoteTakingComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteTakingComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(onNavToHomePage = { /*TODO*/ }) {

                    }
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