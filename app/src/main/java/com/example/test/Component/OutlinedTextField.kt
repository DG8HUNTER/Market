package com.example.test.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.Screens.mainActivityViewModel
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.mediumGray
import com.example.test.ui.theme.onSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
   name: String,
   firstNameColorIcon: Color,
   lastNameColorIcon: Color,
   phoneNumberColorIcon: Color,
   firstNameErrorMessage:String,
   lastNameErrorMessage:String,
   phoneNumberErrorMessage:String,
   focusManager: FocusManager
){




   Column(modifier=Modifier
      .fillMaxWidth()

   ){

      OutlinedTextField(value =
      when (name) {
         "firstName" -> if (mainActivityViewModel.firstName.value != null) mainActivityViewModel.firstName.value.toString() else ""
         "lastName" -> if (mainActivityViewModel.lastName.value != null) mainActivityViewModel.lastName.value.toString() else ""
         else -> if (mainActivityViewModel.phoneNumber.value != null) mainActivityViewModel.phoneNumber.value.toString() else ""

      }
         , onValueChange = {

            when (name) {
               "firstName" -> mainActivityViewModel.setValue(it,"firstName")
               "lastName" -> mainActivityViewModel.setValue(it,"lastName")
               else -> mainActivityViewModel.setValue(it , "phoneNumber")

            }


         },
         label = {

            Text(text = when(name) {
               "firstName" -> "FirstName"
               "lastName" -> "LastName"
               else -> "PhoneNumber"


            } , fontSize = 15.sp,
               fontWeight = FontWeight.Medium)
         },

         placeholder = {
            Text(text = when(name) {
               "firstName" -> "FirstName"
               "lastName" -> "LastName"
               else -> "PhoneNumber"


            } , fontSize = 15.sp,
               fontWeight = FontWeight.Medium)
         },

         leadingIcon = {
            Icon(imageVector =
            when(name){
               "firstName"-> Icons.Filled.Person
               "lastName"->Icons.Filled.Person
               else -> Icons.Filled.Phone
            }


               , contentDescription ="",

               tint= mediumGray

            )


         },

         trailingIcon = {
            IconButton(onClick = { when(name){
               "firstName"->mainActivityViewModel.setValue(null , "firstName")
               "lastName"->mainActivityViewModel.setValue(null , "lastName")
               else -> mainActivityViewModel.setValue(null,"phoneNumber")
            } }) {
               Icon(imageVector = Icons.Filled.Clear, contentDescription ="clearIcon",
                  tint= when(name){
                     "firstName"->firstNameColorIcon
                     "lastName"->lastNameColorIcon
                     else -> phoneNumberColorIcon
                  }

               )

            }


         },

         keyboardOptions = KeyboardOptions(
            keyboardType = when(name){
               "firstName"-> KeyboardType.Text
               "lastName"->KeyboardType.Text
               else -> KeyboardType.Phone
            },
            imeAction =
            when(name){
               "firstName"-> ImeAction.Next
               "lastName"-> ImeAction.Next
               else -> ImeAction.Done
            }


         ), keyboardActions = KeyboardActions(
            onDone = {focusManager.clearFocus()}
         ),
         modifier= Modifier.onFocusEvent {
            focusState ->
            if(focusState.isFocused){
               when(name){
                  "firstName"-> mainActivityViewModel.setValue(false,"firstNameError")
                  "lastName"-> mainActivityViewModel.setValue(false,"lastNameError")
                  else ->  mainActivityViewModel.setValue(false,"phoneNumberError")
               }
            }
         }
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(5.dp))
            .background(shape = RoundedCornerShape(5.dp), color = Color.Transparent)
         ,    colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = onSurface,
            unfocusedIndicatorColor = mediumGray,
            focusedIndicatorColor = customColor,
            cursorColor = onSurface,
            focusedLabelColor = customColor,
            unfocusedLabelColor = mediumGray,
            containerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
         ),

         isError = when(name){
            "firstName"-> mainActivityViewModel.firstNameError.value
            "lastName"-> mainActivityViewModel.lastNameError.value
            else -> mainActivityViewModel.phoneNumberError.value
         }



      )

      when(name){
         "firstName"-> if(mainActivityViewModel.firstNameError.value) Text(text =firstNameErrorMessage , color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium )
         "lastName"-> if (mainActivityViewModel.lastNameError.value) Text(text = lastNameErrorMessage, color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium )
         else -> if(mainActivityViewModel.phoneNumberError.value) Text(text=phoneNumberErrorMessage , color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium )
      }


   }

}








