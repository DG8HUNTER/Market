package com.example.test.Screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.Functions.searchElement
import com.example.test.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

import com.example.test.Component.Product
import com.example.test.ui.theme.customColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductInfoScreen(navController: NavController , productId:String ,storeId:String , category:String){
  val context = LocalContext.current
    var isFavorites  by remember {
        mutableStateOf(false )
    }
    var similarItems :MutableList<HashMap<String,Any?>> by remember {
        mutableStateOf(mutableListOf())
    }

    var quantity :Int by remember{
        mutableIntStateOf(0)
    }

    if(mainActivityViewModel.favorites.value.size!=0){
        isFavorites = searchElement(mainActivityViewModel.favorites.value , key ="productId" , value =productId)
    }
    else {
        isFavorites=false
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var data :HashMap<String,Any?>by remember {
        mutableStateOf(hashMapOf())
    }

    val focus = LocalFocusManager.current

    val scope = rememberCoroutineScope()

  LaunchedEffect(key1 =true , mainActivityViewModel.products.value) {
      scope.launch(Dispatchers.Default){
          val db= Firebase.firestore
           lateinit var productData : HashMap<String,Any?>
        // var similar :MutableList<HashMap<String,Any?>> = mutableListOf()

          val product = db.collection("Products").document(productId).get().await()
          if(product!=null){
             productData = product.data as HashMap<String,Any?>

          }
          withContext(Dispatchers.Main){
              data=productData
              isLoading=false

          }


      }

  }





    if(!isLoading) {

        LaunchedEffect(key1 =quantity) {

            if(quantity > data["inventory"].toString().toInt()){
                quantity=data["inventory"].toString().toInt()

            }

        }

        val painter = rememberImagePainter(data = data["image"])

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 5.dp),
            ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "clear icon",
                            modifier = Modifier.size(20.dp)
                        )


                    }
                    Text(
                        text = data["name"].toString(),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp) // Reduce bottom padding
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (data["inventory"].toString()
                                .toInt() > 0
                        ) "In Stock" else "Out of Stock",
                        color = if (data["inventory"].toString()
                                .toInt() > 0
                        ) Color(0xFF008000) else Color.Red,
                        fontWeight = FontWeight.Normal
                    )



                    IconButton(onClick = {



                    }) {
                        Icon(
                            painter = if (!isFavorites) painterResource(id = R.drawable.love) else painterResource(
                                id = R.drawable.lovefilledred
                            ),
                            contentDescription = "favorite",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )

                    }
                }

            }

            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)


            ) {
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ProductImage(painter = painter)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                }


                item {
                    ProductDetails(
                        name = data["name"].toString(),
                        price = data["price"].toString(),
                        description = data["description"].toString()
                    )

                }



                item {
                    if (mainActivityViewModel.products.value.size != 0) {
                        Text(
                            text = "Similar Items",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp), // Set a fixed height
                            contentPadding = PaddingValues(vertical = 15.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            mainActivityViewModel.products.value.forEach { product ->

                                if (product["productId"] != productId && product["category"] == category) {
                                    item {
                                        Product(
                                            product,
                                            context = context,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                    }

                }


            }

            if (data["inventory"].toString().toInt() > 0){


                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                ,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clickable(
                                            enabled = quantity != 0,
                                            onClick = { quantity -= 1 })

                                        .background(
                                            color = customColor,
                                            shape = RoundedCornerShape(7.dp)
                                        )
                                        .width(35.dp)
                                        .height(35.dp)
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
                                Spacer(modifier = Modifier.width(10.dp))


                                Row(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(50.dp)
                                        .background(
                                            color = customColor,
                                            shape = RoundedCornerShape(7.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(7.dp))
                                ) {

                                   TextField(value = if (quantity == 0) "" else quantity.toString(),
                                        onValueChange = {

                                            quantity = if (it.isNotEmpty()) {
                                                it.toInt()
                                            } else {
                                                0
                                            }
                                        },
                                        maxLines = 2,
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
                                        trailingIcon = {

                                            Text(
                                                text = "/ ${data["inventory"]}",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White
                                            )
                                        },
                                        placeholder = {

                                                      if(quantity==0){
                                                          Text(text = "0" , fontSize = 16.sp,
                                                              fontWeight = FontWeight.Medium,
                                                              color = Color.White,modifier=Modifier.fillMaxWidth() , textAlign = TextAlign.Center)
                                                      }
                                        }

                                        ,

                                        textStyle = TextStyle(
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                    )


                                }


                                Log.d("Quantity", quantity.toString())





                                Spacer(modifier = Modifier.width(10.dp))
                                Box(
                                    modifier = Modifier
                                        .clickable(
                                            enabled = quantity <= data["inventory"]
                                                .toString()
                                                .toInt(),
                                            onClick = { quantity += 1 })
                                        .background(
                                            color = customColor,
                                            shape = RoundedCornerShape(7.dp)
                                        )
                                        .width(35.dp)
                                        .height(35.dp)
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
                            Button(onClick = {} , colors = ButtonDefaults.buttonColors(
                                containerColor = customColor
                            ) , modifier= Modifier
                                .fillMaxWidth(0.85f)
                                .clip(RoundedCornerShape(7.dp))
                                .shadow(elevation = 10.dp, shape = RoundedCornerShape(7.dp)), contentPadding = PaddingValues(vertical=12.dp), shape = RectangleShape) {
                                Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){
                                    Icon(
                                        painterResource(id = R.drawable.addtoshoppingcard),
                                        contentDescription ="Cart button icon",
                                        modifier = Modifier.size(22.dp))

                                    Text(text = "Add to cart",Modifier.padding(start = 10.dp) , fontWeight = FontWeight.Bold , fontSize = 18.sp)
                                }

                            }
                        }

                        }







        }


        }



        }




@Composable
fun ProductImage(painter: Painter) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(150.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProductDetails(name: String, price: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust vertical padding
    ) {
        Row(modifier=Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween){
            Text(
                text = name,
                fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp) // Reduce bottom padding
            )
            Text(
                text = "${String.format("%.2f", price.toFloat())}$",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp) // Reduce bottom padding
            )
        }

        Text(
            text = description,
            fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Gray,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp)) // Add some space between description and button

    }
}

/*

 Canvas(Modifier.fillMaxSize()) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            drawLine( //top line
                                start = Offset(x = 0f, y = 0f),
                                end = Offset(x = canvasWidth, y = 0f),
                                strokeWidth = 3f,
                                color = Color.Gray,
                                pathEffect = PathEffect.chainPathEffect(PathEffect.cornerPathEffect(20f),
                                    PathEffect.cornerPathEffect(0f))
                            )

                        }
 */