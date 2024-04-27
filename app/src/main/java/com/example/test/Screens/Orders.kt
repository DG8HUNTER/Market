package com.example.test.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.StoreInOrders
import com.example.test.R
import com.example.test.ui.theme.customColor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.A


@Composable
fun Orders(navController: NavController){
   // var storesId :MutableSet<String> = mutableSetOf()
    var storesData :MutableList<HashMap<String, Any>> by remember {
       mutableStateOf( mutableListOf())
    }


    val db = Firebase.firestore

    var isLoading :Boolean by remember {
        mutableStateOf(true)
    }

    Log.d("orders",mainActivityViewModel.orders.value.toString())
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 =true, mainActivityViewModel.orders.value) {

        scope.launch(Dispatchers.Default){
            val sData :MutableList<HashMap<String,Any>> = mutableListOf()
            var storesId :MutableSet<String> = mutableSetOf()
            if(mainActivityViewModel.orders.value.size==0){
                storesId= mutableSetOf()
            }else {
                for(doc in mainActivityViewModel.orders.value){
                    storesId.add(doc["storeId"].toString())

                }


                Log.d("id",storesId.toString())
                for(doc in storesId){


                    val store = db.collection("Stores").document(doc).get().await()
                    if(store!=null){
                        sData.add(store.data as HashMap<String,Any>)
                    }

                }



            }

            withContext(Dispatchers.Main){
                storesData=sData
                Log.d("storesData" , storesData.toString())
                delay(1000)
                isLoading=false
            }



        }

    }

    Column(modifier=Modifier.fillMaxSize().padding(20.dp)){

        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(text = "My Orders", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        }

        if(!isLoading){
            if(storesData.size!=0){

                LazyColumn(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp), contentPadding = PaddingValues(vertical = 10.dp)){
                    for(store in storesData){
                        var totalOrders = 0
                        var currentOrders=0
                        var pastOrders=0
                        var data :HashMap<String,Any> = hashMapOf()

                        for(order in mainActivityViewModel.orders.value){
                            if(order["storeId"].toString()==store["storeId"].toString()){
                                totalOrders+=1
                                if(order["status"].toString()=="pending" || order["status"].toString()=="processing"){
                                    currentOrders+=1
                                }
                                else {
                                    pastOrders+=1
                                }
                            }

                        }

                        data = hashMapOf(
                            "storeId"  to store["storeId"].toString(),
                            "image"  to store["image"].toString(),
                            "name"  to store["name"].toString(),
                            "location" to store["location"].toString(),
                            "totalOrders"  to totalOrders,
                            "currentOrders"  to currentOrders,
                            "pastOrders"  to pastOrders

                        )


                        item {
                            StoreInOrders(navController=navController,data=data)
                        }







                    }

                }






            }
            else {
                Column(modifier= Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                    Image(painter = painterResource(id = R.drawable.noproductadded), contentDescription ="" , modifier= Modifier.size(170.dp))
                    Spacer(modifier= Modifier.height(5.dp))
                    Text(text ="No product Added yet" , fontWeight = FontWeight.Bold , fontSize = 16.sp)

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
                        text="Loading your orders",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )


                }
            }


        }


    }









}