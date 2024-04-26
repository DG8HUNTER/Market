package com.example.test.Screens

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.Order
import com.example.test.Component.Store
import com.example.test.R
import com.example.test.ui.theme.customColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun OrderScreen(navController: NavController) {
    var index by remember {
        mutableIntStateOf(0)
    }
    val scope= rememberCoroutineScope()
    val stores = remember { mainActivityViewModel.stores.value }
    val context = LocalContext.current
    val isLoading  by remember {
        mutableStateOf(true)
    }

    var currentOrder by remember {
        mutableIntStateOf(0)
    }

    var pastOrder by remember {
        mutableIntStateOf(0)
    }


    val currentUser = Firebase.auth.currentUser?.uid.toString()

    val dbRef = Firebase.firestore

    val ordersRef = dbRef.collection("Orders")
    ordersRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(ContentValues.TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null) {
            scope.launch(Dispatchers.Default){
                var past =0
                var current =0
                if (snapshot.documents.size == 0) {
                    mainActivityViewModel.setValue(mutableListOf<HashMap<String, Any>>(), "orders")
                }
                else {

                    Log.d("snapshotSize" , snapshot.documents.size.toString())
                    val new :MutableList<HashMap<String,Any>> = mutableListOf()

                    for(document in snapshot.documents){
                        if(document!=null){
                            if(document.data!!["userId"].toString()==currentUser){
                                new.add(document.data as HashMap<String,Any>)
                                if(document.data!!["status"].toString()=="pending" ||document.data!!["status"].toString()=="processing" ){
                                    past+=1
                                }
                                else{
                                    current+=1
                                }
                            }


                        }
                    }
                    withContext(Dispatchers.Main){
                        mainActivityViewModel.setValue(new , "orders")
                        pastOrder=past
                        currentOrder = current
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

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(mainActivityViewModel.orders.value.size!=0){


                LazyColumn(modifier=Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 10.dp) , verticalArrangement = Arrangement.spacedBy(10.dp)){
                    mainActivityViewModel.orders.value.forEach { order ->
                        if(optionSelected=="Current Orders"){
                            if(order["status"]=="pending" || order["status"]=="processing"){

                                item {
                                    Order(orderData=order)

                                }
                            }

                      }
                        else {
                            if(order["status"]!="pending" && order["status"]!="processing"){
                                item {
                                    Order(orderData=order)

                                }
                            }


                        }

                    }
                }


            }






        }


    }
}





