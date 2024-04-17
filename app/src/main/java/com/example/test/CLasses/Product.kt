package com.example.test.CLasses

data class Product(
    var productId:String,
    val name: String,
    val description: String,
    val price: Double,
    var quantity: Int ,
    var image :String,
    var storeId:String,
    var inventory :Int,
    var category:String
)
