package com.example.test.Component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.R
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.customColor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StoreInCard(data: HashMap<String, Any? >, navController: NavController) {

    Log.d("Storessssssssssssssssssss", data.toString())
    val painter = rememberImagePainter(
        data = data["image"],
        builder = {}
    )

    var itemsCount by remember {
        mutableIntStateOf(0)
    }

    val animateItemCounter = animateFloatAsState(targetValue = itemsCount.toFloat(), label = "", animationSpec = tween(500, easing = FastOutSlowInEasing) )

    var totalPrice :Float by remember {
        mutableFloatStateOf(0f)
    }

    val animateTotalPrice = animateFloatAsState(targetValue = totalPrice, label = "", animationSpec = tween(1000, easing = FastOutSlowInEasing) )


    val scope = rememberCoroutineScope()



    val db = Firebase.firestore

    LaunchedEffect(key1 =true , key2= mainActivityViewModel.addToCardProduct.value) {
     scope.launch(Dispatchers.Default){
         var count =0
         var price =0f
         for(product in mainActivityViewModel.addToCardProduct.value){
             if(product["storeId"]== data["storeId"]){
                 count+=1

                 if(product["discount"].toString().toInt()==0){

                     price += ((product["price"].toString().toFloat())* product["quantity"].toString().toFloat())
                 }

                 else {

                     val discountPrice =(((product["price"].toString().toFloat())* product["quantity"].toString().toFloat())) *(product["discount"].toString().toFloat()/100f)
                     price += (((product["price"].toString().toFloat())* product["quantity"].toString().toFloat())-discountPrice)

                 }



             }
         }

         withContext(Dispatchers.Main){
             itemsCount=count
            delay(1000)
             totalPrice=price
         }


     }

    }




    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)


    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(route = "StoreInfoScreen/${data["storeId"]}/${data["name"]}")
            }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painter,
                    contentDescription = "${data["name"]} icon",
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
                        text = data["name"] as String,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = data["location"] as String,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text("Total Items : " ,  fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray)

                            Text(
                                text = "${animateItemCounter.value.toInt()}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = customColor
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text("Total Price : " ,  fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray)

                            Text(
                                text = "${String.format("%.2f", animateTotalPrice.value)}$",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = customColor
                            )
                        }






                    }


                }

                IconButton(onClick = {  navController.navigate(route = "StoreInfoScreen/${data["storeId"]}/${data["name"]}") }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "next",
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )

                }


            }
            Spacer(modifier = Modifier.height(10.dp))
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
    }


}
