package com.example.test

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.test.Screens.ChangePasswordScreen
import com.example.test.Screens.ChangePasswordSecurityScreen
import com.example.test.Screens.FavoriteItemsScreen
import com.example.test.Screens.Home
import com.example.test.Screens.LocationScreen
import com.example.test.Screens.OrderScreen
import com.example.test.Screens.PersonalInfo
import com.example.test.Screens.ResetPassword
import com.example.test.Screens.SignInScreen
import com.example.test.Screens.SignUpScreen
import com.example.test.Screens.SplashScreen
import com.example.test.Screens.StoreInfo
import com.example.test.Screens.StoreProfile
import com.example.test.Screens.WelcomeScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.checkerframework.checker.units.qual.N


@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun Navigation(
    navController: NavController,
    startDestination: String,
    fusedLocation: FusedLocationProviderClient
) {

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
     /*   composable(route = "ResetPassword") {
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


        }*/

        composable(route = "PersonalInfo/{screen}" , arguments=listOf(
            navArgument(name="screen"){
                type= NavType.StringType
                nullable=false
            }
        )) {
            backStackEntry->

            PersonalInfo(navController, screen=backStackEntry.arguments?.get("screen").toString())
        }
        composable(route="LocationScreen"){
           LocationScreen(navController = navController,fusedLocation=fusedLocation)

        }

        composable(route="Home/{currentUser}", arguments = listOf(
            navArgument(name ="currentUser"){
                type= NavType.StringType
                nullable=false
            }
        )){backStackEntry->
            Home(navController=navController,currentUser=backStackEntry.arguments?.get("currentUser").toString(), fusedLocation=fusedLocation)

        }

        composable(route="Orders"){
            OrderScreen(navController = navController)
        }

        composable(route="ChangePasswordSecurityScreen"){
            ChangePasswordSecurityScreen(navController=navController)

        }

        composable(route="ChangePasswordScreen"){
            ChangePasswordScreen(navController=navController)

        }

        composable(route="StoreInfoScreen/{storeId}/{storeName}", arguments = listOf(
            navArgument(name="storeId"){
                type= NavType.StringType
                nullable=false
            },
            navArgument(name="storeName"){
                type= NavType.StringType
                nullable=false
            },

        )){
            backStackEntry->
            StoreInfo(navController=navController, storeId = backStackEntry.arguments?.get("storeId") as String , storeName=backStackEntry.arguments?.get("storeName") as String )

        }

        composable(route="StoreProfile/{storeId}", arguments=listOf(
            navArgument(name="storeId"){
                type= NavType.StringType
                nullable=false
            }
        )){ backstackEntry->
            StoreProfile(navController=navController , storeId=backstackEntry.arguments?.get("storeId").toString())

        }

        composable(route="FavoriteItemsScreen/{storeId}/{storeName}" , arguments = listOf(
            navArgument(name="storeId"){
                type= NavType.StringType
                nullable=false
            }, navArgument(name="storeName"){
                type= NavType.StringType
                nullable=false
            }
        )){
            backStackEntry ->
            FavoriteItemsScreen(navController=navController,storeId = backStackEntry.arguments?.get("storeId").toString() , storeName = backStackEntry.arguments?.get("storeName").toString())
        }

    }
}

