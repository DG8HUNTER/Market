package com.example.test.Screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.mediumGray
import com.example.test.ui.theme.onSurface
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navController: NavController){
    val db = Firebase.firestore
    var buttonText:String by remember {
            mutableStateOf ("Reset Password")
        }
        var email :String?  by remember{
            mutableStateOf(null)
        }
        val focusManager= LocalFocusManager.current
        var emailRequirementError by remember {
            mutableStateOf(false)
        }
        var emailErrorMessage: String? by remember {
            mutableStateOf(null)
        }
        var isEmailEmpty: Boolean by remember {
            mutableStateOf(true)
        }
        val context = LocalContext.current
        var newPassword: String? by remember {
            mutableStateOf(null)
        }
        var newPasswordVisibility: Boolean by remember {
            mutableStateOf(false)
        }
        val newPasswordClearIcon by animateColorAsState(
            targetValue = if (newPassword != null) mediumGray else Color.Transparent,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )

        var passwordVerification: String? by remember {
            mutableStateOf(null)
        }
        var passwordVerificationVisibility: Boolean by remember {
            mutableStateOf(false)

        }
        val passwordVerificationClearIcon by animateColorAsState(
            targetValue = if (passwordVerification != null) mediumGray else Color.Transparent,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        var newPasswordError by remember{
            mutableStateOf(false)
        }
        var passwordVError by remember {
            mutableStateOf(false)

        }
        var newPasswordErrorMessage :String? by remember{
            mutableStateOf(null)
        }
        var passwordVErrorMessage:String?  by remember{
            mutableStateOf(null)
        }
        val clearIconColor = animateColorAsState(
            targetValue = if (!isEmailEmpty) mediumGray else Color.Transparent,
            animationSpec = tween(1000, easing = FastOutSlowInEasing)
        )

    var clicked by remember {
        mutableStateOf(false)
    }
    var passwordFocused  by remember {
        mutableStateOf(false)
    }
    var resettingState by remember{
        mutableStateOf(false)
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                    navController.popBackStack()

                }) {
                    Icon(
                       imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "arrow left icon",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )

                }
                Text(
                    text = "Password Change" ,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }


                Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start , verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    OutlinedTextField(value = if (newPassword != null) newPassword.toString() else "",
                        onValueChange = {
                            newPassword = if (it.isNotEmpty()) it else null


                        },
                        label = {
                            Text(
                                text = "New Password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,

                                )
                        },
                        placeholder = {
                            Text(
                                text = " Enter new password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.LightGray
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Lock icon",
                                tint = mediumGray,

                                )
                        },
                        trailingIcon = {
                            IconButton(onClick = { newPasswordVisibility = !newPasswordVisibility }) {
                                Icon(
                                    painter = painterResource(id = if (newPasswordVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                    contentDescription = if (newPasswordVisibility) "visibility icon" else "visibility off icon",
                                    tint = newPasswordClearIcon
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),

                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(focusDirection = FocusDirection.Down)
                            }
                        ),
                        visualTransformation = if (newPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier

                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    if (newPasswordError && newPasswordErrorMessage == "Required Field") {
                                        newPasswordError = false
                                    }
                                    clicked = false
                                    passwordFocused=true
                                }

                            }
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(5.dp))
                            .background(color = Color.Transparent, shape = RoundedCornerShape(5.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor= onSurface,
                            containerColor = Color.Transparent,
                            unfocusedIndicatorColor = mediumGray,
                            focusedIndicatorColor = customColor,
                            cursorColor = onSurface,
                            focusedLabelColor = customColor,
                            unfocusedLabelColor = mediumGray,
                            errorContainerColor = Color.Transparent
                        ) , isError = newPasswordError

                    )

                    if(newPassword!=null){
                        if(newPassword!!.length>=1&& newPassword!!.length<6){
                            newPasswordError=true
                            newPasswordErrorMessage="Password Should be at least 6 characters"

                        }
                        else{
                            newPasswordError=false
                            newPasswordErrorMessage="Required Field"
                        }

                    }

                   if(newPassword==null && passwordFocused){
                    newPasswordError=false
                    newPasswordErrorMessage="Required Field"}


                    if(newPasswordError){
                        Text(text ="$newPasswordErrorMessage" ,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, top = 0.dp)
                        )
                    }
                }
                Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start , verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    OutlinedTextField(
                        value = if (passwordVerification != null) passwordVerification.toString() else "",
                        onValueChange = {
                            passwordVerification = if (it.isNotEmpty()) it else null
                        },
                        label = {
                            Text(
                                text = "Verify Password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,

                                )
                        },
                        placeholder = {
                            Text(
                                text = "Retype Password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.LightGray
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Lock icon",
                                tint = mediumGray,

                                )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVerificationVisibility = !passwordVerificationVisibility
                            }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVerificationVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                    contentDescription = if (passwordVerificationVisibility) "visibility icon" else "visibility off icon",
                                    tint = passwordVerificationClearIcon
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),

                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                clicked=true
                                if(passwordVerification==null){
                                    passwordVError=true
                                    passwordVErrorMessage="Required Field"
                                }
                            }
                        ),
                        visualTransformation = if (passwordVerificationVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    if (passwordVError && passwordVErrorMessage == "Required Field") {
                                        passwordVError = false

                                    }
                                    if (passwordVerification == null) {
                                        passwordVError = false
                                    }

                                    if (newPassword == null) {
                                        newPasswordError = true
                                        newPasswordErrorMessage = "Required Field"
                                    }
                                    clicked = false
                                    passwordFocused=false

                                }
                            }
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(5.dp))
                            .background(color = Color.Transparent, shape = RoundedCornerShape(5.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor= onSurface,
                            containerColor = Color.Transparent,
                            unfocusedIndicatorColor = mediumGray,
                            focusedIndicatorColor = customColor,
                            cursorColor = onSurface,
                            focusedLabelColor = customColor,
                            unfocusedLabelColor = mediumGray,
                            errorContainerColor = Color.Transparent

                            ) , isError = passwordVError
                    )
                    if(passwordVerification!=null ){

                       if(passwordVerification==newPassword){
                           passwordVError=false
                       }
                        else{
                            passwordVError=true
                           passwordVErrorMessage="Passwords do not match"
                       }

                    }

                    if(passwordVerification==null && !clicked){
                        passwordVError=false
                        passwordVErrorMessage="Required Field"
                    }


                    if(passwordVError){
                        Text(
                            text = "$passwordVErrorMessage",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, top = 0.dp))
                    }

                }

            Button(
                onClick = {
                    clicked=true
                    passwordFocused=false
                    focusManager.clearFocus()

                    val auth = Firebase.auth
                    val currentUser = auth.currentUser
                    Log.d("userCurrent", currentUser?.email.toString())
                    if (newPassword == null) {
                        newPasswordError = true
                        newPasswordErrorMessage = "Required Field"

                    }
                    if (passwordVerification == null) {
                        passwordVError = true
                        passwordVErrorMessage = "Required Field"

                    }

                    if (!newPasswordError && !passwordVError) {
                        resettingState=true

                        currentUser!!.updatePassword(newPassword!!)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    mainActivityViewModel.setValue(newPassword, "password")
                                    Log.d("TAG", "User password updated.")
                                    Toast.makeText(
                                        context, "Password updated successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.popBackStack()
                                    navController.popBackStack()



                          }

                                 else {
                                     resettingState=false
                                    Toast.makeText(
                                        context, "Please re-enter your old password",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    // navController.navigate(route="PasswordSecurity/$userUi"){
                                    //     popUpTo(0)
                                    //    popUpTo(route="MainPage/$userUi")
                                }
                            }
                    }
                },

                colors = ButtonDefaults.buttonColors(
                   containerColor =  Color.Transparent
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
                        .background(
                            color = customColor,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row {
                        Text(
                            text = if(resettingState) "Changing" else "Change Password",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )

                        if(resettingState){
                            Spacer(modifier =Modifier.width(8.dp))
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier=Modifier.size(16.dp),
                                color=Color.White
                            )
                        }
                    }


                }


            }


        }



}