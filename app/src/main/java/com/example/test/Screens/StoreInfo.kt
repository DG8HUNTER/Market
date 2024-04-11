package com.example.test.Screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import com.example.test.Component.Product
import com.example.test.R
import com.example.test.ui.theme.customGreen
import com.example.test.ui.theme.navyBlue
import com.example.test.ui.theme.superLightGray
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreInfo(navController: NavController, storeId:String) {
    // Snapshot on the store for status change to be done
    var storeData: HashMap<String, Any?>? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val db = Firebase.firestore
    LaunchedEffect(key1 = true) {
        db.collection("Stores").document(storeId).get()
            .addOnSuccessListener { document ->
                storeData = document.data as HashMap<String, Any?>
            }



    }

    // State to track whether the bottom sheet is expanded or not
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipHiddenState = false,
            skipPartiallyExpanded = false
        )
    )
    var isClicked by remember {
        mutableStateOf(false)
    }

    val storeOption = listOf(
        hashMapOf(
            "name" to "Orders",
            "icon" to painterResource(id = R.drawable.shoppingbasket),
            "onClickEvent" to {}


        ),
        hashMapOf(
            "name" to "Favorites",
            "icon" to painterResource(id = R.drawable.favorite),
            "onClickEvent" to {}
        )
    )


    /*  if(isVisible){
            ModalBottomSheet(onDismissRequest = {isVisible=false}, sheetState = bottomSheetState) {
                Column(modifier=Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Hello world")
                }

            }
        }*/

    BottomSheetScaffold(
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center

                ) {
                    /* // Background Image
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val painter: Painter = painterResource(R.drawable.storebackgroudn2)
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                //modifier = Modifier.fillMaxSize()
                                modifier = Modifier.background(Color.Transparent)
                            )
                        }*/

                    val painter = rememberImagePainter(
                        data = if (storeData != null) storeData!!["image"] else ""
                    )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = navyBlue)
                            .padding(20.dp), verticalAlignment = Alignment.CenterVertically
                    ) {


                        Image(
                            painter = painter, contentDescription = "", modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .border(
                                    BorderStroke(1.dp, Color.Gray),
                                    shape = CircleShape
                                ),
                            contentScale = ContentScale.FillBounds
                        )






                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)

                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (storeData != null) storeData!!["name"].toString() else "",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.width(10.dp))
                                if (storeData != null) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = if (storeData!!["status"] == "Open") R.drawable.open else R.drawable.close),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier.size(10.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = if (storeData != null) storeData!!["status"].toString() else "",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                            }

                            Text(
                                text = if (storeData != null) storeData!!["location"].toString() else "",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )

                            Button(
                                onClick = {
                                    navController.navigate(route = "StoreProfile/${storeId}")
                                }, modifier = Modifier
                                    .clip(shape = RoundedCornerShape(20.dp))
                                    .width(90.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                contentPadding = PaddingValues(0.dp)


                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(35.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(color = customGreen)
                                        .padding(7.dp), contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "View Store",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }


                            }
                        }

                    }


                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp, bottom = 10.dp, start = 20.dp)
                ) {

                    Text(
                        text = "Account Activity :",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
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

                    storeOption.forEachIndexed { index, item ->
                        Row(modifier = Modifier
                            .clickable { item["onClickEvent"] }
                            .fillMaxWidth()
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp,

                                )) {
                            Icon(
                                painter = item["icon"] as Painter,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = item["name"].toString(), fontSize = 14.sp)


                        }


                    }


                }


            }


        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContainerColor = Color.White,
        sheetShadowElevation = 10.dp


    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "KeyboardArrowLeft",
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (storeData != null) storeData!!["name"].toString() else "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )

                IconButton(onClick = {
                    if (!scaffoldState.bottomSheetState.isVisible) {
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    } else {
                        scope.launch { scaffoldState.bottomSheetState.hide() }
                    }
                    isClicked = !isClicked

                }) {
                    Icon(
                        imageVector = if (!scaffoldState.bottomSheetState.isVisible) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = "",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Gray
                    )

                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Product()
                    Product()
                    Product()
                }

            }
        }


    }
}






 /*@Composable
     fun bottomSheetContent(){
     Column {
         Text("Bottom Sheet Content")
         Button(onClick = {scope.launch{ bottomSheetState.hide()} }) {
             Text("Close Bottom Sheet")
         }
     }
     }

*/