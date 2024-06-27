package com.example.test.Component

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.R
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.blue
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.yellow
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun Order(
    navController: NavController,
    orderData: HashMap<String, Any>,
    storeName: String,
    storeImage: String,
    context: Context
){
    val createdAtTimestamp = orderData["createdAt"] as Timestamp
    val createdAtDate = createdAtTimestamp.toDate()
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
    val formattedDate = sdf.format(createdAtDate)
    val scope = rememberCoroutineScope()
    var orderedProduct :MutableList <HashMap<String,Any?>> by remember{
        mutableStateOf(mutableListOf())
    }

    var reOrderedProduct :MutableList<HashMap<String,Any>> by remember {
        mutableStateOf(mutableListOf())
    }

    val currentUser = Firebase.auth.currentUser?.uid.toString()

    var isReordering by remember {
        mutableStateOf(false)
    }

    var isAdding by remember {
        mutableStateOf(false)
    }


    var reordered by remember {
        mutableStateOf(false)
    }




    val db=Firebase.firestore


    LaunchedEffect(key1 =true) {
        scope.launch(Dispatchers.Default){
            val docs = db.collection("OrderItems").whereEqualTo("orderId",orderData["orderId"]).get().await()
            val ordersItemData :MutableList<HashMap<String,Any?>> = mutableListOf()
            if(docs!=null){
                for(doc in docs){
                       ordersItemData.add(doc.data as HashMap<String, Any?>)

                }
                    withContext(Dispatchers.Main){
                        Log.d("Ordered1" , ordersItemData.toString())
                        orderedProduct=ordersItemData
                    }
            }
        }

    }

    Box(modifier= Modifier
        .fillMaxWidth()
        .shadow(elevation = 10.dp, shape = RoundedCornerShape(12.dp))
        .height(230.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(
            color = Color.White, shape = RoundedCornerShape(7.dp)

        )){
        val painter = rememberImagePainter(data =storeImage
        )
Column(modifier=Modifier.fillMaxSize()){


        Row(modifier= Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painter, contentDescription = "store image",
                modifier = Modifier
                    .size(60.dp)
                    .border(BorderStroke(width = 1.dp, color = Color.Gray), shape = CircleShape)
                    .clip(
                        CircleShape
                    ),

                contentScale = ContentScale.Fit,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Order From : ",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Text(
                            text = storeName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(onClick = {navController.navigate(route = "OrderInfoScreen/${orderData["orderId"]}") }, modifier = Modifier.size(25.dp)) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "arrow right",
                            modifier = Modifier.size(18.dp),
                            tint = customColor
                        )


                    }


                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        "Order Id : ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text = orderData["orderId"].toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        "Order In : ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text =formattedDate.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                }

            }


        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
        , verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(modifier=Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween){
            Text(text ="Items : (${orderData["totalItems"]} items)",   fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray)
            Text(text="${orderData["totalPrice"].toString()} $", fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = Color.LightGray)
            .clip(shape = RoundedCornerShape(5.dp)))

       Row(modifier=Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){

            Text(text ="Status :",fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically){

                when(orderData["status"].toString()){
                     "pending"-> Icon(painter = painterResource(id = R.drawable.circle), contentDescription ="pending",modifier=Modifier.size(13.dp),tint= yellow )
                     "processing"->Icon(painter = painterResource(id = R.drawable.circle), contentDescription ="pending",modifier=Modifier.size(13.dp),tint= customColor )
                     "shipped"->Icon(painter = painterResource(id = R.drawable.circle), contentDescription ="pending",modifier=Modifier.size(13.dp),tint= blue )
                     "cancelled"-> Icon(painter = painterResource(id =R.drawable.cancelled), contentDescription ="cancelled", modifier=Modifier.size(13.dp) , tint=Color.Red )
                     "delivered" ->  Icon(painter = painterResource(id = R.drawable.circle), contentDescription ="pending",modifier=Modifier.size(13.dp),tint=Color.Green )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(text=orderData["status"].toString(), fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

            }

        }

        Row(modifier=Modifier.fillMaxSize() , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){

            Button(modifier= Modifier
                .width(140.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(color = customColor, shape = RoundedCornerShape(7.dp)), contentPadding = PaddingValues(8.dp) , colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),

                onClick = {

                    isAdding=true
scope.launch(Dispatchers.Default){
    val addToBasket :MutableList <HashMap<String,Any?>> = mutableListOf()
    var data : HashMap<String,Any?> = hashMapOf()
    for(order in orderedProduct){
        var totalPerItem =0f
        val quantity = order["quantity"]


        val productData = db.collection("Products").document(order["productId"].toString()).get().await()

        if(productData!=null){
            totalPerItem =  if(productData["discount"].toString().toInt()==0){
                productData["price"].toString().toFloat()* quantity.toString().toFloat()
            }else{

                val discountPrice =(((productData["price"].toString().toFloat())* quantity.toString().toFloat())) *(productData["discount"].toString().toFloat()/100f)
                (((productData["price"].toString().toFloat())* quantity.toString().toFloat())-discountPrice)

            }

        }

        data = productData.data as HashMap<String,Any?>
        data["quantity"]=quantity
        data["totalPrice"] =totalPerItem

        addToBasket.add(data)

    }

    withContext(Dispatchers.Main){
        delay(1000)
        mainActivityViewModel.setValue(addToBasket , "addToCardProduct")
        isAdding=false
        Toast.makeText(context , "All  product added successfully !" , Toast.LENGTH_SHORT).show()

    }

}




                }){

                if (!isAdding) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,

                    ) {
                        Icon(
                            painterResource(id = R.drawable.addtoshoppingcard),
                            contentDescription = "Cart button icon",
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "Add to cart",
                            Modifier.padding(start = 10.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                } else {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = customColor,
                            strokeWidth = 2.dp

                        )

                        Text(
                            text = "Adding",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp

                        )


                    }


                }

            }
            Button(onClick = {
                isReordering = true

                scope.launch(Dispatchers.Default){
                    val reorderedProduct :MutableList<HashMap<String,Any>>  = mutableListOf()
                    delay(1000)
                    var orders :MutableList<HashMap<String,Any>> = mutableListOf()
                    var data :HashMap<String,Any> = hashMapOf()
                    var totalPerItem =0f
                    var total = 0f
                    var items = 0
                    var totalProfit=0f
                    var productData :HashMap<String,Any?> = hashMapOf()
                    Log.d("Ordered2" , orderedProduct.toString())
                    for (orderItem in orderedProduct){

                        Log.d("like" , orderItem.toString())
                        items+=1
                        val product = db.collection("Products").document(orderItem["productId"].toString()).get().await()
                        if(product!=null){
                             productData = product.data as HashMap<String,Any?>
                            productData["quantity"] = orderItem["quantity"].toString().toInt()
                            totalPerItem =  if(productData["discount"].toString().toInt()==0){
                                productData["price"].toString().toFloat()* productData["quantity"].toString().toFloat()
                            }else{

                                val discountPrice =(((productData["price"].toString().toFloat())* productData["quantity"].toString().toFloat())) *(productData["discount"].toString().toFloat()/100f)
                                (((productData["price"].toString().toFloat())* productData["quantity"].toString().toFloat())-discountPrice)

                            }

                            totalProfit +=  productData["quantity"].toString().toFloat()*productData["profitPerItem"].toString().toFloat()

                            total+=totalPerItem

                        }

                        data = hashMapOf(
                            "productId" to productData["productId"].toString(),
                            "quantity" to orderItem["quantity"].toString().toInt(),
                            "totalPrice" to String.format("%.2f".format(totalPerItem)).toDouble(),
                            "totalProfit" to String.format("%.2f".format(productData["quantity"].toString().toFloat()*productData["profitPerItem"].toString().toFloat())).toDouble())
                        reorderedProduct.add(data)



                    }

                    Log.d("reordered" , reorderedProduct.toString())
                    val currentDateTime = LocalDateTime.now()
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

                    db.collection("Orders").add(
                        hashMapOf(
                            "orderId" to "",
                            "userId" to currentUser,
                            "status" to "pending",
                            "storeId" to orderData["storeId"],
                            "createdAt" to dateFormat.parse("${currentDateTime.dayOfMonth}-${currentDateTime.monthValue}-${currentDateTime.year} ${currentDateTime.hour}:${currentDateTime.minute}:${currentDateTime.second}"),
                            "updatedAt" to dateFormat.parse("${currentDateTime.dayOfMonth}-${currentDateTime.monthValue}-${currentDateTime.year} ${currentDateTime.hour}:${currentDateTime.minute}:${currentDateTime.second}"),
                            "totalItems" to items,
                            "totalPrice" to String.format("%.2f".format(total)).toDouble(),
                            "totalProfit" to String.format("%.2f".format(totalProfit)).toDouble(),
                            "location"  to orderData["location"]

                        )
                    ).addOnSuccessListener { document ->

                        db.collection("Orders").document(document.id).update("orderId", document.id)

                        for (order in reorderedProduct){
                            Log.d("reordered4",order.toString())
                            val toOrder :HashMap<String,Any> = order
                            toOrder["orderId"] =  document.id
                            toOrder["orderItemId"] = ""
                            Log.d("toOrder",toOrder.toString())

                            db.collection("OrderItems").add(toOrder).addOnSuccessListener { doc ->
                                db.collection("OrderItems").document(doc.id).update("orderItemId", doc.id)
                            }
                        }



                    }

                    withContext(Dispatchers.Main){
                        isReordering=false
                    }








                }










            } , modifier= Modifier
                .width(140.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(color = customColor, shape = RoundedCornerShape(7.dp)), contentPadding = PaddingValues(8.dp) , colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)){
Row(modifier=Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){
    if(isReordering){
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 3.dp,
            modifier = Modifier.size(16.dp)
        )

    }
    else {
        Icon(imageVector= Icons.Filled.Refresh, contentDescription ="" , tint=Color.White , modifier=Modifier.size(20.dp))
    }

    Spacer(modifier = Modifier.width(5.dp))
    Text(if (isReordering)"Reordering" else "Reorder" , fontWeight = FontWeight.Bold , fontSize = 16.sp)
}



            }

        }



    }
        }







    }



}


/*
@Preview
@Composable
fun PreviewCard(){
    Column(modifier= Modifier
        .background(color = Color.White)
        .fillMaxSize()
        .padding(20.dp)){
        Order()
    }

}*/