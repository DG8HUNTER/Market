package com.example.test.Component

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.test.Functions.searchElement
import com.example.test.R
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.customColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun Product(data: HashMap<String, Any?>, context: Context , navController: NavController){
   val currentUser=Firebase.auth.currentUser?.uid.toString()
    val db = Firebase.firestore
    val currentUserId = Firebase.auth.currentUser?.uid.toString()

    val painter = rememberImagePainter(data = data["image"].toString())


//Log.d("favoritess ${data["name"]}", isFavorites.toString())

    Log.d("from product" , mainActivityViewModel.favorites.toString())

    var isFavorites  by remember {
        mutableStateOf(false )
    }

    if(mainActivityViewModel.favorites.value.size!=0){
        isFavorites = searchElement(mainActivityViewModel.favorites.value , key ="productId" , value =data["productId"].toString())
    }
    else {
        isFavorites=false
    }


    Log.d("favorites ${data["name"]}", isFavorites.toString())


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
                            .toString() == data["storeId"] && document.data?.get("userId") == currentUser
                    ) {
                        mainActivityViewModel.addToFavorites(document.data as HashMap<String, Any?>)
                    }
                }
        }
            Log.d("favorites", mainActivityViewModel.favorites.value.toString())
            isFavorites =searchElement(mainActivityViewModel.favorites.value , key ="productId" , value =data["productId"].toString())

            // Toast.makeText(context, "Stores Updated", Toast.LENGTH_SHORT).show()
        }else {
            Log.d(ContentValues.TAG, "Current data: null")
        }
    }*/



    Box(modifier= Modifier


        ) {


        Surface(
            modifier= Modifier
                .padding(start = 2.dp)
                .height(165.dp)
                .width(130.dp),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 10.dp,
            color = Color.White,


        ) {
            

Row(verticalAlignment = Alignment.Top , modifier=Modifier.fillMaxWidth()){

Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = if(data["discount"].toString().toFloat()!=0f) Arrangement.SpaceBetween else Arrangement.End,modifier= Modifier
    .padding(2.dp)
    .fillMaxWidth() ){

    if(data["discount"].toString().toFloat()!=0f) {
        Text(
            " - ${data["discount"].toString()}%",
            fontSize = 12.sp,
            color = Color.Red,
            fontWeight = FontWeight.Bold,

            )

    }


    IconButton(onClick = {
        if (!isFavorites) {
            val info = hashMapOf(
                "favoriteId" to "",
                "userId" to currentUserId,
                "storeId" to data["storeId"].toString(),
                "productId" to data["productId"].toString()

            )
            db.collection("Favorites").add(info)
                .addOnSuccessListener { documentReference ->
                    if (documentReference != null){
                        db.collection("Favorites").document(documentReference.id).update("favoriteId" ,documentReference.id.toString())

                        Toast.makeText(
                            context,
                            "${data["name"].toString()} added to your favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        " Failed to add ${data["name"].toString()}  to your favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            db.collection("Favorites")
                .whereEqualTo("userId", currentUserId)
                .whereEqualTo("storeId", data["storeId"])
                .whereEqualTo("productId", data["productId"])
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("Favorites").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "${data["name"].toString()} removed from favorites", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error deleting document", e)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }






        }

    }, modifier = Modifier.size(25.dp)) {

        Icon(
            painter = if(!isFavorites) painterResource(id = R.drawable.love) else painterResource(id = R.drawable.lovefilledred),
            contentDescription = "favorite",
            modifier = Modifier.size(18.dp),
            tint=Color.Unspecified)
    }

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
                    modifier = Modifier
                        .padding(top = 20.dp)

                        .size(60.dp)
                        .clip(shape = CircleShape)
                )
                Spacer(modifier=Modifier.height(7.dp))

                Text(
                    text =data["name"].toString()  ,
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
                    Spacer(modifier = Modifier.height(10.dp))

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

        Column(modifier= Modifier
            .align(Alignment.BottomEnd)
            .offset(x = 8.dp, y = (8).dp)){
            IconButton(
                onClick = { navController.navigate(route="ProductInfoScreen/${data["productId"]}/${data["storeId"]}/${data["category"]}") },
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(color = Color.Transparent, shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(containerColor = customColor)
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