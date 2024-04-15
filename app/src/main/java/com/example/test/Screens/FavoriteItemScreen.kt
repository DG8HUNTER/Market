package com.example.test.Screens

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.Product
import com.example.test.Functions.searchForString
import com.example.test.ui.theme.customGreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.A

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavoriteItemsScreen(navController: NavController , storeId :String ,storeName :String) {
    val currentUser = Firebase.auth.currentUser?.uid.toString()
    val db = Firebase.firestore
    val context = LocalContext.current
    var categoryIndex by remember {
        mutableIntStateOf(0)
    }

    Log.d("storeId", storeId)
    var categorySelected: String? by remember {
        mutableStateOf("")
    }

    var snapshotDocuments: MutableList<DocumentSnapshot> by remember {
        mutableStateOf(mutableListOf())
    }


    var favoritesProductsPerCategory: MutableList<HashMap<String, Any?>> by remember {
        mutableStateOf(mutableListOf())
    }

    val scope = rememberCoroutineScope()


    categorySelected = if (mainActivityViewModel.favoriteCategories.value.size != 0) {
        mainActivityViewModel.favoriteCategories.value[categoryIndex]
    } else {
        null
    }

    /*val favoritesRef = db.collection("Favorites")

    favoritesRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(ContentValues.TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null ) {
            Log.d("Snap", snapshot.documents.size.toString())
            val newArray: MutableList<DocumentSnapshot> = mutableListOf()


            scope.launch(Dispatchers.Default){
                for (doc in snapshot.documents) {
                    val docData = doc.data
                    if (docData != null) {
                        val docStoreId = docData["storeId"]
                        Log.d("Store ID", docStoreId.toString())
                        if (docStoreId == storeId) {
                            newArray.add(doc)
                            Log.d("NewArray", newArray.toString())
                        }


                    } else {
                        Log.e("Document", "Document data is null")
                    }
                }
                withContext(Dispatchers.Main){
                    snapshotDocuments = newArray


                }


            }

            Log.d("snapshotDocuments" , snapshotDocuments.toString())

        }
    }

    LaunchedEffect(key1 =snapshotDocuments, categorySelected) {
        Log.d("SnapChanged" , snapshotDocuments.size.toString())
        if(snapshotDocuments.size>=1){

            scope.launch(Dispatchers.Default){
                val result :MutableList<String?> = mutableListOf()
                val productPerCategory :MutableList<HashMap<String,Any>?> = mutableListOf()
                val favoriteProducts: MutableList<HashMap<String,Any>?> = mutableListOf()
                for(doc in snapshotDocuments){
                    val product = db.collection("Products").document(doc.data!!["productId"].toString()).get().await()
                    favoriteProducts.add(product.data as HashMap<String, Any>)
                    Log.d("Snap Doc" , doc.data.toString())
                  val data=  db.collection("Products").document(doc.data!!["productId"].toString()).get().await()
                        if(data!=null){
                            Log.d("cate" , data.data!!["category"].toString())

                            if(result.size>=1){
                                if(!searchForString(result , data.data?.get("category").toString())){
                                    result.add(data.data?.get("category").toString())
                                }
                            }
                            else{
                                result.add(data.data?.get("category").toString())
                            }


                        }
                        Log.d("result from if" , result.toString())


                Log.d("result from loop" , result.toString())

                }
              /*  if(favoriteProducts.size>=1){
                    for(product in favoriteProducts ){
                        if(product!!["category"].toString()==categorySelected){
                            productPerCategory.add(product)

                        }
               }
                }*/
                if(favoriteProducts.isNotEmpty()) {
                    for(product in favoriteProducts) {
                        val category = product?.get("category")?.toString()
                        if(category == categorySelected) {
                            productPerCategory.add(product)
                        }
                    }
                }

                withContext(Dispatchers.Main){
                    Log.d("result from Main" , result.toString())
                    mainActivityViewModel.setValue(mutableListOf<String?>() , "favorite categories")
                    mainActivityViewModel.setValue(result , "favorite categories")
                    mainActivityViewModel.setValue(productPerCategory , "favorite products")
                }
            }


        }
        else{
            mainActivityViewModel.setValue(mutableListOf<String?>() , "favorite categories")
        }

    }


*/
    db.collection("Favorites")
        .addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w(TAG, "listen:error", e)
                return@addSnapshotListener
            }

              scope.launch(Dispatchers.Default){

                  val favorites:MutableList<HashMap<String,Any?>> = mutableListOf()
                  val products :MutableList<HashMap<String,Any?>> = mutableListOf()
                  for (dc in snapshots!!.documentChanges) {
                      if(dc.document.data["storeId"]==storeId && dc.document.data["userId"]==currentUser){
                          favorites.add(dc.document.data as HashMap<String, Any?>)
                      }

                  }

                  withContext(Dispatchers.Main){
                      mainActivityViewModel.setValue(favorites,"favorites")


                  }
              }

        }


    LaunchedEffect(key1 = mainActivityViewModel.favorites.value , key2=categorySelected ) {
        Log.d("favorites g " , mainActivityViewModel.favorites.value.toString())
        val categories :MutableList<String> = mutableListOf()
        val favoriteProducts :MutableList<HashMap<String,Any?>> =  mutableListOf()
        scope.launch(Dispatchers.Default){
            for(doc in mainActivityViewModel.favorites.value){
                Log.d("doc in fav" , doc.toString())
            val productRef = db.collection("Products").document(doc["productId"].toString()).get().await()
                if(!searchForString(categories, productRef.data!!["category"].toString())){
                    categories.add(productRef.data!!["category"].toString())
                }

                if(productRef.data!!["category"]==categorySelected){
                    favoriteProducts.add(productRef.data as HashMap<String,Any?>)
                }


            }
            Log.d("catt" ,categories.toString())
            Log.d("fav" , favoriteProducts.toString())
            withContext(Dispatchers.Main){
                mainActivityViewModel.setValue(categories , "favorite categories" )
                favoritesProductsPerCategory=favoriteProducts

            }

        }


    }


Log.d("product per category" , favoritesProductsPerCategory.toString())

            /*  val favoritesRef = db.collection("Favorites")

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

          */
            Log.d("Last Value", mainActivityViewModel.favoriteCategories.value.toString())


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier.size(22.dp)
                        )

                    }

                    Text("  Favorite Items", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(5.dp))


                }


                if (mainActivityViewModel.favoriteCategories.value.size != 0) {
                    ScrollableTabRow(
                        selectedTabIndex = categoryIndex, modifier = Modifier.fillMaxWidth(),
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[categoryIndex])
                                    .clip(shape = RoundedCornerShape(10.dp)),
                                color = customGreen,
                                height = 4.dp
                            )
                        }, edgePadding = 0.dp


                    ) {

                        mainActivityViewModel.favoriteCategories.value.forEachIndexed { index, item ->
                            Tab(selected = categoryIndex == index, onClick = {
                                categorySelected = item
                                categoryIndex = index

                            }) {
                                Text(
                                    text = item.toString(),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (categoryIndex == index) Color.Black else Color.Gray,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                        }


                    }
                    if (favoritesProductsPerCategory.size > 0){
                        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                            favoritesProductsPerCategory.forEach { product ->
                                item {
                                    Product(
                                        data = product ,
                                        context = context
                                    )
                                }
                            }

                        }
                }

                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No Favorites Item For $storeName ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }


            }


        }
