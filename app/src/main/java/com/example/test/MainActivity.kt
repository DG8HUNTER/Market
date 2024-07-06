package com.example.test

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.TestTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

//val auth: FirebaseAuth = Firebase.auth
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)

        setContent {



            TestTheme {

                  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                val auth =Firebase.auth
                val currentUser = auth.currentUser
                val navController = rememberNavController()



               /* val timeThread = Thread {
                    while (true) {
                        mainActivityViewModel.setValue(LocalTime.now() , "currentTime")

                        Thread.sleep(1000) // Sleep for 1 second
                    }
                }
                timeThread.start()*/

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














