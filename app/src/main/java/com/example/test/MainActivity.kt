package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.theme.TestTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

//val auth: FirebaseAuth = Firebase.auth
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)

        setContent {



            TestTheme {

                  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                val auth =Firebase.auth
                val currentUser = auth.currentUser
                val navController = rememberNavController()

                if(currentUser!=null){
                    Navigation(
                        navController = navController,
                        startDestination ="WhiteScreen",
                        fusedLocation = fusedLocationProviderClient
                    )
                    navController.navigate("Home/${currentUser.uid}"){
                        popUpTo(0)
                    }
                }
                else{
                    Navigation(navController = navController, startDestination ="SplashScreen",fusedLocation=fusedLocationProviderClient)
                }













            }


        }
    }









}














