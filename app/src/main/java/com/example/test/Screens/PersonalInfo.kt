package com.example.test.Screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Component.TextFieldComponent
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.mediumGray
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val mainActivityViewModel= MainActivityViewModel()
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalInfo(navController: NavController ,screen:String){
val userUid = Firebase.auth.currentUser?.uid.toString()
    val context= LocalContext.current

    var submitting by remember {
        mutableStateOf(false)
    }

   // mainActivityViewModel.setValue("georgio","firstName")
   // mainActivityViewModel.setValue("fayad","lastName")
  //  mainActivityViewModel.setValue("71675525","phoneNumber")
    val firstNameColorIcon = animateColorAsState(targetValue = if(mainActivityViewModel.firstName.value==null) Color.Transparent else mediumGray,
        label = "", animationSpec = tween(1000, easing = FastOutSlowInEasing)

    )

    val lastNameColorIcon = animateColorAsState(targetValue = if(mainActivityViewModel.lastName.value==null) Color.Transparent else mediumGray,
        label = "", animationSpec = tween(1000, easing = FastOutSlowInEasing)

    )

    val phoneNumberColorIcon = animateColorAsState(targetValue = if(mainActivityViewModel.phoneNumber.value==null) Color.Transparent else mediumGray,
        label = "", animationSpec = tween(1000, easing = FastOutSlowInEasing)

    )

 var firstNameError:Boolean by remember {
     mutableStateOf(false)
 }

    var lastNameError:Boolean by remember {
        mutableStateOf(false)
    }
    var phoneNumberError:Boolean by remember {
        mutableStateOf(false)
    }
    val firstNameErrorMessage by remember {
       mutableStateOf("Required Field")}

        val lastNameErrorMessage by remember {
            mutableStateOf("Required Field")
        }

    var phoneNumberErrorMessage by remember {
        mutableStateOf("Required Field")
    }

    val focusManager : FocusManager = LocalFocusManager.current


    val db = Firebase.firestore


    LaunchedEffect(key1 = true) {
        if(screen!="SignUp"){
            db.collection("Users").document(userUid)
                .get()
                .addOnSuccessListener { document->
                    if(document !=null)
                    mainActivityViewModel.setValue(document.data?.get("FirstName").toString() , "firstName")
                    mainActivityViewModel.setValue(document.data?.get("LastName").toString() , "lastName")
                    mainActivityViewModel.setValue(document.data?.get("PhoneNumber").toString() , "phoneNumber")

                }
        }

        else {
            mainActivityViewModel.setValue(null , "firstName")
            mainActivityViewModel.setValue(null, "lastName")
            mainActivityViewModel.setValue(null , "phoneNumber")
        }

    }



    Column(modifier= Modifier
        .fillMaxSize()
        .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Row(modifier=Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.spacedBy(5.dp) , verticalAlignment = Alignment.CenterVertically){
           IconButton(onClick = { navController.popBackStack()

           }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription ="Arrow Back" )}

                Text(text="Personal Info" , fontSize = 25.sp , fontWeight = FontWeight.Bold)

        }


        Column(modifier=Modifier
            .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally


        ){

                TextFieldComponent(
                    name = "firstName",
                    firstNameColorIcon = firstNameColorIcon.value,
                    lastNameColorIcon =lastNameColorIcon.value ,
                    phoneNumberColorIcon =phoneNumberColorIcon.value ,
                    firstNameErrorMessage =firstNameErrorMessage,
                    lastNameErrorMessage =lastNameErrorMessage,
                    phoneNumberErrorMessage = phoneNumberErrorMessage,
                    focusManager= focusManager

                )

            TextFieldComponent(
                name = "lastName",
                firstNameColorIcon = firstNameColorIcon.value,
                lastNameColorIcon =lastNameColorIcon.value ,
                phoneNumberColorIcon =phoneNumberColorIcon.value ,
                firstNameErrorMessage =firstNameErrorMessage,
                lastNameErrorMessage =lastNameErrorMessage,
                phoneNumberErrorMessage = phoneNumberErrorMessage,
                focusManager= focusManager

            )

            TextFieldComponent(
                name = "phoneNumber",
                firstNameColorIcon = firstNameColorIcon.value,
                lastNameColorIcon =lastNameColorIcon.value ,
                phoneNumberColorIcon =phoneNumberColorIcon.value ,
                firstNameErrorMessage =firstNameErrorMessage,
                lastNameErrorMessage =lastNameErrorMessage,
                phoneNumberErrorMessage = phoneNumberErrorMessage,
                focusManager= focusManager

            )





        }


        Button(onClick = {
            focusManager.clearFocus()

            if(mainActivityViewModel.firstName.value==null){
                mainActivityViewModel.setValue(true , "firstNameError")
            }
            if(mainActivityViewModel.lastName.value==null){
                mainActivityViewModel.setValue(true , "lastNameError")
            }

            if(mainActivityViewModel.phoneNumber.value==null){
                phoneNumberErrorMessage="Required Field"
                mainActivityViewModel.setValue(true , "phoneNumberError")

            }else{
                if(mainActivityViewModel.phoneNumber.value!!.length!=8){
                    phoneNumberErrorMessage="Phone Number Should be 8 digits "
                    mainActivityViewModel.setValue(true , "phoneNumberError")


                }
            }
            if(!mainActivityViewModel.firstNameError.value && !mainActivityViewModel.lastNameError.value&& !mainActivityViewModel.phoneNumberError.value){
                submitting=true

                if(screen=="SignUp") {
                    val user = hashMapOf(
                        "UserID" to userUid,
                        "FirstName" to mainActivityViewModel.firstName.value,
                        "LastName" to mainActivityViewModel.lastName.value,
                        "Location" to "",
                        "PhoneNumber" to mainActivityViewModel.phoneNumber.value,
                        "role" to "User"
                    )

                    db.collection("Users").document(userUid)
                        .set(user)
                        .addOnSuccessListener {

                            navController.navigate(route = "LocationScreen")


                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed To insert user", Toast.LENGTH_LONG)
                                .show()
                        }

                }    else{
                    db.collection("Users").document(userUid)
                        .update("FirstName", mainActivityViewModel.firstName.value.toString())

                    db.collection("Users").document(userUid)
                        .update("LastName", mainActivityViewModel.lastName.value.toString())

                    db.collection("Users").document(userUid)
                        .update("PhoneNumber" , mainActivityViewModel.phoneNumber.value.toString())

                    navController.popBackStack()

                }

            }

        }

            ,

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp)),
            contentPadding = PaddingValues()

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(color = customColor, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Row(modifier=Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){
                    Text(
                        text = if(screen=="SignUp")
                        {if(!submitting)"Submit" else "Submitting"}
                        else{
                            if(!submitting)"Update" else "Updating"
                        }

                        ,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    if(submitting){
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(
                            modifier=Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    }
                }

            }



    }



    }






    //TextField(name ="firstName" , firstNameColorIcon,lastNameColorIcon,phoneNumberColorIcon ,firstNameError,lastNameError,phoneNumberError)




}

