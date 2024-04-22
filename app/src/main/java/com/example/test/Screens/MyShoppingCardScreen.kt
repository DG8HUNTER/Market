package com.example.test.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.StoreInCard
import com.example.test.R
import com.example.test.ui.theme.customColor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun MyShoppingCardScreen(navController: NavController) {

    var stores: MutableList<HashMap<String,Any?>> by remember {
        mutableStateOf(mutableListOf())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope(

    )


    LaunchedEffect(key1 = mainActivityViewModel.addToCardProduct.value) {
        val db = Firebase.firestore
        if(mainActivityViewModel.addToCardProduct.value.size !=0){
            val storesId :MutableSet<String>  = mutableSetOf()
            val storesData : MutableList<HashMap<String,Any?>> = mutableListOf()

            scope.launch(Dispatchers.Default){
                for(product in mainActivityViewModel.addToCardProduct.value){
                    storesId.add(product["storeId"].toString())
                }

                for(id in storesId){
                    val data = db.collection("Stores").document(id).get().await()
                    storesData.add(data.data as HashMap<String, Any?>)
                }


                withContext(Dispatchers.Main){
                    stores=storesData
                    delay(1500)
                    isLoading=false
                }


            }


        }else {
            delay(1500)
            isLoading=false

        }


    }






    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {


        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(text = " My Shopping Card", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        }

        if(!isLoading){

            if(stores.size!=0){
                LazyColumn(modifier=Modifier.fillMaxSize(), contentPadding = PaddingValues(top=10.dp)){

                    stores.forEach { store ->
                        item{
                           StoreInCard(data = store, navController =navController )
                        }



                    }
                }


            }

            else {

                Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                    Image(painter = painterResource(id = R.drawable.noproductfoundorangetheme), contentDescription ="" , modifier=Modifier.size(200.dp))
                    Spacer(modifier=Modifier.height(5.dp))
                    Text(text ="No product ordered yet" , fontWeight = FontWeight.Medium , fontSize = 16.sp)

                }
            }





        }else {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    CircularProgressIndicator(
                        modifier=Modifier.size(16.dp),
                        color= customColor,
                        strokeWidth = 2.dp

                    )
                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text="Loading your Shopping Card",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )


                }
            }

        }



    }
}