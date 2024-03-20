package com.example.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.test.Screens.ResetPassword
import com.example.test.Screens.SignInScreen
import com.example.test.Screens.SignUpScreen
import com.example.test.Screens.SplashScreen
import com.example.test.Screens.UserCredential
import com.example.test.Screens.WelcomeScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable

fun Navigation(navController: NavController,startDestination:String) {

    NavHost(
        navController = navController as NavHostController,
        startDestination = startDestination
    ) {

        composable(route = "SplashScreen") {
            SplashScreen(navController = navController)
        }
        composable(route = "WhiteScreen") {

        }

        composable(route = "WelcomeScreen") {

            WelcomeScreen(navController = navController)

        }

        composable(route = "SignUpScreen") {
            SignUpScreen(navController = navController)


        }

        composable(route = "SignInScreen") {
            SignInScreen(navController = navController)


        }
        composable(route = "ResetPassword") {
            ResetPassword(navController = navController)
        }
        composable(route = "UserCredentials/{userUID}", arguments = listOf(
            navArgument(name = "userUID") {
                type = NavType.StringType
                nullable = false
            }
        )) {

                backStackEntry ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(text = backStackEntry.arguments?.get("userUID").toString())
                Button(onClick = {
                    Firebase.auth.signOut()
                    navController.navigate(route = "WelcomeScreen") {
                        popUpTo(0)


                    }
                }) {
                    Text(text = "Sign Out")


                }
            }


        }

        composable(route = "PersonalInfo") {
            UserCredential(navController)

        }
        composable(route="LocationScreen"){
            Text(text="Location Screen")
        }

    }
}
