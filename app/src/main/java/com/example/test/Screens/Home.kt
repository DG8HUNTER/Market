package com.example.test.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Home(navController: NavController,currentUser:String){

  var userInfo :MutableMap<String, Any> by remember {
      mutableStateOf(
          hashMapOf(
          "userID" to "",
          "FirstName" to "",
          "LastName" to "",
          "Location" to "",
          "PhoneNumber" to ""

      )
      )
  }
    var location by remember{
        mutableStateOf(userInfo["Location"])
    }
    val db = Firebase.firestore


        db.collection("Users").document(currentUser)
            .get()
            .addOnSuccessListener {document->

                userInfo = document.data!! as MutableMap<String, Any>
                Log.d("user",document.data!!.toString())
            }




    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp).background(color= Color.Red) ){



        Text(text=userInfo["Location"].toString() , modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }



}