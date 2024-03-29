package com.example.test.Screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

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



    fun setValue(newValue:Any?, name:String){

        when(name){
            "firstName"-> if(newValue!=null)_firstName.value=newValue.toString() else _firstName.value=null
            "lastName"-> if(newValue!=null)_lastName.value=newValue.toString() else _lastName.value=null
            "phoneNumber"-> if(newValue!=null)_phoneNumber.value=newValue.toString() else _phoneNumber.value=null
            "firstNameError"-> if(newValue!=null)_firstNameError.value= newValue as Boolean
            "lastNameError" -> if(newValue!=null)_lastNameError.value=newValue as Boolean
            "phoneNumberError"-> if(newValue!=null)_phoneNumberError.value=newValue as Boolean
            "rotate" -> if (newValue!=null) _rotate.value=newValue as Boolean
        }
    }

}