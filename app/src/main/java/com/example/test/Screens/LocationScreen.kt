package com.example.test.Screens

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.test.MainActivity
import com.example.test.R
import com.example.test.ui.theme.customGreen
import com.example.test.ui.theme.mediumGray
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

@Composable
fun LocationScreen(navController: NavController,fusedLocation:FusedLocationProviderClient){

    val context = LocalContext.current



    Column(modifier= Modifier
        .fillMaxSize()
        .padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {

        Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            Icon(painter = painterResource(id = R.drawable.location3d), contentDescription ="location Icon" , tint=Color.Unspecified, modifier=Modifier.size(320.dp))
            Text(text="Location Access", fontSize=20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier=Modifier.height(5.dp))
            Text(text="Location access need to be enabled to view stores around you", fontSize=16.sp , fontWeight = FontWeight.Medium,color= mediumGray,modifier=Modifier.fillMaxWidth(0.8f), textAlign = TextAlign.Center)
            Spacer(modifier=Modifier.height(15.dp))
            Button(onClick = { getLocation(fusedLocation ,context,navController
            )} , modifier = Modifier
                .fillMaxWidth(0.5f)
                .clip(shape=RoundedCornerShape(2.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )

                ,

                contentPadding = PaddingValues()


            ) {

                Box(modifier=Modifier.fillMaxWidth().height(55.dp)
                    .background(color= customGreen)
                    .clip(shape = RoundedCornerShape(2.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Enable " , fontWeight = FontWeight.Bold , color = Color.White , fontSize = 18.sp  )
                }


            }
        }

    }







}



private fun getLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context,
    navController: NavController

    ){
    //Check location permission
    if(ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED  &&
        ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION) , 100)
        return
    }
    val location = fusedLocationProviderClient.lastLocation
    location.addOnSuccessListener {
        if(it!=null){
           // mainActivityViewModel.setValue(it.longitude , "longitude")
          //  mainActivityViewModel.setValue(it.latitude , "latitude")
            val longitude=it.longitude
            val latitude=it.latitude
            AddressFromCoordinates(
                latitude =latitude ,
                longitude =longitude ,
                context = context,
                navController = navController

            )



        }
    }


}



fun AddressFromCoordinates(
    latitude: Double,
    longitude: Double,
    context: Context,
    navController: NavController

) {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
    if (addresses != null) {
        if(addresses.isEmpty()){
Toast.makeText(context,"Failed to get Location",Toast.LENGTH_LONG).show()
        }
        else{
            val userAddress = addresses[0].getAddressLine(0)
            val db= Firebase.firestore
            val auth = Firebase.auth
            val currentUser=auth.uid

            if (currentUser != null) {
                db.collection("Users").document(currentUser)
                    .update("Location",userAddress)
                    .addOnCompleteListener {task->
                        if(task.isSuccessful){

                            navController.navigate(route="Home/$currentUser")



                        }
                    }
            }

        }
    }





}