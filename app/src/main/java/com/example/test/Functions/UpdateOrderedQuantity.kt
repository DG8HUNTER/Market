package com.example.test.Functions

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.test.Screens.mainActivityViewModel

@RequiresApi(Build.VERSION_CODES.O)


    fun updateOrderedQuantity(index: Int, quantity: Int) {
        val productList = mainActivityViewModel.addToCardProduct.value.toMutableList()






    val totalPrice =if(productList[index]["discount"].toString().toInt()==0){
        productList[index]["price"].toString().toFloat()* quantity.toString().toFloat()
    }else{
        val discountPrice =(((productList[index]["price"].toString().toFloat())* quantity.toString().toFloat())) *(productList[index]["discount"].toString().toFloat()/100f)
        (((productList[index]["price"].toString().toFloat())* quantity.toString().toFloat())-discountPrice)

    }

    val tProfit :Float =quantity.toString().toFloat() *productList[index]["profitPerItem"].toString().toFloat()

    val totalProfit =if(productList[index]["discount"].toString().toInt()==0){
        tProfit
    }else{
        val discountPrice =tProfit * productList[index]["discount"].toString().toFloat()/100f
        (tProfit-discountPrice)

    }




    Log.d("Card",productList.toString())


// Create a new HashMap using the existing HashMap's contents
    val newHashMap= hashMapOf(
        "productId" to productList[index]["productId"],
        "name" to productList[index]["name"],
        "profitPerItem" to productList[index]["profitPerItem"],
        "inventory" to productList[index]["inventory"],
        "price" to productList[index]["price"],
        "discount"  to productList[index]["discount"],
        "quantity" to quantity,
        "totalPrice" to totalPrice,
        "image"  to productList[index]["image"],
        "storeId" to productList[index]["storeId"],
        "description" to productList[index]["description"],
        "totalProfit" to totalProfit



    )

    productList[index]=newHashMap

        // need a recomposition
      //  mainActivityViewModel.setValue(mutableListOf<HashMap<String,Any>>(), "addToCardProduct")
    mainActivityViewModel.setValue(productList.toMutableList(), "addToCardProduct")

        // Log the updated list
        Log.d("k", mainActivityViewModel.addToCardProduct.value.toString())
    }









