package com.example.test

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.example.test.ui.theme.TestTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import java.util.concurrent.TimeUnit
//val auth: FirebaseAuth = Firebase.auth
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)

        setContent {


            TestTheme {
                val auth =Firebase.auth
                val currentUser = auth.currentUser
                val navController = rememberNavController()

                if(currentUser!=null){
                    Navigation(navController = navController, startDestination ="WhiteScreen")
                    navController.navigate("UserCredentials/${currentUser.uid}"){
                        popUpTo(0)
                    }
                }
                else{
                    Navigation(navController = navController, startDestination ="SplashScreen")
                }













            }


        }
    }









}














