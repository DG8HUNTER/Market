package com.example.test.Screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.StoreInCard
import com.example.test.R
import com.example.test.ui.theme.customColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun MyShoppingCardScreen(navController: NavController) {

    var stores: MutableList<HashMap<String,Any?>> by remember {
        mutableStateOf(mutableListOf())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }
    val currentUser = Firebase.auth.currentUser?.uid.toString()

    var isOrdering by remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val scope = rememberCoroutineScope(

    )

    var success by remember {
        mutableStateOf(false)
    }


    var totalItems by remember {
        mutableIntStateOf(0)
    }



 val animateTotalToPay = animateFloatAsState(targetValue = mainActivityViewModel.totalToPay.value , label = "" , animationSpec = tween(1000, easing = FastOutSlowInEasing))
    val animateTotalItems = animateFloatAsState(targetValue = totalItems.toString().toFloat(), label = "" , animationSpec = tween(1000, easing = FastOutSlowInEasing))
Log.d("an" , animateTotalToPay.value.toString())

    LaunchedEffect(key1 = mainActivityViewModel.addToCardProduct.value) {
        val db = Firebase.firestore
        if(mainActivityViewModel.addToCardProduct.value.size !=0){
            mainActivityViewModel.setValue(0f,"totalToPay")

            val storesId :MutableSet<String>  = mutableSetOf()
            val storesData : MutableList<HashMap<String,Any?>> = mutableListOf()

            scope.launch(Dispatchers.Default){
                for(product in mainActivityViewModel.addToCardProduct.value){
                    storesId.add(product["storeId"].toString())
                }



                for(id in storesId){
                    val data = db.collection("Stores").document(id).get().await()
                    storesData.add(data.data as HashMap<String, Any?>)
                }


                withContext(Dispatchers.Main){
                    stores=storesData

                    delay(1500)
                    isLoading=false
                    totalItems= mainActivityViewModel.addToCardProduct.value.size
                }


            }


        }else {
            delay(1500)
            isLoading=false

        }


    }






    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {


        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(text = " My Shopping Card", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        }

        if(!isLoading){

            if(mainActivityViewModel.addToCardProduct.value.size!=0){
                LazyColumn(modifier= Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f), contentPadding = PaddingValues(top=10.dp)){

                    stores.forEach { store ->
                        item{
                           StoreInCard(data = store, navController =navController )
                        }



                    }
                }

                Row(modifier=Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                    Column(verticalArrangement = Arrangement.Top , horizontalAlignment = Alignment.Start,modifier= Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)){


                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text(text ="Total Items :", fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray)
                            Spacer(modifier =Modifier.width(5.dp))

                            Text(
                                text = String.format("%.0f",animateTotalItems.value)
                                ,//"${animateItemCounter.value.toInt()}"
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = customColor
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text(text ="Total To Pay :", fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray)
                            Spacer(modifier =Modifier.width(5.dp))

                            Text(
                                text = "${String.format("%.2f",animateTotalToPay.value)}$",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = customColor
                            )
                        }




                    }


                    Button(modifier= Modifier
                        .fillMaxWidth()
                        .background(color = customColor, shape = RoundedCornerShape(7.dp))
                        .clip(shape = RoundedCornerShape(7.dp)), onClick = {

                            isOrdering=true
                             val db = Firebase.firestore
                        var totalPerStore :Float = 0f
                        var items :Int =0
                        stores.forEach {store ->
                            var total=0f
                            for(data in mainActivityViewModel.addToCardProduct.value){
                                if(data["storeId"]==store["storeId"]){

                                 items+=1

                                    if(data["discount"].toString().toInt()==0){

                                        total += ((data["price"].toString().toFloat())* data["quantity"].toString().toFloat())
                                        Log.d("quantity1", data["quantity"].toString())
                                        Log.d("price1", total.toString())

                                    }

                                    else {
                                        Log.d("mmm",data["quantity"].toString())

                                        val discountPrice =(((data["price"].toString().toFloat())* data["quantity"].toString().toFloat())) *(data["discount"].toString().toFloat()/100f)
                                        total += (((data["price"].toString().toFloat())* data["quantity"].toString().toFloat())-discountPrice)
                                        Log.d("quantity2", data["quantity"].toString())
                                        Log.d("price2", total.toString())
                                    }
                                }

                            }

                            val currentDateTime = LocalDateTime.now()
                            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

                            db.collection("Orders").add(
                                hashMapOf(
                                    "orderId" to "",
                                    "userId" to currentUser,
                                    "status" to "pending",
                                    "storeId" to store["storeId"],
                                    "createdAt" to dateFormat.parse("${currentDateTime.dayOfMonth}-${currentDateTime.monthValue}-${currentDateTime.year} ${currentDateTime.hour}:${currentDateTime.minute}:${currentDateTime.second}"),
                                    "updatedAt" to dateFormat.parse("${currentDateTime.dayOfMonth}-${currentDateTime.monthValue}-${currentDateTime.year} ${currentDateTime.hour}:${currentDateTime.minute}:${currentDateTime.second}"),
                                    "totalItems" to items,
                                    "totalPrice" to String.format("%.2f".format(total)).toDouble()

                                )).addOnSuccessListener { document->
                                    if(document!=null){
                                        db.collection("Orders").document(document.id).update("orderId" , document.id)
                                        mainActivityViewModel.addToCardProduct.value.forEach { product ->
                                          if(product["storeId".toString()]==store["storeId"]){

                                              val data = hashMapOf(
                                                  "orderItemId" to "",
                                                  "productId" to product["productId"],
                                                  "orderId" to document.id,
                                                  "quantity" to product["quantity"],
                                                  "totalPrice" to String.format("%.2f".format(product["totalPrice"])).toDouble()


                                              )

                                              db.collection("OrderItems").add(data).addOnSuccessListener { document->
                                                  if(document!=null){
                                                      db.collection("OrderItems").document(document.id).update("orderItemId",document.id)

                                                  }

                                              }
                                          }




                                        }
                                    }
                            }.addOnFailureListener {it->
                                Log.d("Failed",it.message.toString())

                            }

                        }

                      scope.launch(Dispatchers.Default){
                          delay(1000)

                          withContext(Dispatchers.Main){
                              mainActivityViewModel.setValue(mutableListOf<HashMap<String,Any>>() , "addToCardProduct")

                              success=true

                          /*    if(stores.size==1){
                                  Toast.makeText(context , " Your order has been successfully placed", Toast.LENGTH_SHORT).show()
                              }

                              else {
                                  Toast.makeText(context , " Your orders have been successfully placed", Toast.LENGTH_SHORT).show()
                              }*/

                          }
                      }







                    } , shape = RectangleShape ,colors=ButtonDefaults.buttonColors(containerColor = Color.Transparent) ){

                        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){

                            if(isOrdering){
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp

                                )
                            }else{
                                Icon(painter = painterResource(id = R.drawable.checkout), contentDescription ="", modifier=Modifier.size(20.dp), tint = Color.White)

                            }


                            Spacer(modifier =Modifier.width(5.dp))

                            Text(text = if(!isOrdering)"Order" else "Ordering" , fontSize = 18.sp , fontWeight = FontWeight.Bold, color= Color.White)
                        }


                    }




                }


            }

            else {

                if(success){


                    Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                        Image(painter = painterResource(id = R.drawable.orderplacedpurchased), contentDescription ="" , modifier=Modifier.size(170.dp))
                        Spacer(modifier=Modifier.height(5.dp))
                        Text(text = if(stores.size==1)"order has been successfully placed" else "orders have been successfully placed" , fontWeight = FontWeight.Bold , fontSize = 16.sp)

                    }

                }else {

                    Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                        Image(painter = painterResource(id = R.drawable.noproductadded), contentDescription ="" , modifier=Modifier.size(170.dp))
                        Spacer(modifier=Modifier.height(5.dp))
                        Text(text ="No product Added yet" , fontWeight = FontWeight.Bold , fontSize = 16.sp)

                    }
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
                        text="Loading your Shopping Card",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )


                }
            }

        }



    }
}