package com.example.test.Component



import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.R
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.mediumGray
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import java.time.LocalTime
import java.time.format.TextStyle


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Store(data: HashMap<String, Any? >, navController: NavController) {

    Log.d("Storessssssssssssssssssss", data.toString())
    val painter = rememberImagePainter(
        data = data["image"],
        builder = {}
    )

    val db = Firebase.firestore


    // Log.d("name" , data["name"] as String)
    val today = LocalDate.now()
    val dayName = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

    Log.d("dayName", dayName)

var status :String by remember {
    mutableStateOf(data["status"].toString())
}
    Log.d("status var" , status.toString())

    val operatingTime = data["OperatingField"] as? HashMap<*, *>
    val todayOperatingTime = operatingTime?.get(dayName) as? HashMap<*, *>

    if (todayOperatingTime != null) {
        val todayOpeningTimeString = todayOperatingTime["OpeningTime"]
        val todayClosingTimeString = todayOperatingTime["ClosingTime"]

        val todayOpeningDateTime = parseTime(todayOpeningTimeString.toString())
        val todayClosingDateTime = parseTime(todayClosingTimeString.toString())


        if ((mainActivityViewModel.currentTime.value.isAfter(todayOpeningDateTime) && mainActivityViewModel.currentTime.value.isBefore(
                todayClosingDateTime
            )) || mainActivityViewModel.currentTime.value == todayOpeningDateTime || mainActivityViewModel.currentTime.value == todayClosingDateTime
        ) {

            // if
            if (status == "Close"){
                db.collection("Stores").document(data["storeId"].toString())
                    .update("status", "Open").addOnSuccessListener {
                        Log.d("Status ", "Status Updated to Open")
                        status = "Open"
                    }
       }
        } else {
            //if
           if(status=="Open") {
                db.collection("Stores").document(data["storeId"].toString())
                    .update("status", "Close").addOnSuccessListener {
                        Log.d("Status ", "Status Updated to Close")
                        status = "Close"
                    }

                Log.d("current Timeeee", mainActivityViewModel.currentTime.value.toString())
            }
        }

    }else {
        db.collection("Stores").document(data["storeId"].toString())
            .update("status", "Close").addOnSuccessListener {
                Log.d("Status ", "Status Updated to Close")
                status = "Close"

            }
    }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)


        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(route = "StoreInfoScreen/${data["storeId"]}")
                }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painter,
                        contentDescription = "${data["name"]} icon",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                           /* .border(
                                BorderStroke(1.dp, Color.Gray),
                                shape = CircleShape
                            ),*/,
                        contentScale = ContentScale.FillBounds,

                        )
                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)

                    ) {
                        Text(
                            text = data["name"] as String,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = data["location"] as String,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = if (status == "Open") R.drawable.open else R.drawable.close),
                                contentDescription = "Status Icon",
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = status,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }


                    }

                    IconButton(onClick = { navController.navigate(route = "StoreInfoScreen/${data["storeId"]}") }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "next",
                            tint = Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )

                    }


                }
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clip(shape = RoundedCornerShape(5.dp))
                )
            }
        }


    }



@RequiresApi(Build.VERSION_CODES.O)
fun parseTime(timeString: String): LocalTime {
    val date = SimpleDateFormat("h:mm a", Locale.ENGLISH).parse(timeString)

    if (date != null) {
        return LocalTime.of(date.hours, date.minutes)
    }
    else {
        return LocalTime.now()
    }



}



