package com.example.test.Functions

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

import com.example.test.Screens.mainActivityViewModel

@RequiresApi(Build.VERSION_CODES.O)


    fun updateOrderedQuantity(index: Int, quantity: Int) {
        val productList = mainActivityViewModel.addToCardProduct.value.toMutableList()
    val product = productList[index]






    val totalPrice =if(productList[index]["discount"].toString().toInt()==0){
        productList[index]["price"].toString().toFloat()* quantity.toString().toFloat()
    }else{
        val discountPrice =(((productList[index]["price"].toString().toFloat())* quantity.toString().toFloat())) *(productList[index]["discount"].toString().toFloat()/100f)
        (((productList[index]["price"].toString().toFloat())* quantity.toString().toFloat())-discountPrice)

    }

    val totalProfit =  if(product["discount"].toString().toInt()==0){
        quantity.toString().toFloat()*product["profitPerItem"].toString().toFloat()
    }else {
        val unitPrice = product["price"].toString().toFloat()- product["profitPerItem"].toString().toFloat()
        val priceAfterDiscount= product["price"].toString().toFloat()-(product["price"].toString().toFloat()* (product["discount"].toString().toFloat()/100f))
        Log.d("unitPrice", unitPrice.toString())
        Log.d("priceAfterDiscount", priceAfterDiscount.toString())
        (priceAfterDiscount-unitPrice)*quantity.toString().toFloat()
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









