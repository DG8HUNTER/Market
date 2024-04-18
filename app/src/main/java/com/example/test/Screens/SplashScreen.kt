package com.example.test.Screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.ui.theme.customColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var launched by remember {
        mutableStateOf(false)
    }




    val rotation by animateFloatAsState(
        targetValue = if (launched) 360f else 0f,
        animationSpec = tween(2000, easing = FastOutSlowInEasing),
        label = ""
    )


    var rotate by remember {
        mutableStateOf(0f)
    }
    val rotateIcon by animateFloatAsState(targetValue = rotate , tween(durationMillis =1000 , easing = FastOutSlowInEasing))

    // Simulating a loading delay
    LaunchedEffect(key1 = true) {
        launched = true
        delay(2000)
        rotate=20f
       delay(1000)
        rotate=-20f
        delay(1000)
        rotate=20f

      // rotateR=false
        delay(1500)


        // Simulating a delay of 2 seconds
       // delay(4000)
        // Navigate to the next destination
      navController.navigate("WelcomeScreen")
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        /* Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
           BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val painter: Painter = painterResource(R.drawable.burger)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    //modifier = Modifier.fillMaxSize()
                    modifier=Modifier.background(Color.Transparent)
                )
            }


        }*/

Column(modifier=Modifier.fillMaxWidth()){

    Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.End , verticalAlignment = Alignment.CenterVertically){
        Icon(painter = painterResource(id = R.drawable.burger2), contentDescription ="burger",tint=Color.Unspecified,
            modifier = Modifier
                .size(110.dp)
                .rotate(if (rotate!=0f) rotateIcon else  0f)
        )

        Icon(painter = painterResource(id = R.drawable.fries1), contentDescription ="burger" , tint=Color.Unspecified,
            modifier = Modifier
                .size(90.dp)
                .rotate(if (rotate!=0f) rotateIcon else  0f))

    }

    Row(modifier=Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){

        Icon(painter = painterResource(id = R.drawable.pizza), contentDescription ="burger",tint=Color.Unspecified,
            modifier = Modifier
                .size(110.dp)
                .rotate(if (rotate!=0f) rotateIcon else  0f)
        )

    }



}










       Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,


            ) {


            Icon(
                painter = painterResource(id = R.drawable.store),
                contentDescription = "marketplace",
                tint = customColor,
                modifier = Modifier
                    .size(110.dp)
                    .rotate(rotation)
            )



            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Market",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = customColor,
                fontSize = 22.sp
            )


        }


        Column(modifier=Modifier.fillMaxWidth()){

            Row(modifier=Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){

                Icon(painter = painterResource(id = R.drawable.cookies), contentDescription ="burger",tint=Color.Unspecified,
                    modifier = Modifier
                        .size(120.dp)
                        .rotate(if (rotate!=0f) rotateIcon else  0f)
                )

            }

            Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Start , verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.icecream), contentDescription ="burger",tint=Color.Unspecified,
                    modifier = Modifier
                        .size(95.dp)
                        .rotate(if (rotate!=0f) rotateIcon else  0f)
                )

                Icon(painter = painterResource(id = R.drawable.donut), contentDescription ="burger" , tint=Color.Unspecified,
                    modifier = Modifier
                        .size(155.dp)
                        .rotate(if (rotate!=0f) rotateIcon else  0f))

            }





        }
    }
}


