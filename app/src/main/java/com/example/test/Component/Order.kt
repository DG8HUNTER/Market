package com.example.test.Component

import android.content.ContentValues
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.R
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.blue
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.lightGray1
import com.example.test.ui.theme.lightGray3
import com.example.test.ui.theme.superLightGray
import com.example.test.ui.theme.yellow
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.A
import java.text.SimpleDateFormat
import java.util.Date

@Composable

fun Order(orderData:HashMap<String,Any>){
    val createdAtTimestamp = orderData["createdAt"] as Timestamp
    val createdAtDate = createdAtTimestamp.toDate()
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
    val formattedDate = sdf.format(createdAtDate)

    val scope = rememberCoroutineScope()
   var storeData:HashMap<String,Any> by remember {mutableStateOf(hashMapOf()) }
    LaunchedEffect(key1 = true) {
        val db = Firebase.firestore
        scope.launch(Dispatchers.Default){
            val store = db.collection("Stores").document(orderData["storeId"].toString()).get().await()
            withContext(Dispatchers.Main){
                storeData=store.data as HashMap<String, Any>
            }


        }


    }
    Box(modifier= Modifier
        .fillMaxWidth()
        .shadow(elevation = 10.dp)
        .height(230.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(
            color = Color.White, shape = RoundedCornerShape(7.dp)

        )){
        val painter = rememberImagePainter(data =storeData["image"]
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
                            text = storeData["name"].toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(25.dp)) {
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

        Row(modifier=Modifier.fillMaxSize() , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End  ){
            Button(onClick = {} , modifier= Modifier
                .width(110.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(color = customColor, shape = RoundedCornerShape(7.dp)), contentPadding = PaddingValues(0.dp) , colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)){
Row(modifier=Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){

    Icon(imageVector= Icons.Filled.Refresh, contentDescription ="" , tint=Color.White , modifier=Modifier.size(20.dp))
    Spacer(modifier = Modifier.width(5.dp))
    Text("Re-order" , fontWeight = FontWeight.Bold , fontSize = 16.sp)
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