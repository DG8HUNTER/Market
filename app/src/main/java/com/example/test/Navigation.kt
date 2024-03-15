package com.example.test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test.Screens.SplashScreen
import com.example.test.Screens.WelcomeScreen


@Composable

fun Navigation(navController: NavController){

NavHost(navController =navController as NavHostController , startDestination = "SplashScreen") {

 composable(route = "SplashScreen"){
     SplashScreen(navController =navController )
 }

    composable(route="WelcomeScreen"){

        WelcomeScreen(navController=navController)

    }

    composable(route="SignUpScreen"){
        Text(text = "SignUpScreen")


    }




}



}