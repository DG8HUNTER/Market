package com.example.test.Screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.OrderItem
import com.example.test.ui.theme.customColor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.A

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun OrderInfo(navController: NavController,orderId:String){

    var isLoading by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()

    var orderItemsData :MutableList<HashMap<String,Any>> by remember {
        mutableStateOf(mutableListOf())
    }




    LaunchedEffect(key1 = true) {
       val oItems :MutableList<HashMap<String,Any>> = mutableListOf()
        var data :HashMap<String,Any> = hashMapOf()

        scope.launch(Dispatchers.Default){
            val db= Firebase.firestore
            val docs=db.collection("OrderItems").whereEqualTo("orderId",orderId).get().await()


       if(docs!=null){
           for(doc in docs){
               Log.d("doc",doc.data.toString())
            val productData = db.collection("Products").document(doc.data["productId"].toString()).get().await()
               if(productData!=null){
                   data = hashMapOf(
                       "name" to productData.data?.get("name").toString(),
                       "quantity" to  doc.data["quantity"].toString(),
                       "image" to productData.data?.get("image").toString(),
                       "totalPrice" to doc.data["totalPrice"].toString(),
                       "discount" to productData.data?.get("discount").toString().toInt(),
                       "price"   to productData.data?.get("price").toString()
                       )
                   oItems.add(data)
               }




           }

           withContext(Dispatchers.Main){
               orderItemsData=oItems
               Log.d("orderItemsData",orderItemsData.toString())
               delay(1000)
               isLoading=false
           }
       }



        }


    }




    Column(modifier= Modifier
        .fillMaxSize()
        .padding(20.dp)){
        Row(modifier= Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Arrow Back" , modifier=Modifier.size(22.dp))

            }

            Text(text = "$orderId " , fontSize = 22.sp , fontWeight = FontWeight.Bold, color = customColor)

        }

        if(!isLoading){

            if(orderItemsData.size!=0){
                LazyColumn(modifier=Modifier.fillMaxWidth().fillMaxHeight(0.9f), verticalArrangement = Arrangement.spacedBy(15.dp), contentPadding = PaddingValues(vertical = 10.dp)){
                    orderItemsData.forEach { data ->
                        item{
                            OrderItem(data = data)

                        }
                    }
                }



            }else {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text ="No Data Found")
                }
            }



        }else {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    CircularProgressIndicator(
                        modifier=Modifier.size(25.dp),
                        color= customColor,
                        strokeWidth = 2.dp

                    )


            }


        }





    }

}