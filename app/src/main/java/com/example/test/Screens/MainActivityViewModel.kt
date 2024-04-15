package com.example.test.Screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.test.addTo
import java.time.LocalTime

class MainActivityViewModel {

    private val _firstName: MutableState<String?> = mutableStateOf(null)
    val firstName : State<String?> = _firstName

    private val  _lastName :MutableState<String?> = mutableStateOf(null)

    val lastName :State<String?> = _lastName

    private val _phoneNumber :MutableState<String?> = mutableStateOf(null)

    val phoneNumber :State<String?> = _phoneNumber

    private val _firstNameError :MutableState<Boolean> = mutableStateOf(false)
    val firstNameError :State<Boolean> = _firstNameError

    private val _lastNameError :MutableState<Boolean> = mutableStateOf(false)
    val lastNameError :State<Boolean> = _lastNameError

    private val _phoneNumberError :MutableState<Boolean> = mutableStateOf(false)
    val phoneNumberError :State<Boolean> = _phoneNumberError

    private val  _rotate :MutableState<Boolean> = mutableStateOf(false)
    val rotate :State<Boolean> = _rotate

    private val _homeLaunchedEffectExecuted : MutableState<Boolean> = mutableStateOf(false)

    val homeLaunchedEffectExecuted :State<Boolean> = _homeLaunchedEffectExecuted


    @RequiresApi(Build.VERSION_CODES.O)
    private val _currentTime :MutableState<LocalTime> = mutableStateOf(LocalTime.now())
    @RequiresApi(Build.VERSION_CODES.O)
    val currentTime :State<LocalTime> = _currentTime


    @SuppressLint("MutableCollectionMutableState")
    private val _stores :MutableState<MutableList<HashMap<String , Any?>>> = mutableStateOf(mutableListOf())

    val stores : MutableState<MutableList<HashMap<String, Any?>>> = _stores

    fun addToStores(newValue :HashMap<String , Any?>){
        _stores.value= addTo(_stores, document =newValue)

    }

    private val _homeSnapshotEntry : MutableState<Boolean> = mutableStateOf(true)

    val homeSnapshotEntry : State<Boolean> = _homeSnapshotEntry


    private val _products :MutableState<MutableList<HashMap<String,Any?>>>  = mutableStateOf(mutableListOf())

    val products :State<MutableList<HashMap<String ,Any?>>> = _products

    fun addToProducts (newValue :HashMap<String , Any?>){
        _products.value= addTo(_products, document =newValue)

    }

    private val _favorites :MutableState<MutableList<HashMap<String,Any?>>>  = mutableStateOf(mutableListOf())

    val favorites : State<MutableList<HashMap<String ,Any?>>> = _favorites
    fun addToFavorites (newValue :HashMap<String , Any?>){
        _favorites.value= addTo(_favorites, document =newValue)

    }

    private val _categories :MutableState<MutableList<String>> = mutableStateOf(mutableListOf("All"))

    val categories :State<MutableList<String>> = _categories

    fun addToCategory (newValue:String){
        val newCategories = _categories.value
        newCategories.add(newValue)
        _categories.value=newCategories
    }

    private val _favoriteCategories : MutableState<MutableList<String?>> = mutableStateOf(mutableListOf())

    val favoriteCategories :State<MutableList<String?>> = _favoriteCategories


    fun addToFavoriteCategories (newValue:String){
        val newCategories = _favoriteCategories.value
        newCategories.add(newValue)
        _favoriteCategories.value=newCategories
    }

   private val _favoriteProducts :MutableState<MutableList<HashMap<String,Any?>>> = mutableStateOf(mutableListOf())

    val favoriteProducts :State<MutableList<HashMap<String,Any?>>> = _favoriteProducts




    @RequiresApi(Build.VERSION_CODES.O)
    fun setValue(newValue:Any?, name:String){

        when(name){
            "firstName"-> if(newValue!=null)_firstName.value=newValue.toString() else _firstName.value=null
            "lastName"-> if(newValue!=null)_lastName.value=newValue.toString() else _lastName.value=null
            "phoneNumber"-> if(newValue!=null)_phoneNumber.value=newValue.toString() else _phoneNumber.value=null
            "firstNameError"-> if(newValue!=null)_firstNameError.value= newValue as Boolean
            "lastNameError" -> if(newValue!=null)_lastNameError.value=newValue as Boolean
            "phoneNumberError"-> if(newValue!=null)_phoneNumberError.value=newValue as Boolean
            "rotate" -> if (newValue!=null) _rotate.value=newValue as Boolean
            "currentTime"-> if (newValue!=null) _currentTime.value= newValue as LocalTime
            "homeLaunchedEffectExecuted"-> if(newValue!=null) _homeLaunchedEffectExecuted.value=newValue as Boolean
            "stores"-> if(newValue!=null) _stores.value= newValue as MutableList<HashMap<String, Any?>>
            "homeSnapshotEntry"-> if(newValue!=null) _homeSnapshotEntry.value=newValue as Boolean
            "products"-> if(newValue!=null) _products.value= newValue as MutableList<HashMap<String, Any?>>
            "favorites"-> if(newValue!=null) _favorites.value= newValue as MutableList<HashMap<String, Any?>>
            "categories"-> if(newValue!=null) _categories.value = newValue as MutableList<String>
            "favorite categories"-> if(newValue!=null) _favoriteCategories.value = newValue as MutableList<String?>
            "favorite products"-> if(newValue!=null) _favoriteProducts.value= newValue as MutableList<HashMap<String, Any?>>
        }
    }





}