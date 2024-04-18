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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.ui.theme.customColor
import com.example.test.ui.theme.mediumGray
import com.example.test.ui.theme.onSurface
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ResetPassword(navController: NavController){

    var email: String? by remember {
        mutableStateOf(null)
    }

    var emailRequirementError :Boolean by remember{
        mutableStateOf(false)
    }

    var emailErrorMessage: String ? by remember {
        mutableStateOf("Required Field")
    }

    val clearIconColor = animateColorAsState(targetValue =if(email==null)Color.Transparent else mediumGray, animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = ""
    )

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    Column(modifier= Modifier
        .fillMaxWidth()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ){
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            modifier=Modifier.fillMaxWidth()
        ){

            IconButton(onClick = {
                navController.navigate("SignInScreen")


            }) {

               Icon(imageVector =Icons.Filled.ArrowBack, contentDescription ="Arrow Back", modifier = Modifier.size(25.dp))

            }

            Text(
                text = "Password Reset"  ,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,

            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enter your email address we will send you an email to reset your password.",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = mediumGray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(value = if (email != null) email.toString() else "",
                onValueChange = {
                    if (it.isNotEmpty()) {
                        email = it


                    } else {
                        email = null

                    }
                },
                label = {
                    Text(text = "Email", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                },
                placeholder = {
                    Text(
                        text = "Create Email",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.LightGray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email icon",
                        tint = Color.LightGray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        email = null

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear Icon",
                            tint = clearIconColor.value
                        )
                    }
                },
                modifier = Modifier
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            emailRequirementError = false
                        }
                    }
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(5.dp))
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(5.dp)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor= onSurface,
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = mediumGray,
                    focusedIndicatorColor = customColor,
                    cursorColor = mediumGray,
                    focusedLabelColor = customColor,
                    unfocusedLabelColor = mediumGray,
                    errorContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                isError = emailRequirementError
            )

            if (emailRequirementError) {
                Text(
                    text = "$emailErrorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 0.dp)
                )
            }
        }

        Button(onClick = {
            focusManager.clearFocus()

                if(email==null){
                    emailRequirementError=true
                    emailErrorMessage="Required Field"
                }
                else {
                    if (!isValidEmail(email)) {
                        emailRequirementError = true
                        emailErrorMessage = "Invalid Email"
                    } else {

                        FirebaseAuth.getInstance().sendPasswordResetEmail(email!!)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Email sent successfully to reset your password",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(route = "SignInScreen")
                                } else {
                                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                                    emailRequirementError = true
                                    emailErrorMessage = "Email does not exist"
                                }
                            }


                    }
                }


        } ,colors = ButtonDefaults.buttonColors(
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
                    .background(
                        color = customColor,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Reset Password",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

            }


        }


        }



















    }



