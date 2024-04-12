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
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.test.R
import com.example.test.ui.theme.customGreen
import com.google.android.play.integrity.internal.f

@Composable

fun Product(data :HashMap<String,Any>){

    val painter = rememberImagePainter(data = data["image"].toString())



    Box(modifier= Modifier

        .padding(horizontal = 7.dp, vertical = 7.dp )
        ) {


        Surface(
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 10.dp,
            color = Color.White,


        ) {
Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = if(data["discount"].toString().toFloat()!=0f) Arrangement.SpaceBetween else Arrangement.End,modifier=Modifier.fillMaxWidth().padding(horizontal = 2.dp)){


          if(data["discount"].toString().toFloat()!=0f) {
              Text(
                  " - ${data["discount"].toString()}%",
                  fontSize = 12.sp,
                  color = Color.Red,
                  fontWeight = FontWeight.Bold,

              )

          }


    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(25.dp)) {

        Icon(
            painter = painterResource(id = R.drawable.favoriteitem),
            contentDescription = "favorite",
            modifier = Modifier.size(16.dp)
        )
    }

}





            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painter,
                    contentDescription = "product image",
                    modifier = Modifier.padding(top=20.dp)

                        .size(60.dp)
                        .clip(shape = CircleShape)
                )

                Text(
                    text = data["name"].toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    //textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${String.format("%.2f", data["price"].toString().toFloat())}$",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    fontFamily = FontFamily.SansSerif,
                    modifier=Modifier.drawWithContent {
                        drawContent()
                        if(data["discount"].toString().toFloat() !=0f){
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
                if(data["discount"].toString().toFloat() !=0f){
                    val discountPrice = data["price"].toString().toFloat() *(data["discount"].toString().toFloat()/100f)
                    val newPrice =data["price"].toString().toFloat() - discountPrice.toFloat()
                    val formattedNumber = String.format("%.2f", newPrice)
                    Text(text="${formattedNumber}$", fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        fontFamily = FontFamily.SansSerif,)
                }


            }



        }


     /*   if(discount!=0) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(35.dp)
                    .offset(x = (-45).dp, y = (-177).dp)

                    .clip(shape = CircleShape)
                    .background(color = Color.Transparent, shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red)
            ) {
                Text(
                    " - ${discount}%",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

            }
        }*/

        Column(modifier=Modifier.align(Alignment.BottomEnd).offset(x=8.dp,y=(8).dp)){
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(30.dp).clip(CircleShape)
                    .background(color = Color.Transparent, shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(containerColor = customGreen)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "", tint = Color.White)
            }

        }


    }




    }





/*
@Preview
@Composable
fun PreviewProduct(){
    Column(modifier=Modifier.fillMaxSize().background(color=Color.White).padding(20.dp) ){
        Product()

    }



}*/