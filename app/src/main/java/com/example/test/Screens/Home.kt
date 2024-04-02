package com.example.test.Screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.Component.Store
import com.example.test.R
import com.example.test.ui.theme.customGreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MutableCollectionMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController,
    currentUser: String,
    fusedLocation: FusedLocationProviderClient
) {

    var userInfo: MutableMap<String, Any> by remember {
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
    var location by remember {
        mutableStateOf(userInfo["Location"])
    }
    val db = Firebase.firestore

    var showMenu by remember {

        mutableStateOf(false)
    }
    val context = LocalContext.current


    var rotate by remember {
        mutableFloatStateOf(0f)
    }

    val rotation = animateFloatAsState(
        targetValue = rotate, animationSpec = tween(2000, easing = FastOutSlowInEasing),
        label = ""

    )


    val auth = Firebase.auth

    db.collection("Users").document(currentUser)
        .get()
        .addOnSuccessListener { document ->

            userInfo = document.data!! as MutableMap<String, Any>
            Log.d("user", document.data!!.toString())
        }


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(10.dp), verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { scope.launch { drawerState.close() } }) {
                                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                            }
                            Spacer(modifier = Modifier.width(3.dp))

                            Text(
                                text = "Market",
                                fontWeight = FontWeight.Bold,
                                color = customGreen,
                                fontFamily = FontFamily.Serif,
                                fontSize = 25.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row() {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Profile",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = {
                                navController.navigate(route = "PersonalInfo/Home") {

                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Gray
                                )

                            }


                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .height(1.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clip(shape = RoundedCornerShape(5.dp))
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Name: ${userInfo["FirstName"]} ${userInfo["LastName"]}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Email: ${auth.currentUser?.email.toString()} ",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Phone Number : +961-${userInfo["PhoneNumber"]}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(1.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                            .clip(shape = RoundedCornerShape(5.dp))
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Orders") },
                        selected = false,
                        onClick = {
                            navController.navigate(route = "Orders") {

                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .background(color = Color.Transparent)
                            .fillMaxWidth(),
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.shoppingbasket),
                                contentDescription = "Shopping Bag", modifier = Modifier.size(22.dp)

                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        )
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Refresh Location") },
                        selected = false,
                        onClick = {
                            rotate += 720f
                            getLocation(
                                fusedLocation, context, navController, screen = "Home"
                            )
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .background(color = Color.Transparent)
                            .fillMaxWidth(),
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.refreshlocation),
                                contentDescription = "Shopping Bag", modifier = Modifier.size(22.dp)

                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        )
                    )


                    NavigationDrawerItem(
                        label = { Text(text = "Change Password") },
                        selected = false,
                        onClick = { navController.navigate(route = "ChangePasswordSecurityScreen") },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.changepassword),
                                contentDescription = "change Password",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .background(color = Color.Transparent)
                            .fillMaxWidth(),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        )

                    )


                    NavigationDrawerItem(
                        label = { Text(text = "Log Out") }, selected = false,
                        onClick = {
                            auth.signOut()
                            navController.navigate("WelcomeScreen") {
                                popUpTo(0)
                            }
                        }, modifier = Modifier
                            .height(50.dp)
                            .background(color = Color.Transparent)
                            .fillMaxWidth(), icon = {
                            Icon(
                                painterResource(id = R.drawable.signoutfilled),
                                contentDescription = "Shopping Bag",
                                modifier = Modifier.size(22.dp),
                            )
                        }, colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        )
                    )


                    //  NavigationDrawerItem(label = { Text(text = "Hello") }, selected =false , onClick = { /*TODO*/ })
                }


            }
        }, modifier = Modifier
            .fillMaxHeight()
            .background(color = Color.Gray)
            .fillMaxWidth(), drawerState = drawerState
    ) {

        Scaffold(
            topBar = {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                painter = painterResource(id = R.drawable.sidemenu),
                                contentDescription = "Menu Icon",
                                modifier = Modifier.size(40.dp)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.width(15.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,

                        ) {
                        Text(
                            text = "Shops In",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Text(
                            text = userInfo["Location"].toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                    }
                    IconButton(onClick = {
                        rotate += 720f
                        getLocation(fusedLocation, context, navController, screen = "Home")


                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(22.dp)
                                .rotate(
                                    rotation.value
                                )
                        )

                    }


                }


            },
            modifier = Modifier.fillMaxSize(),
        ) {innerPadding->
            val size= mainActivityViewModel.stores.value.size



LaunchedEffect(true){


                val dbRef = Firebase.firestore
                dbRef.collection("Stores").get()
                    .addOnSuccessListener { documents ->
                        if (documents != null) {
                            for (document in documents) {

                                Log.d("document", document.data.toString())
                                mainActivityViewModel.addToStores(newValue = document.data as HashMap<String, Any>)
                                Log.d("stores", mainActivityViewModel.stores.value.toString())
                            }
                        }


                    }}


         Column(modifier= Modifier
             .fillMaxSize()
             .padding(innerPadding)){

             if(mainActivityViewModel.stores.value.size!=0) {
                 LazyColumn(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                     items(items= mainActivityViewModel.stores.value){
                         item->
                         Store(data=item)
                     }
                 }
             }





         }




        }
    }


    /*        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)


        ){

            Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Column(verticalArrangement =Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

                    IconButton(onClick = {DrawerValue.Open }) {
                        Icon(painter= painterResource(id = R.drawable.sidemenu), contentDescription = "Menu Icon" , modifier=Modifier.size(40.dp))
                    }

                }

                Spacer(modifier=Modifier.width(15.dp))
                Column(modifier=Modifier.fillMaxWidth() ,
                    verticalArrangement = Arrangement.Center ,
                    horizontalAlignment = Alignment.Start){
                    Text(text="Shops In" , fontSize =14.sp , fontWeight = FontWeight.Medium, color = Color.Gray)
                    Text(text=userInfo["Location"].toString() , fontWeight = FontWeight.Bold, fontSize = 20.sp)

                }





            }

            if(showMenu)

                Box(modifier=Modifier.fillMaxSize()){


                }







        }*/

}
    @Composable
    fun CoilImage() {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("F1/mercedesF1.jpg")
        val url = imageRef.downloadUrl
        Log.d("ref", imageRef.toString())



        Box(modifier = Modifier.size(200.dp)) {
            val painter = rememberImagePainter(
                data = "https://firebasestorage.googleapis.com/v0/b/ouvatech.appspot.com/o/060947f4-1a7a-425b-9cee-e755f4705e9d.jpeg?alt=media&token=09967a52-a1ea-4dd9-ab81-105f8cdea0ae",
                builder = {})

            Image(painter = painter, contentDescription = null, modifier = Modifier.fillMaxSize())
        }
    }
