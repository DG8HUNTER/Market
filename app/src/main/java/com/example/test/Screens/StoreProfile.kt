package com.example.test.Screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.R
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.lightGray1
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun StoreProfile(navController: NavController , storeId:String) {
    val db = Firebase.firestore
    var storeData: HashMap<String, Any?>? by remember {
        mutableStateOf(null)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val phoneNumber="71675525"

    LaunchedEffect(key1 = true) {
        db.collection("Stores").document(storeId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    storeData = document.data as HashMap<String, Any?>
                }

            }

    }
    var operatingTime :HashMap<String,Any?> by remember {
        mutableStateOf(
            linkedMapOf(
                "Monday" to "",
                "Tuesday" to "",
                "Wednesday" to "",
                "Thursday" to "",
                "Friday" to "",
                "Saturday" to "",
                "Sunday" to ""
            )
        )
    }

    var deliveryInfo by remember {
        mutableStateOf(
            listOf(
                hashMapOf(
                    "iconId" to R.drawable.deliverytime,
                    "name" to "Delivery Time",
                    "info" to ""


                ),
                hashMapOf(
                    "iconId" to R.drawable.deliverycost,
                    "name" to "Delivery Charge",
                    "info" to ""


                ),
                hashMapOf(
                    "iconId" to R.drawable.minimumcharge,
                    "name" to "Minimum Charge",
                    "info" to ""


                )
            )
        )
    }

    if (storeData != null) {
        deliveryInfo = listOf(
            hashMapOf(
                "iconId" to R.drawable.deliverytime,
                "name" to "Delivery Time",
                "info" to storeData!!["deliveryTime"].toString()


            ),
            hashMapOf(
                "iconId" to R.drawable.deliverycost,
                "name" to "Delivery Charge",
                "info" to storeData!!["deliveryCharge"].toString()


            ),
            hashMapOf(
                "iconId" to R.drawable.minimumcharge,
                "name" to "Minimum Charge",
                "info" to storeData!!["minimumCharge"].toString()


            ))
        val operating = storeData!!["OperatingField"] as HashMap<String,Any?>

        operatingTime = linkedMapOf(
            "Monday" to operating["Monday"],
            "Tuesday" to operating["Tuesday"],
            "Wednesday" to operating["Wednesday"],
            "Thursday" to operating["Thursday"],
            "Friday" to operating["Friday"],
            "Saturday" to operating["Saturday"],
            "Sunday" to operating["Sunday"],


        )
    }




val weekDay = listOf("Monday" , "Tuesday" , "Wednesday" ,"Thursday" , "Friday" , "Saturday" , "Sunday" )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 5.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {

                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Arrow Back",
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Store Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            }
            Spacer(modifier = Modifier.height(5.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clip(shape = RoundedCornerShape(5.dp))
            )


        }
        Spacer(modifier = Modifier.height(5.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painter =
                    rememberImagePainter(data = if (storeData != null) storeData!!["image"].toString() else "")

                Image(
                    painter = painter,
                    contentDescription = "Store icon",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(1.dp, Color.Gray),
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.FillBounds,

                    )
                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)

                ) {
                    Text(
                        text = if (storeData != null) storeData!!["name"].toString() else "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = if (storeData != null) storeData!!["location"].toString() else "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )


                }


            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                deliveryInfo.forEach { item ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(
                                border = BorderStroke(1.dp, color = Color.Gray),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(color = lightGray1, shape = RoundedCornerShape(3.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = item["iconId"] as Int),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(text = item["name"].toString(), fontSize = 10.sp)
                            Row() {
                                Text(
                                    text = item["info"].toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = if (item["name"] == "Delivery Time") "mins" else "$",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }


                        }

                    }

                }


            }
            Spacer(modifier = Modifier.height(20.dp))

             Text(text = "Operating Time", fontSize = 18.sp , fontWeight = FontWeight.Bold )

            LazyColumn() {

               operatingTime.forEach{item->
                   item{
                       Row(modifier= Modifier
                           .fillMaxWidth()
                           .padding(top = 10.dp, bottom = 10.dp) , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween){
                           Text(text=item.key , fontWeight = FontWeight.Medium  , fontSize = 16.sp)
                           if(item.value!="") {
                               Row() {
                                   val values = item.value as? HashMap<String, Any?>
                                   if(values!=null) {
                                       Text(text = values["OpeningTime"].toString() , color=Color.Gray ,fontSize = 16.sp,fontWeight = FontWeight.Medium)
                                       Text(text = " - " , color=Color.Gray , fontSize = 16.sp,fontWeight = FontWeight.Medium)
                                       Text(text = values["ClosingTime"].toString(), color=Color.Gray, fontSize = 16.sp,fontWeight = FontWeight.Medium)
                                   }
                                   else {

                                       Text(text="Closed" , color=Color.Gray ,fontSize = 16.sp,fontWeight = FontWeight.Medium)
                                   }
                               }
                           }
                           else {
                               Text(text="")
                           }


                       }
                   }



               }


            }

            Spacer(modifier=Modifier.height(25.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${storeData!!["phoneNumber"]}")
                    }
                        context.startActivity(intent)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .clip(shape = RoundedCornerShape(0.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)

            ) {
                Box(modifier=Modifier.fillMaxWidth().background(color = customColor , shape = RoundedCornerShape(0.dp)).height(50.dp), contentAlignment = Alignment.Center){
                    Text(text = "Call Store" , fontSize = 18.sp , fontWeight = FontWeight.Bold)
                }

            }

        }


    }
}

private suspend fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // Handle error or show a message that no activity is available to handle the intent
    }
}
