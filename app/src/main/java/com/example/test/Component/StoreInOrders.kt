package com.example.test.Component

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.customColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun StoreInOrders(navController: NavController, data: HashMap<String, Any>) {
    val painter = rememberImagePainter(data = data["image"].toString())
    val scope= rememberCoroutineScope()
    var totalOrders by remember {
        mutableIntStateOf(0)
    }
    val animateTotalOrders = animateFloatAsState(targetValue = totalOrders.toString().toFloat() , label="" , animationSpec = tween(1000,
        easing = FastOutLinearInEasing))

    var pastOrders by remember {
        mutableIntStateOf(0)
    }
    val animatePastOrders = animateFloatAsState(targetValue = pastOrders.toString().toFloat() , label="" , animationSpec = tween(1000,
        easing = FastOutLinearInEasing))
    var currentOrders by remember {
        mutableIntStateOf(0)
    }
    val animateCurrentOrders = animateFloatAsState(targetValue = currentOrders.toString().toFloat() , label="" , animationSpec = tween(1000,
        easing = FastOutLinearInEasing))




    LaunchedEffect(key1 = mainActivityViewModel.orders.value) {
        scope.launch(Dispatchers.Default){
            val totalO = data["totalOrders"].toString().toInt()
            val pastO=data["pastOrders"].toString().toInt()
            val currentO =data["currentOrders"].toString().toInt()

            withContext(Dispatchers.Main){
                totalOrders=totalO
                pastOrders=pastO
                currentOrders=currentO
            }
        }




    }

    Log.d("animatedPast",animatePastOrders.value.toString())
    Log.d("animatedCurrent",animateCurrentOrders.value.toString())

    Box(
        modifier = Modifier
            .shadow(elevation = 10.dp, shape =RoundedCornerShape(10.dp) )
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(130.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp),

                )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Image(
                painter = painter,
                contentDescription = "Store Icon",
                modifier = Modifier
                    .border(
                        border = BorderStroke(width = 1.dp, color = Color.Gray),
                        shape = CircleShape
                    )
                    .size(100.dp)
                    .clip(shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Store details
            Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.85f)) {
                // Store name
                Text(
                    text = data["name"] as String,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Store location
                Text(
                    text = data["location"] as String,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Orders
               Column {
                    // Total Orders

                   Row(){
                       Text(
                           text = "Total Orders: ",
                           fontSize = 16.sp,
                           fontWeight = FontWeight.Bold,
                           fontFamily = FontFamily.SansSerif,
                           color = Color.Gray
                       )

                       Text(
                           text = "${animateTotalOrders.value.toInt()}",
                           fontSize = 16.sp,
                           fontWeight = FontWeight.Bold,
                           fontFamily = FontFamily.SansSerif,
                           color = customColor
                       )
                   }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Past Orders
                   Row{

                       Row(){
                           Text(
                               text = "Past:",
                               fontSize = 16.sp,
                               fontWeight = FontWeight.Bold,
                               fontFamily = FontFamily.SansSerif,
                               color = Color.Gray
                           )

                           Text(
                               text = " ${animatePastOrders.value.toInt()}",
                               fontSize = 16.sp,
                               fontWeight = FontWeight.Bold,
                               fontFamily = FontFamily.SansSerif,
                               color = customColor
                           )
                       }

                       Spacer(modifier = Modifier.width(16.dp))

                       // Current Orders
                       Row(){
                           Text(
                               text = "Current:",
                               fontSize = 16.sp,
                               fontWeight = FontWeight.Bold,
                               fontFamily = FontFamily.SansSerif,
                               color = Color.Gray
                           )
                           Text(
                               text = "${animateCurrentOrders.value.toInt()}",
                               fontSize = 16.sp,
                               fontWeight = FontWeight.Bold,
                               fontFamily = FontFamily.SansSerif,
                               color = customColor
                           )
                       }

                   }

                }


            }

            IconButton(onClick = {
                navController.navigate(route ="OrdersPerStore/${data["storeId"].toString()}") }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "next",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )

            }
        }
    }
}


