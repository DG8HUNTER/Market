package com.example.test.Component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.test.Functions.updateOrderedQuantity
import com.example.test.R
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.redDiscount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material3.Badge


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable()

fun OrderItem(data:HashMap<String,Any>){
    val focus = LocalFocusManager.current
    val painter = rememberImagePainter(data = data["image"])

    val quantity :Int by remember {
        mutableIntStateOf(data["quantity"].toString().toInt())
    }

    val scope = rememberCoroutineScope()

    var total by remember {
        mutableFloatStateOf( 0f)
    }

    val animateTotalPrice = animateFloatAsState(targetValue =total, label = "", animationSpec = tween(1000, easing = FastOutSlowInEasing) )

    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.Default){
            withContext(Dispatchers.Main){
                total=data["totalPrice"].toString().toFloat()
            }

        }
    }


    Log.d("animated", animateTotalPrice.value.toString())


    Log.d("quantity" ,"${data["inventory"].toString()} : ${quantity.toString()}" )

    Box(
    ){

        Box(modifier= Modifier.fillMaxSize()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(5.dp))
            .clip(shape = RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(5.dp))
            .height(110.dp)){

            Row(modifier= Modifier
                .fillMaxWidth()
                .background(color = Color.White)){
                Row(modifier= Modifier
                    .clip(shape = RoundedCornerShape(5.dp))
                    .background(color = Color(0xFFe7e7e7), shape = RoundedCornerShape(5.dp))
                    .fillMaxWidth(0.8f)
                ){
                    Column(modifier= Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .fillMaxHeight()
                        .width(100.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                        .padding(7.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){

                        Image(painter = painter, contentDescription ="${data["name"]} image" , contentScale = ContentScale.Crop )



                    }
                    Column(modifier= Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .fillMaxHeight()
                    ){
                        Column(modifier= Modifier.padding(top = 10.dp , start = 10.dp) ){
                            Text(text =data["name"].toString() , fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text="${data["price"].toString()}$ / item" , fontWeight = FontWeight.Medium , fontSize = 14.sp , color= Color.Gray)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text="Quantity :${data["quantity"].toString()}" , fontWeight = FontWeight.Medium , fontSize = 14.sp , color= Color.Gray)

                        }



                    }

                }
                Column(modifier= Modifier
                    .fillMaxSize()
                    .background(color = Color.White), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        text = "${String.format("%.2f",animateTotalPrice.value)}$",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = customColor
                    )
                }
            }


        }

        if(data["discount"].toString().toInt()!=0){
            Badge(modifier = Modifier

                .align(Alignment.TopStart)
                .offset(x = (-10).dp, y = (-10).dp)
                .size(35.dp)
                .clip(CircleShape),
                containerColor = redDiscount
            ){

                Text(text ="${data["discount"].toString()}%", fontWeight = FontWeight.Bold,color=Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }

    }






}