package com.example.test.Functions

import com.example.test.Screens.mainActivityViewModel

fun searchCategory(value:String){
    val array = mainActivityViewModel.categories.value

    for(cat in array){
        if(cat==value){
            return
        }
    }
    mainActivityViewModel.addToCategory(value)
}
