package com.example.test.Screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.example.test.ui.theme.customColor
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import com.example.test.ui.theme.lightGray2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.A


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
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(animation = tween(4000, easing = FastOutSlowInEasing)),
        label = ""
    )
    val brush =
        Brush.linearGradient(
            colors = listOf(
                customColor,
                Color(
                    0xFFFFCCBC

                ),
            ), start = Offset.Zero, end = Offset(x = translateAnim.value, y = translateAnim.value)
        )


    val rotation = animateFloatAsState(
        targetValue = rotate, animationSpec = tween(2000, easing = FastOutSlowInEasing),
        label = ""

    )

    var isLoading by remember {
        mutableStateOf(true)
    }


    val auth = Firebase.auth

    db.collection("Users").document(currentUser)
        .get()
        .addOnSuccessListener { document ->

            userInfo = document.data!! as MutableMap<String, Any>

        }


    val dbRef = Firebase.firestore

    val focus = LocalFocusManager.current

    val scope = rememberCoroutineScope()

    var search :String? by remember{
        mutableStateOf(null)
    }



    var stores: MutableList<HashMap<String, Any?>> by remember {
       mutableStateOf(mutableListOf())
    }


            val storesRef = dbRef.collection("Stores")

            storesRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                   if(snapshot.documents.size!=0){
                       scope.launch(Dispatchers.Default){
                           val result :MutableList<HashMap<String,Any>> = mutableListOf()
                           if(snapshot.documents.size!=0){
                               for(doc in snapshot.documents){

                                   result.add(doc.data as HashMap<String,Any>)}

                           }

                           withContext(Dispatchers.Main){
                               mainActivityViewModel.setValue(result,"stores")

                               delay(1000)
                               isLoading=false
                           }






                       }
                  } else {
                       mainActivityViewModel.setValue(mutableListOf<HashMap<String,Any>>(),"stores")
                   }
                }
            }


    LaunchedEffect(key1 = mainActivityViewModel.stores.value) {
        scope.launch(Dispatchers.Default){
            var array :MutableList<HashMap<String,Any?>> = mutableListOf()
            if(search==null){
                if(mainActivityViewModel.stores.value.size!=0){
                    for(store in mainActivityViewModel.stores.value){
                        array.add(store)
                    }
                }

            }else{

                array= searchStore(search)
            }

            withContext(Dispatchers.Main){
                stores=array
                Log.d("stores" , stores.toString())
            }

        }

    }










    var clearIcon = animateColorAsState(targetValue =if(search!=null)Color.Black else Color.Transparent , label="" , animationSpec = tween(1000, easing = FastOutSlowInEasing))




    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val ordersRef = dbRef.collection("Orders")

    ordersRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null ) {
            if(snapshot.documents.size>0) {


                val list: MutableList<HashMap<String, Any>> = mutableListOf()

                for (doc in snapshot.documents) {
                    list.add(doc.data as HashMap<String, Any>)

                }


                mainActivityViewModel.setValue(list.toMutableList(), "orders")

            }else {

                mainActivityViewModel.setValue(mutableListOf<HashMap<String,Any>>(),"orders")
            }

        }

    }




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
                                color = customColor,
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
                        label = { Text(text = "My Shopping Card") },
                        badge = {Text(text = mainActivityViewModel.addToCardProduct.value.size.toString(), color = customColor , fontWeight = FontWeight.Medium)},
                        selected = false,
                        onClick = {
                            navController.navigate(route = "MyShoppingCardScreen") {

                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .background(color = Color.Transparent)
                            .fillMaxWidth(),
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.shoppingcard),
                                contentDescription = "Shopping Bag", modifier = Modifier.size(22.dp)

                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        )
                    )


                    NavigationDrawerItem(
                        label = { Text(text = "Orders") },
                        selected = false,
                        onClick = {
navController.navigate(route="Orders")
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



                }


            }
        }, modifier = Modifier
            .fillMaxHeight()
            .background(color = Color.Gray)
            .fillMaxWidth(),
        drawerState = drawerState
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
        ) { innerPadding ->

            Column(modifier= Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp) ){

                Box(modifier= Modifier
                    .fillMaxWidth()
                    .height(53.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(7.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(7.dp))
                    .clip(shape = RoundedCornerShape(7.dp))){

                    Row(modifier= Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),verticalAlignment = Alignment.CenterVertically){



                        OutlinedTextField(value = if(search!=null)search.toString() else "", onValueChange ={search=
                            it.ifEmpty { null }


                        } , modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(7.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(7.dp))
                            .shadow(elevation = 7.dp, shape = RoundedCornerShape(7.dp)), colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            cursorColor = customColor,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedPlaceholderColor = Color.LightGray
                        ), leadingIcon = {  Icon(imageVector =Icons.Filled.Search, contentDescription ="Search Icon" , modifier = Modifier.size(25.dp), tint = Color.Gray )},
                            trailingIcon = {
                                if(search!=null){
                                    IconButton(onClick = {
                                        search=null
                                        stores= mainActivityViewModel.stores.value.toMutableList()

                                    }) {
                                        Icon(imageVector =Icons.Filled.Clear, contentDescription ="Search Icon" , modifier = Modifier.size(20.dp), tint = clearIcon.value )
                                    }

                                }
                            },

                            placeholder = {Text(text="Search for any Store or Restaurant" , fontSize = 16.sp , fontWeight = FontWeight.Medium , color=Color.Gray)},
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focus.clearFocus()
                                    scope.launch(Dispatchers.Default){

                                        focus.clearFocus()
                                        val result = if(search!=null){
                                            searchStore(search)
                                        } else {
                                            mainActivityViewModel.stores.value
                                        }


                                        // Process the result
                                        // Note: You can handle UI updates here based on the result if needed
                                        withContext(Dispatchers.Main) {
                                            //mainActivityViewModel.setValue(result, "stores")
                                            stores=result
                                            Log.d("stores" , stores.toString())
                                        }

                                    }


                                    //run the query
                                }
                            )

                        )

                    }




                }

                Spacer(modifier=Modifier.height(15.dp))

                Box(modifier= Modifier
                    .width(120.dp)
                    .height(45.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(brush = brush),
                    contentAlignment = Alignment.Center
                    ){

                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(painter= painterResource(id = R.drawable.shoppingbasket), contentDescription ="" , tint = Color.White , modifier=Modifier.size(20.dp))
                        Spacer(modifier=Modifier.width(8.dp))
                        Text(text="Stores" , color = Color.White , fontSize = 16.sp , fontWeight = FontWeight.Medium)

                    }

                }
                Spacer(modifier=Modifier.height(20.dp))




                if(!isLoading){
                    if(stores.size>0){

                        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp) ,contentPadding = PaddingValues(vertical = 5.dp)){
                            items(items= stores){
                                    item->
                                if(item["isAuthorized"]==true){
                                Store(data =item, navController = navController)}
                            }

                        }}
                    else{
                        Column(modifier=Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){
                            Image(painter = painterResource(id = R.drawable.nostorefound) , contentDescription ="no store found" , modifier = Modifier.size(220.dp) )
                        }

                    }
                }else {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            CircularProgressIndicator(
                                modifier=Modifier.size(16.dp),
                                color= customColor,
                                strokeWidth = 2.dp

                            )
                            Spacer(modifier = Modifier.width(7.dp))

                            Text(
                                text="Loading stores",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )


                        }
                    }
                }



            }
        }






        }
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





@RequiresApi(Build.VERSION_CODES.O)
suspend fun
        searchStore(search:String?): MutableList<HashMap<String, Any?>> {

    val docRef = Firebase.firestore
    val storesList = mutableListOf<HashMap<String, Any?>>()

    if (search != null) {
        Log.d("inside1" , "1")
        val data = docRef.collection("Stores").whereEqualTo("name", search).get().await()
        Log.d("data" , data.toString())

        if (!data.isEmpty) {
            for(document in data){
                Log.d("res" , document.data.toString())
                storesList.add(document.data as HashMap<String, Any?>)
            }
        }
    } else {
        Log.d("inside2" , "2")
        val data = docRef.collection("Stores").get().await()
        Log.d("data", data.toString())

        for(document in data){
            storesList.add(document.data as HashMap<String, Any?>)
        }
    }

 //   mainActivityViewModel.setValue(storesList, "stores")

Log.d("storeList", storesList.toString())
    return storesList
}