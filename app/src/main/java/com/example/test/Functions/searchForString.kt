package com.example.test.Functions

import androidx.compose.runtime.Composable


fun searchForString(array:MutableList<String> , value:String): Boolean {
    for( item in array){
        if(item==value){
            return true
        }
    }
    return false

}