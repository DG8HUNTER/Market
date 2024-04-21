package com.example.test.Functions

import com.example.test.Screens.mainActivityViewModel

fun SearchForOrderedProduct(productId:String):Boolean{

    var list = mainActivityViewModel.addToCardProduct.value.toMutableList()

    list.forEach {  item ->

        if(item["productId"]==productId){
            return true
        }

    }
    return false


}