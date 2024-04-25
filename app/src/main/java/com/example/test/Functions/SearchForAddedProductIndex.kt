package com.example.test.Functions

import com.example.test.Screens.mainActivityViewModel

fun SearchForAddedProductIndex(productId:String):Int{

   mainActivityViewModel.addToCardProduct.value.forEachIndexed { index, product ->
       if(product["productId"]==productId){
           return index
       }
   }
    return -1


}

