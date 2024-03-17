package com.example.test.Screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.ui.theme.customGreen
import kotlinx.coroutines.delay
import java.util.Collections.list

@Composable

fun WelcomeScreen(navController: NavController){
    val icons :MutableList<Int> = mutableListOf(R.drawable.burger2,
        R.drawable.chicken,R.drawable.pizza,R.drawable.fries,R.drawable.cookies,R.drawable.donut,R.drawable.icecream,R.drawable.orange)
    var product :MutableList<String> = mutableListOf("Burger","Chicken", "Pizza" , "Fries", "Cookies","Donut","Ice-Cream","Fruit")
    val lastPosition = icons.size-1

    var position by remember{
        mutableStateOf(0)
    }
    val currentIcon by remember {
        mutableIntStateOf(icons[position])
    }


    LaunchedEffect(key1 = true , key2 = position) {
        if(position<icons.size-1){
            delay(2000)
            position+=1
        }

        if(position==icons.size-1){
            delay(2000)
            position=0

        }


    }
    var launched by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(targetValue = if(launched)360f else 0f, animationSpec = tween(2000, easing = FastOutSlowInEasing),
        label = ""
    )
    LaunchedEffect(key1 = true) {
        launched=true



    }

/*    LaunchedEffect(key1 = position) {
        if(position==lastPosition){
            position=0
        }

    }*/


    Column(modifier= Modifier
        .fillMaxSize()
        .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(40.dp)){



        Column(modifier=Modifier.fillMaxWidth()){
            Row(modifier=Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically){
                Text(text = "Welcome to " , fontSize = 30.sp , fontWeight = FontWeight.Bold)
                Text(text = "Market " , fontSize = 30.sp , fontWeight = FontWeight.Bold, color = customGreen,fontFamily = FontFamily.Serif)
                Icon(
                    painter = painterResource(id = R.drawable.store),
                    contentDescription = "marketplace",
                    tint = customGreen,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(rotation)
                )



            }
            Text(text = "Your pocket-friendly marketplace " , fontSize = 15.sp, fontWeight = FontWeight.W400 )

        }


        Column(modifier= Modifier
            .fillMaxWidth().fillMaxHeight(0.5f), verticalArrangement = Arrangement.SpaceAround
            , ){
            Column() {
                Text(
                    text = "Tell us what you're looking for!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "we have: ", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text(
                        text = product[position],
                        color = customGreen,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )


                }
            }
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Image(painter = painterResource(icons[position]), contentDescription ="orderIcon", modifier = Modifier.size(
                    if(product[position]=="Donut") 160.dp else
                    150.dp))

            }







        }
        Column(modifier=Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(40.dp)){
            Column(modifier = Modifier.fillMaxWidth()){
                Text(text="Don't worry we got it " , fontWeight = FontWeight.Medium, fontSize =25.sp)
                Text(text="Discover a shopping experience like never before", fontSize = 19.sp , fontWeight = FontWeight.Medium, color=Color.Gray, modifier = Modifier.fillMaxWidth(0.9f))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Button(onClick = {navController.navigate("SignUpScreen")},
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(shape = RoundedCornerShape(2.dp)),contentPadding = PaddingValues(), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)){
                    Box(modifier = Modifier.fillMaxWidth().clip(shape= RoundedCornerShape(2.dp)).background(color= customGreen, shape = RoundedCornerShape(2.dp)).padding(15.dp), contentAlignment = Alignment.Center){
                        Text(text ="Get Started", fontWeight = FontWeight.Bold , fontSize = 18.sp, color=Color.White )

                    }

                }

            }


        }









    }


}