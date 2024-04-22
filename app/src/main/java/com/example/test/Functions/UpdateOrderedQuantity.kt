package com.example.test.Functions

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.test.Screens.mainActivityViewModel

@RequiresApi(Build.VERSION_CODES.O)


    fun updateOrderedQuantity(index: Int, quantity: Int) {
        val productList = mainActivityViewModel.addToCardProduct.value.toMutableList()


    val existingHashMap = productList[index]

// Create a new HashMap using the existing HashMap's contents
    val newHashMap= hashMapOf(
        "productId" to productList[index]["productId"],
        "name" to productList[index]["name"],
        "inventory" to productList[index]["inventory"],
        "price" to productList[index]["price"],
        "discount"  to productList[index]["discount"],
        "quantity" to quantity,
        "image"  to productList[index]["image"],
        "storeId" to productList[index]["storeId"],
        "description" to productList[index]["description"]



    )

    productList[index]=newHashMap

        // need a recomposition
      //  mainActivityViewModel.setValue(mutableListOf<HashMap<String,Any>>(), "addToCardProduct")
    mainActivityViewModel.setValue(productList.toMutableList(), "addToCardProduct")

        // Log the updated list
        Log.d("k", mainActivityViewModel.addToCardProduct.value.toString())
    }









