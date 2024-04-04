package com.example.test.Screens

import android.content.ContentValues
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.test.Component.Store
import com.example.test.Functions.ShowStores
import com.example.test.ui.theme.customGreen
import com.google.android.play.integrity.internal.al
import com.google.android.play.integrity.internal.i
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun OrderScreen(navController: NavController){
    var index  by remember{
        mutableIntStateOf(0)
    }
    val stores = remember{ mainActivityViewModel.stores.value}
    val context = LocalContext.current

/*LaunchedEffect(key1 = true) {
    if(!mainActivityViewModel.homeLaunchedEffectExecuted.value){
        val dbRef = Firebase.firestore
       val documents =  dbRef.collection("Stores").get().await()
                if(documents!=null){
                    for(document in documents){
                        mainActivityViewModel.addToStores(document.data as HashMap<String, Any>)
                    }
                }
            }

        mainActivityViewModel.setValue(true , "homeLaunchedEffectExecuted")
    }

*/
    val dbRef = Firebase.firestore
    val storesRef = dbRef.collection("Stores")

    storesRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(ContentValues.TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.documents.isNotEmpty()) {
            mainActivityViewModel.setValue(mutableListOf<HashMap<String,Any>>(), "stores")
            Log.d("_stores", mainActivityViewModel.stores.value.toString())
            for(document in snapshot.documents){
                mainActivityViewModel.addToStores(document.data as HashMap<String, Any>)
            }
            Log.d(" stores from snap"  , mainActivityViewModel.stores.value.toString())
            Log.d("var stores from snap", stores.toString())
            // Toast.makeText(context, "Stores Updated", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(ContentValues.TAG, "Current data: null")
        }
    }






    var optionSelected  by remember {
        mutableStateOf("Current Orders")
    }


    val currentUser = Firebase.auth.currentUser?.uid.toString()
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(20.dp)){
        Column(modifier= Modifier
            .fillMaxWidth()
            .background(color = Color.White) , verticalArrangement = Arrangement.spacedBy(10.dp)){

            Row(modifier=Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.spacedBy(5.dp) , verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription ="Arrow Back", modifier = Modifier.size(22.dp))

                }
                Text(text=" My Orders" , fontSize = 22.sp , fontWeight = FontWeight.Bold)



            }

            TabRow(selectedTabIndex = index , modifier = Modifier.fillMaxWidth(), contentColor = customGreen,   indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[index])
                        .clip(shape = RoundedCornerShape(10.dp)),
                    color = customGreen,
                    height=4.dp
                )
            } ) {
                Tab(selected =index==0,
                    onClick = {
                        index=0
                        optionSelected="Current Orders"
                    },
                    selectedContentColor = customGreen
                ) {
                    Text(text ="Current Orders" , fontSize = 15.sp , fontWeight = FontWeight.Medium , color=if(index==0)Color.Black else Color.Gray, modifier = Modifier.padding(10.dp))

                }
                Tab(selected =index==1,
                    onClick = {
                        index=1
                        optionSelected="Past Orders"
                    } ,
                    selectedContentColor = customGreen
                ) {
                    Text(text ="Past Orders" , fontSize = 15.sp , fontWeight = FontWeight.Medium , color = if(index==1) Color.Black else Color.Gray,modifier = Modifier.padding(10.dp))

                }

            }






        }

        Column(modifier=Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

           // Text(text= optionSelected , fontSize = 15.sp)

            Text(text= mainActivityViewModel.stores.value.size.toString(),fontSize = 15.sp)

            if(mainActivityViewModel.stores.value.size>0){
                LazyColumn(modifier = Modifier.fillMaxSize()){
                    items(items= mainActivityViewModel.stores.value){
                        item->
                        Store(data=item)
                    }
                }}
                else{
                    Text(text="No Restaurants Yet")
                }


        }



    }

}