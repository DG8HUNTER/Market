package com.example.test.Screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.test.Component.Product
import com.example.test.Functions.searchCategory
import com.example.test.R
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.navyBlue
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreInfo(navController: NavController, storeId:String , storeName:String) {
    // Snapshot on the store for status change to be done
    var storeData: HashMap<String, Any?>? by remember {
        mutableStateOf(null)
    }
    val currentUser = Firebase.auth.currentUser?.uid.toString()
    val context = LocalContext.current
    val db = Firebase.firestore
    val scope = rememberCoroutineScope()

    var categoryIndex  by remember {
        mutableStateOf(0)
    }

    var categorySelected by remember {
        mutableStateOf("All")
    }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(key1 = true) {
        db.collection("Stores").document(storeId).get()
            .addOnSuccessListener { document ->
                storeData = document.data as HashMap<String, Any?>
            }






    }

    LaunchedEffect(key1 =categorySelected) {

        if(categorySelected=="All"){
            scope.launch(Dispatchers.Default) {
                try {
                    // Perform heavy or blocking operation (e.g., searchStore) in the background
                    val result = mutableListOf<HashMap<String, Any>>() // Initialize an empty list of hash maps
                    val data = db.collection("Products").whereEqualTo("storeId",storeId).get().await() // Fetch all documents from the "Products" collection

                    for (document in data) { // Iterate over each document in the query snapshot
                         // Get the data of the current document as a map
                        if (document != null) { // Check if the data is not null
                            result.add(HashMap(document.data)) // Add a copy of the data to the result list
                        }
                    }

                    // Switch to the main thread to update UI with the result
                    withContext(Dispatchers.Main) {
                        mainActivityViewModel.setValue(result, "products")
                        Log.d("Results", result.toString())
                    }
                } catch (e: Exception) {
                    // Handle any exceptions that occur during the coroutine execution
                    Log.e("CoroutineError", "An error occurred: ${e.message}", e)
                } finally {
                    // Reset the flag to indicate that the coroutine has completed

                }
            }
        }else {

            scope.launch(Dispatchers.Default) {
                try {
                    // Perform heavy or blocking operation (e.g., searchStore) in the background
                    val result = mutableListOf<HashMap<String, Any>>() // Initialize an empty list of hash maps
                    val data = db.collection("Products").whereEqualTo("storeId",storeId).get().await() // Fetch all documents from the "Products" collection

                    for (document in data) { // Iterate over each document in the query snapshot
                        // Get the data of the current document as a map
                        if (document != null) {
                            // Check if the data is not null
                            if(document.data["category"].toString()==categorySelected) {
                                result.add(HashMap(document.data))
                            }// Add a copy of the data to the result list
                        }
                    }

                    // Switch to the main thread to update UI with the result
                    withContext(Dispatchers.Main) {
                        mainActivityViewModel.setValue(result, "products")
                        Log.d("Results", result.toString())
                    }
                } catch (e: Exception) {
                    // Handle any exceptions that occur during the coroutine execution
                    Log.e("CoroutineError", "An error occurred: ${e.message}", e)
                } finally {
                    // Reset the flag to indicate that the coroutine has completed

                }
            }

        }

    }

 val productRef= db.collection("Products")
    productRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(ContentValues.TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.documents.isNotEmpty()) {

            mainActivityViewModel.setValue(mutableListOf<HashMap<String, Any>>(), "products")
            mainActivityViewModel.setValue(mutableListOf<String>("All"), "categories")

            for (document in snapshot.documents) {

                if(document.data?.get("storeId").toString() == storeId){
                    searchCategory(document.data?.get("category").toString())
                }

                if (categorySelected == "All") {
                    if (document.data?.get("storeId").toString() == storeId) {

                        mainActivityViewModel.addToProducts(document.data as HashMap<String, Any?>)
                    }
                } else {

                    if (document.data?.get("storeId").toString() == storeId && document.data?.get("category") == categorySelected
                    ) {

                        mainActivityViewModel.addToProducts(document.data as HashMap<String, Any?>)
                    }


                }
            }
                // Toast.makeText(context, "Stores Updated", Toast.LENGTH_SHORT).show()
            }else {
                Log.d(ContentValues.TAG, "Current data: null")
            }
        }

    val favoritesRef = db.collection("Favorites")

    favoritesRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(ContentValues.TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null) {
            mainActivityViewModel.setValue(mutableListOf<HashMap<String, Any>>(), "favorites")
            if (snapshot.documents.size >= 1){

                for (document in snapshot.documents) {
                    if (document.data?.get("storeId")
                            .toString() ==storeId && document.data?.get("userId") == currentUser
                    ) {
                        mainActivityViewModel.addToFavorites(document.data as HashMap<String, Any?>)
                    }
                }
            }
            Log.d("favorites", mainActivityViewModel.favorites.value.toString())


            // Toast.makeText(context, "Stores Updated", Toast.LENGTH_SHORT).show()
        }else {
            Log.d(ContentValues.TAG, "Current data: null")
        }
    }




    // State to track whether the bottom sheet is expanded or not
    val bottomSheetState = rememberModalBottomSheetState()

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
                                        .background(color = customColor)
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
                        Box(modifier = Modifier
                            .clickable {
                                if (item["name"] == "Favorites") {
                                    navController.navigate(
                                        route = "FavoriteItemsScreen/$storeId/${
                                            storeData
                                                ?.get("name")
                                                ?.toString()
                                        }"
                                    )
                                }else {
                                    navController.navigate("OrdersPerStore/${storeData!!["storeId"].toString()}")
                                }
                            }
                            .fillMaxWidth()
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp,

                                ),) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                                Icon(
                                    painter = item["icon"] as Painter,
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(text = item["name"].toString(), fontSize = 14.sp)
                            }

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
                .padding(15.dp)

        ) {

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween , modifier = Modifier.fillMaxWidth()) {

                Row(verticalAlignment = Alignment.CenterVertically){
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
                        text = storeName,
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

                Row(verticalAlignment = Alignment.CenterVertically){



                    Box(

                        modifier = Modifier


                            )
                     {

                        Surface(modifier=Modifier.size(55.dp).clip(shape = CircleShape).clickable(
                            onClick = { navController.navigate(route = "MyShoppingCardScreen")},
                        ).border(border = BorderStroke(width = 1.dp , color=Color.LightGray), shape = CircleShape).background(color= Color.White, shape = CircleShape), shadowElevation = 10.dp){

                            Row(verticalAlignment = Alignment.CenterVertically, modifier=Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center){

                                Icon(painter = painterResource(id = R.drawable.shoppingcardfilled), contentDescription =" "  , modifier = Modifier.size(24.dp) , tint= customColor)

                            }
                        }


                         Badge(modifier= Modifier
                             .offset(x=(5).dp).clip(CircleShape).background(color= Color.Red, shape = CircleShape)
                             .align(Alignment.TopEnd) , contentColor = Color.White, containerColor = Color.Red){
                             Text(if(mainActivityViewModel.addToCardProduct.value.size <100)mainActivityViewModel.addToCardProduct.value.size.toString() else "+99", modifier =Modifier.padding(vertical = 2.dp))
                         }


                    }

                }

            }
            ScrollableTabRow(selectedTabIndex =if(categoryIndex>= mainActivityViewModel.categories.value.size) categoryIndex-1 else categoryIndex , modifier = Modifier.fillMaxWidth(), contentColor = customColor,   indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[categoryIndex])
                        .clip(shape = RoundedCornerShape(10.dp)),
                    color = customColor,
                    height=4.dp
                )}, edgePadding = 0.dp) {

                mainActivityViewModel.categories.value.forEachIndexed { index, value ->

                    Tab(selected = categoryIndex==index, onClick = {
                        categoryIndex=index
                        categorySelected=value

                    }) {

                        Text(text =value.toString() , fontSize = 15.sp , fontWeight = FontWeight.Medium , color=if(categoryIndex==index)Color.Black else Color.Gray, modifier = Modifier.padding(10.dp))
                    }
                }

            }

            /*     Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Product()
                    Product()
                    Product()
                }

            }*/


            if (mainActivityViewModel.products.value.size != 0){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),

                ) {
                   mainActivityViewModel.products.value.forEach { product ->
                       item{Product(product  , context=context , navController = navController , storeName=storeName)}
                   }
                }

        }
            else {
                Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text ="No product Available" , fontSize = 16.sp , fontWeight = FontWeight.Medium , color=Color.Black)


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