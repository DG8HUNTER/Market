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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderedProduct(data: HashMap<String, Any?>, onLeftSwipe: SwipeAction , index:Int){
  val focus = LocalFocusManager.current
    val painter = rememberImagePainter(data = data["image"])

    var quantity :Int by remember {
        mutableIntStateOf(data["quantity"].toString().toInt())
    }


    var totalPrice :Float by remember {
        mutableFloatStateOf(0f)
    }



    val scope = rememberCoroutineScope()






        Log.d("mm", mainActivityViewModel.addToCardProduct.value.toString())

        var price =0f
        Log.d("k1",totalPrice.toString())





            if(data["discount"].toString().toInt()==0){

                price += ((data["price"].toString().toFloat())* data["quantity"].toString().toFloat())
                Log.d("quantity1", data["quantity"].toString())
                Log.d("price1", price.toString())

            }

            else {
                Log.d("mmm",data["quantity"].toString())

                val discountPrice =(((data["price"].toString().toFloat())* data["quantity"].toString().toFloat())) *(data["discount"].toString().toFloat()/100f)
                price += (((data["price"].toString().toFloat())* data["quantity"].toString().toFloat())-discountPrice)
                Log.d("quantity2", data["quantity"].toString())
                Log.d("price2", price.toString())
            }






    val animateTotalPrice = animateFloatAsState(targetValue =price, label = "", animationSpec = tween(1000, easing = FastOutSlowInEasing) )
















    Log.d("quantity" ,"${data["inventory"].toString()} : ${quantity.toString()}" )

    SwipeableActionsBox(endActions = listOf(onLeftSwipe), swipeThreshold = 100.dp ,modifier = Modifier
        .shadow(elevation = 10.dp, shape = RoundedCornerShape(5.dp))
        .clip(shape = RoundedCornerShape(5.dp))
        .fillMaxWidth()
        .background(color = Color.Transparent, shape = RoundedCornerShape(5.dp))
        .height(110.dp),
        backgroundUntilSwipeThreshold = Color.White
       ){
        Row(modifier=Modifier.fillMaxWidth().background(color=Color.White)){
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

                    Image(painter = painter, contentDescription ="${data["name"]} image" , contentScale = ContentScale.Fit )



                }
                Column(modifier= Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .fillMaxHeight()
                    ){
                    Column(modifier=Modifier.padding(top = 10.dp , start = 10.dp) ){
                        Text(text =data["name"].toString() , fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier =Modifier.height(5.dp))
                        Text(text="${data["price"].toString()}$ / item" , fontWeight = FontWeight.Medium , fontSize = 14.sp , color=Color.Gray)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        enabled = quantity != 1,
                                        onClick = {

                                            updateOrderedQuantity(index , quantity-1 )
                                            quantity-=1

                                        })

                                    .background(
                                        color = customColor,
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .width(35.dp)
                                    .height(25.dp)
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(7.dp)
                                    ),
                                contentAlignment = Alignment.Center

                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.minus),
                                    contentDescription = "minus",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))


                            Row(

                            ) {

                                TextField(value = if (data["quantity"] == 0) "" else data["quantity"].toString(),
                                    onValueChange = {

                                        quantity = if (it.isNotEmpty()) {
                                            it.toInt()
                                        } else {
                                            0
                                        }
                                    },
                                    maxLines = 1,
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = customColor,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White

                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focus.clearFocus()
                                        }
                                    ),
                                    /*   trailingIcon = {

                                           Text(
                                               text = "/ ${data["inventory"]}",
                                               fontSize = 16.sp,
                                               fontWeight = FontWeight.Medium,
                                               color = Color.White
                                           )
                                       },*/



                                    textStyle = TextStyle(
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = customColor
                                    ),
                                    modifier = Modifier
                                        .width(70.dp)
                                        .fillMaxHeight()

                                )


                            }


                            Log.d("Quantity", quantity.toString())





                            Spacer(modifier = Modifier.width(5.dp))
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        enabled = quantity < data["inventory"]
                                            .toString()
                                            .toInt(),
                                        onClick = {


                                            updateOrderedQuantity(index , quantity+1)
                                            quantity+=1

                                        })
                                    .background(
                                        color = customColor,
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .width(35.dp)
                                    .height(25.dp)
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(7.dp)
                                    ),
                                contentAlignment = Alignment.Center

                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plus),
                                    contentDescription = "plus",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }


                        }


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




}