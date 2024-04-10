package com.example.test.Component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.test.R
import com.example.test.ui.theme.customGreen

@Composable

fun Product(){

    //val painter = rememberImagePainter(data = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmtAtrYACF1-lq-_of6g_lRj9cRqzlMAOcaHJa-yStaA&s")

    val discount = 90
    val price = 6.00

    Column(modifier= Modifier
        .height(240.dp)
        .width(180.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {



        Surface(
            modifier = Modifier
              //  .height(160.dp)
                .width(130.dp),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 10.dp,
            color = Color.White


        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(25.dp)) {

                        Icon(
                            painter = painterResource(id = R.drawable.favoriteitem),
                            contentDescription = "favorite",
                            modifier = Modifier.size(16.dp)
                        )
                    }


                }

                Image(
                    painter = painterResource(id = R.drawable.pizza),
                    contentDescription = "product image",
                    modifier = Modifier

                        .size(60.dp)
                        .clip(shape = CircleShape)
                )

                Text(
                    text = "Pizza",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${price}$",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    fontFamily = FontFamily.SansSerif,
                    modifier=Modifier.drawWithContent {
                        drawContent()
                        if(discount!=0){
                        val strokeWidth = 1.dp.toPx()
                        val verticalCenter = size.height / 2 + 2 * strokeWidth
                        drawLine(
                            color = Color.Red,
                            strokeWidth = strokeWidth,
                            start = Offset(0f, verticalCenter),
                            end = Offset(size.width, verticalCenter)
                        )}
                    },
                    //textDecoration = if(discount!=0)TextDecoration.LineThrough else TextDecoration.None

                )
                if(discount!=0){
                    val discountPrice = price*(discount/100.0)
                    val newPrice =price- discountPrice
                    val formattedNumber = String.format("%.2f", newPrice)
                    Text(formattedNumber, fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        fontFamily = FontFamily.SansSerif,)
                }


            }

        }

        IconButton(onClick = { /*TODO*/ },
            modifier = Modifier.size(30.dp)
                .offset(x = 60.dp, y = (-23).dp)
                .clip(shape = CircleShape)
                .background(color=Color.Transparent, shape = CircleShape)
                , colors = IconButtonDefaults.iconButtonColors(containerColor = customGreen)) {
                Icon(imageVector =Icons.Filled.Add, contentDescription ="", tint=Color.White)

        }
        if(discount!=0) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(35.dp)
                    .offset(x = (-60).dp, y = (-195).dp)

                    .clip(shape = CircleShape)
                    .background(color = Color.Transparent, shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red)
            ) {
                Text(
                    " - ${discount}%",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )

            }
        }
    }
    }






@Preview
@Composable
fun PreviewProduct(){

        Product()

}