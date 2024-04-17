package com.example.test.Screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.example.test.Component.Product
import org.checkerframework.checker.units.qual.A

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

    val scope = rememberCoroutineScope()

  LaunchedEffect(key1 =true) {
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

        val painter = rememberImagePainter(data = data["image"])

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
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

                IconButton(onClick = { navController.popBackStack() }) {
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


            Column(
                modifier = Modifier
                    .fillMaxWidth()


            ) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ProductImage(painter = painter)
                    }
                    Spacer(modifier = Modifier.height(16.dp))




                    ProductDetails(
                        name = data["name"].toString(),
                        price = data["price"].toString(),
                        description = data["description"].toString()
                    )



                if (mainActivityViewModel.products.value.size!= 0){
                    Text(text ="Similar Items" , fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Spacer(modifier=Modifier.height(5.dp))
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp), // Set a fixed height
                        contentPadding = PaddingValues(vertical = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                       mainActivityViewModel.products.value.forEach { product ->

                           if(product["productId"]!=productId && product["category"]==category){
                            item { Product(product, context = context, navController = navController) }}
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