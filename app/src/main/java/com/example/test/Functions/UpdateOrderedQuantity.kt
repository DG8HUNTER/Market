package com.example.test.Functions

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.test.Screens.mainActivityViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun updateOrderedQuantity(index:Int, quantity:Int){

    var array :MutableList<HashMap<String,Any?>> = mutableListOf()
    array = mainActivityViewModel.addToCardProduct.value.toMutableList()

    array.forEachIndexed { i, item ->
        if(i==index){
            item["quantity"]=quantity
            mainActivityViewModel.setValue(array,"addToCardProduct")
            return
        }

    }





}
