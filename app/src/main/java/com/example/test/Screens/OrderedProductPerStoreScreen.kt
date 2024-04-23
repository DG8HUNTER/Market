package com.example.test.Screens

import android.os.Build
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.CLasses.OrderItem
import com.example.test.Component.OrderedProduct
import com.example.test.R
import com.example.test.ui.theme.customColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.saket.swipe.SwipeAction

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderedProductPerStore(navController: NavController , storeId:String , storeName:String){

    var orderedProducts : MutableList<HashMap<String,Any?>> by remember {
        mutableStateOf(mutableListOf())
    }
    var isLoading by remember {
        mutableStateOf(true)
    }
    var totalAmount  by remember {
        mutableFloatStateOf(0f)
    }

    val animateTotal = animateFloatAsState(targetValue = totalAmount , label="" , animationSpec = tween(1500 , easing= FastOutSlowInEasing))
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1= mainActivityViewModel.addToCardProduct.value) {
        //mainActivityViewModel.setValue(mutableListOf<OrderItem>() , "orderItems")
        val products :MutableList<HashMap<String,Any?>> = mutableListOf()
        var total=0f
    //  lateinit var orderItem :OrderItem
        Log.d("the" , mainActivityViewModel.addToCardProduct.value.toString())
       scope.launch(Dispatchers.Default){
           if(mainActivityViewModel.addToCardProduct.value.size!=0){
               for(data in mainActivityViewModel.addToCardProduct.value){

                   if(storeId==data["storeId"]){
                       products.add(data)


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

                    /*   orderItem = OrderItem(orderItemId = "" ,
                           productId = data["productId"].toString() ,
                           storeId=data["storeId"].toString(),
                           quantity = data["quantity"].toString().toInt(),
                           orderId = "",
                           totalPRice = total
                           )
                       mainActivityViewModel.addToOrderItems(data=orderItem)*/

                   }

               }

           }
           Log.d("Data", products.toString())
           withContext(Dispatchers.Main){
               orderedProducts=products
               totalAmount=total

               Log.d("main" , orderedProducts.toString())
               delay(1000)
               isLoading=false
           }
       }

    }



    Column(modifier= Modifier
        .fillMaxSize()
        .padding(20.dp)){


        Column(modifier= Modifier.fillMaxWidth()){
            Row(modifier= Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Arrow Back" , modifier=Modifier.size(22.dp))

                }
                Text(text = "From  " , fontSize = 22.sp , fontWeight = FontWeight.Bold)

                Text(text = if(!isLoading)"$storeName (${orderedProducts.size})" else storeName , fontSize = 22.sp, fontWeight = FontWeight.Bold, color= customColor )




            }


        }

        if(!isLoading){

            if(orderedProducts.size!=0){

                LazyColumn(modifier= Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                   , contentPadding = PaddingValues(vertical = 10.dp),
                ){

                    orderedProducts.forEachIndexed { index,product->

                        val delete = SwipeAction(
                            onSwipe = {
                                deleteProduct(index =index)
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.padding(start = 15.dp)
                                )

                            },
                            background = Color.Red,

                        )

                        item{


                            OrderedProduct(data = product , onLeftSwipe = delete , index=index)

                            if(index!=orderedProducts.size-1){
                                Row(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(end = 25.dp, bottom = 5.dp, top = 5.dp) , horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
                                    Icon(imageVector =Icons.Filled.Add, contentDescription ="added" , modifier = Modifier.size(20.dp), tint = Color.Gray)
                                }
                            }

                        }






                    }

                }

                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(0.98f).fillMaxHeight()){
                    Text(text ="Total Amount " , fontWeight = FontWeight.Bold, fontSize = 16.sp , color= Color.Gray)

                    Text(
                        text = "${String.format("%.2f", animateTotal.value)}$",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = customColor
                    )


                }





            }
            else {

                Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                    Image(painter = painterResource(id = R.drawable.noproductfoundorangetheme), contentDescription ="" , modifier=Modifier.size(200.dp))
                    Spacer(modifier=Modifier.height(5.dp))
                    Text(text ="No product ordered yet" , fontWeight = FontWeight.Medium , fontSize = 16.sp)

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
                        text="Loading products",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )


                }
            }
        }

    }



}


@RequiresApi(Build.VERSION_CODES.O)
fun deleteProduct(index:Int) {
    var array :MutableList<HashMap<String,Any?>> = mutableListOf()

    array = mainActivityViewModel.addToCardProduct.value.toMutableList()
       array.removeAt(index)

    mainActivityViewModel.setValue(array,"addToCardProduct")

}


