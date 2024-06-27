package com.example.test.Screens

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.Order
import com.example.test.ui.theme.customColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable

  fun OrdersPerStore(navController: NavController, storeId: String) {
    var index by remember {
        mutableIntStateOf(0)
    }
    val scope= rememberCoroutineScope()
    val stores = remember { mainActivityViewModel.stores.value }
    val context = LocalContext.current
    var isLoading  by remember {
        mutableStateOf(true)
    }

    var currentOrder by remember {
        mutableIntStateOf(0)
    }

    var pastOrder by remember {
        mutableIntStateOf(0)
    }

    var storeOrders :MutableList<HashMap<String,Any>> by remember {
        mutableStateOf(mutableListOf())
    }




    val currentUser = Firebase.auth.currentUser?.uid.toString()

    val dbRef = Firebase.firestore

    val ordersRef = dbRef.collection("Orders").orderBy("createdAt",Query.Direction.DESCENDING)
    ordersRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(ContentValues.TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null ) {
            scope.launch(Dispatchers.Default){
                var past =0
                var current =0
                if (snapshot.documents.size == 0) {
                    mainActivityViewModel.setValue(mutableListOf<HashMap<String, Any>>(), "orders")
                    isLoading=false
                }
                else {

                    Log.d("snapshotSize" , snapshot.documents.size.toString())
                    val new :MutableList<HashMap<String,Any>> = mutableListOf()

                    for(document in snapshot.documents){
                        if(document!=null){
                            if(document.data!!["userId"].toString()==currentUser && document.data!!["storeId"].toString()==storeId){
                                new.add(document.data as HashMap<String,Any>)
                                if(document.data!!["status"].toString()=="pending" ||document.data!!["status"].toString()=="processing" || document.data!!["status"].toString()=="shipped" ){
                                    current+=1
                                }
                                else{
                                    past+=1
                                }
                            }


                        }
                    }
                    withContext(Dispatchers.Main){

                       // mainActivityViewModel.setValue(new , "orders")
                        storeOrders=new
                        pastOrder=past
                        currentOrder = current
                        delay(1000)
                        isLoading=false

                    }
                }

            }



        }
    }


    var optionSelected by remember {
        mutableStateOf("Current Orders")
    }



    /*LaunchedEffect(key1 = mainActivityViewModel.orders.value , optionSelected ) {



    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Arrow Back",
                        modifier = Modifier.size(22.dp)
                    )

                }
                Text(text = " My Orders", fontSize = 22.sp, fontWeight = FontWeight.Bold)


            }

            TabRow(
                selectedTabIndex = index,
                modifier = Modifier.fillMaxWidth(),
                contentColor = customColor,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[index])
                            .clip(shape = RoundedCornerShape(10.dp)),
                        color = customColor,
                        height = 4.dp
                    )
                }) {
                Tab(
                    selected = index == 0,
                    onClick = {
                        index = 0
                        optionSelected = "Current Orders"
                    },
                    selectedContentColor = customColor
                ) {
                    Text(
                        text = "Current Orders",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (index == 0) Color.Black else Color.Gray,
                        modifier = Modifier.padding(10.dp)
                    )

                }
                Tab(selected = index == 1,
                    onClick = {
                        index = 1
                        optionSelected = "Past Orders"
                    },
                    selectedContentColor = customColor
                ) {
                    Text(
                        text = "Past Orders",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (index == 1) Color.Black else Color.Gray,
                        modifier = Modifier.padding(10.dp)
                    )

                }

            }


        }


        if(!isLoading){

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {



                if(storeOrders.size!=0){

                    if(optionSelected=="Current Orders"){

                        if(currentOrder==0){

                            Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                                Text(text = "No $optionSelected yet", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                            }
                        }else {


                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(vertical = 15.dp),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                               storeOrders.forEach { order ->
                                    val storeData = getStore(order["storeId"].toString())

                                        if (order["status"] != "delivered" && order["status"] != "cancelled") {
                                            item {
                                                Order(navController = navController, orderData = order, storeName=storeData["name"].toString() , storeImage = storeData["image"].toString(),context=context)

                                            }
                                        }



                                }

                            }
                        }}else{
                            if(pastOrder==0){
                                Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                                    Text(text = "No $optionSelected yet", fontSize = 18.sp , fontWeight = FontWeight.Bold)

                                }
                            }else {
                                LazyColumn(modifier=Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 10.dp) , verticalArrangement = Arrangement.spacedBy(10.dp)){
                                   storeOrders.forEach { order ->
                                        val storeData = getStore(order["storeId"].toString())

                                            if(order["status"]=="delivered" || order["status"]=="cancelled"){
                                                item {
                                                    Order(
                                                        navController = navController,
                                                        orderData = order,
                                                        storeName=storeData["name"].toString(),
                                                        storeImage = storeData["image"].toString(),
                                                        context = context
                                                    )



                                                }
                                            }





                                        }


                                    }


                            }


                        }





                }else{

                    Column(modifier=Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

                        Text(text="No $optionSelected yet" , fontSize = 18.sp , fontWeight = FontWeight.Bold)

                    }
                }






            }
        }
        else {

            Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

                Row(verticalAlignment = Alignment.CenterVertically){
                    CircularProgressIndicator(
                        modifier=Modifier.size(16.dp),
                        color= customColor,
                        strokeWidth = 2.dp

                    )
                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text="Loading your Orders",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )


                }

            }
        }




    }
}

fun getStore(storeId:String): HashMap<String, Any?> {
    for (doc in mainActivityViewModel.stores.value) {
        if (doc["storeId"] == storeId) {
            return doc
        }
    }

        return hashMapOf<String,Any?>()



}


