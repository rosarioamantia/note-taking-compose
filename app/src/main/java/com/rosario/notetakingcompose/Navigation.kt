package com.rosario.notetakingcompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rosario.notetakingcompose.login.LoginScreen
import com.rosario.notetakingcompose.login.LoginViewModel
import com.rosario.notetakingcompose.login.SignUpScreen

enum class LoginRoutes{
    Signup,
    Signin
}

enum class HomeRoutes{
    Home,
    Detail
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel
){
    NavHost(
        navController = navController,
        startDestination = LoginRoutes.Signin.name)
    {
        composable(route = LoginRoutes.Signin.name){
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavToHomePage = {
                    navController.navigate(HomeRoutes.Home.name) {
                        launchSingleTop = true
                        popUpTo(route = LoginRoutes.Signin.name){ //remove from the back-stack
                            inclusive = true
                        }
                    }
                },
                onNavToSignupPage = {
                    navController.navigate(LoginRoutes.Signup.name){
                        launchSingleTop = true
                        popUpTo(route = LoginRoutes.Signin.name){ //remove from the back-stack
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(route = LoginRoutes.Signup.name){
            SignUpScreen(
                loginViewModel = loginViewModel,
                onNavToHomePage = {
                    navController.navigate(HomeRoutes.Home.name){
                        launchSingleTop = true
                        popUpTo(route = LoginRoutes.Signup.name){
                            inclusive = true
                        }
                    }
                },
                onNavToLoginPage = {
                    navController.navigate(LoginRoutes.Signin.name){
                        launchSingleTop = true
                        popUpTo(route = LoginRoutes.Signup.name){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = HomeRoutes.Home.name){
            //HomeScreen()
        }
    }

}