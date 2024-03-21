package com.example.test.Screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
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
import com.example.test.ui.theme.customGreen
import com.example.test.ui.theme.mediumGray
import com.example.test.ui.theme.onSurface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController){

    val auth = Firebase.auth
    val interactionSource = remember { MutableInteractionSource() }
    var email: String? by remember {
        mutableStateOf(null)
    }
    var isEmailEmpty: Boolean by remember {
        mutableStateOf(true)
    }
    val clearIconColor = animateColorAsState(
        targetValue = if (!isEmailEmpty) mediumGray else Color.Transparent,
        animationSpec = tween(1000, easing = FastOutSlowInEasing)
    )
    var password: String? by remember {
        mutableStateOf(null)
    }
    var passVisibility by remember {
        mutableStateOf(false)
    }
    var isPasswordEmpty by remember {
        mutableStateOf(true)
    }
    var emailRequirementError by remember {
        mutableStateOf(false)
    }
    var passwordRequirementError by remember {
        mutableStateOf(false)
    }
    var signInButtonClicked by remember {
        mutableStateOf(false)
    }
    var isSigningIn by remember {
        mutableStateOf(false)
    }
    var signInError by remember {
        mutableStateOf(false)
    }
    var errorMessage: String? by remember {
        mutableStateOf(null)
    }
    var emailErrorMessage: String? by remember {
        mutableStateOf(null)
    }
    var passwordErrorMessage: String? by remember {
        mutableStateOf(null)
    }
    val passTrailingIconColor = animateColorAsState(
        targetValue = if (isPasswordEmpty) Color.Transparent else mediumGray,
        animationSpec = tween(
            easing = FastOutSlowInEasing
        )
    )

    val context = LocalContext.current
    var googleButtonClicked by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    val signInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!,navController,"SignIn")
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Sign In",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OutlinedTextField(value = if (email != null) email.toString() else "",
                        onValueChange = {
                            if (it.isNotEmpty()) {
                                email = it
                                isEmailEmpty = false

                            } else {
                                email = null
                                isEmailEmpty = true
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
                                tint = mediumGray
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                email = null
                                isEmailEmpty = true
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
                                    errorMessage = null
                                    signInError=false
                                }
                            }
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(5.dp))
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(5.dp)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor =onSurface,
                            containerColor = Color.Transparent,
                            unfocusedIndicatorColor = mediumGray,
                            focusedIndicatorColor = customGreen,
                            cursorColor = onSurface,
                            focusedLabelColor = customGreen,
                            unfocusedLabelColor = mediumGray,
                            errorContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        isError = (emailRequirementError || signInError)
                    )

                    if (emailRequirementError) {
                        Text(
                            text = "$emailErrorMessage",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, top = 0.dp)

                        )
                    }

                   // if (errorMessage == "The email address is badly formatted." || errorMessage == "There is no user record corresponding to this identifier. The user may have been deleted.") {
                  //      emailRequirementError = true
                 //       emailErrorMessage = "Email does not exist."
                 //   }
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OutlinedTextField(value = if (password != null) password.toString() else "",
                        onValueChange = {
                            if (it.isNotEmpty()) {
                                password = it
                                isPasswordEmpty = false
                            } else {
                                password = null
                                isPasswordEmpty = true
                            }
                        },
                        label = {
                            Text(
                                text = "Password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Enter Password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.LightGray
                            )
                        },
                        leadingIcon = {
                          Icon(imageVector = Icons.Filled.Lock, contentDescription ="Lock",tint= mediumGray )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                passVisibility = !passVisibility
                            }) {
                                Icon(
                                    painter = painterResource(id = if (passVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                    contentDescription = if (passVisibility) "ic_visibility" else "ic_visibility_off",
                                    tint = passTrailingIconColor.value
                                )
                            }
                        },
                        visualTransformation = if (!passVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                        modifier = Modifier
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    signInError=false
                                    passwordRequirementError = false
                                    errorMessage = null
                                    if (email == null) {
                                        emailRequirementError = true
                                        emailErrorMessage = "Required Field"
                                    } else {
                                        if (email != null && !isValidEmail(email)) {
                                            emailRequirementError = true
                                            emailErrorMessage = "Invalid Email"
                                        } else {
                                            emailRequirementError = false
                                        }
                                    }
                                }
                            }
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(5.dp))
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(5.dp)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor =onSurface,
                            containerColor = Color.Transparent,
                            unfocusedIndicatorColor = mediumGray,
                            focusedIndicatorColor = customGreen,
                            cursorColor = onSurface,
                            focusedLabelColor = customGreen,
                            unfocusedLabelColor = mediumGray,
                            errorContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ), isError =(passwordRequirementError||signInError)

                    )
                    if (passwordRequirementError) {
                        Text(
                            text = "$passwordErrorMessage",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, top = 0.dp)
                        )
                    }
                    if (signInError) {
//                            //"The password is invalid or the user does not have a password."
                        errorMessage?.toString()?.let { Text(text= "Invalid email or password. Please check your credentials and try again.",color=MaterialTheme.colorScheme.error,modifier=Modifier.fillMaxWidth())}
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Forgot Password ?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = customGreen,
                        modifier = Modifier.clickable {
                            navController.navigate(route="ResetPassword")
                        }

//                            .clickable(interactionSource=interactionSource, indication = null){
//                                focusManager.clearFocus()
//                                if (email == null) {
//                                    emailRequirementError = true
//                                    emailErrorMessage = "Required Field"
//                                } else {
//                                    if (email != null && !isValidEmail(email)) {
//                                        emailRequirementError = true
//                                        emailErrorMessage = "Invalid Email"
//                                    } else {
//
//                                    }
                        // }
                        //        },
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = {
                        if (email == null) {
                            emailRequirementError = true
                            emailErrorMessage = "Required Field"
                        }

                        if (password == null) {
                            passwordRequirementError = true
                            passwordErrorMessage = "Required Field"
                        }

                        signInButtonClicked = true
                        focusManager.clearFocus()
                        if (!emailRequirementError && !passwordRequirementError) {
                            isSigningIn = true
                            auth.signInWithEmailAndPassword(email!!, password!!)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success")
                                        val userUi = auth.currentUser?.uid.toString()
                                        navController.navigate("Home/$userUi"){
                                            popUpTo(0)

                                        }
                                     //   mainActivityViewModel.setValue("Home","_selectedButton")
                                    //    mainActivityViewModel.setValue("General","_tabSelected")
                                    //    mainActivityViewModel.setValue(0,"_state")
                                      //  navController.navigate(route = "MainPage/$userUi"){
                                      //      popUpTo(0)
                                      //  }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                                        isSigningIn = false
                                        signInButtonClicked = false
                                        signInError = true
                                        errorMessage = task.exception?.message.toString()
                                    }
                                }
                            Log.d(
                                "currentuser", EmailAuthProvider.getCredential(
                                    email!!,
                                    password!!
                                ).toString()
                            )
                        }
                    },
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
                            .background(color = customGreen, shape = RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isSigningIn) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Signing In",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            }
                        } else {
                            Text(
                                text = "Sign In",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account ?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Text(
                        text = " Sign Up ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = customGreen,
                        modifier = Modifier.clickable {
                            navController.navigate(route = "SignUpScreen"){

                                popUpTo(route="SignInScreen"){
                                    saveState=true
                                }
                                launchSingleTop = true
                            }

                        })
                }
            }
            item {



                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.47f)
                            .height(1.dp)
                            .background(color = mediumGray)
                            .clip(shape = RoundedCornerShape(20.dp))
                    )
                    Text(
                        text = " OR ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = mediumGray
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(1.dp)
                            .background(color = mediumGray)
                            .clip(shape = RoundedCornerShape(20.dp))
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = {
                        googleButtonClicked=true
                        signInLauncher.launch(googleSignInClient.signInIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(10.dp),
                            color = mediumGray
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                            .clip(shape = RoundedCornerShape(10.dp))
                            , contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "Google icon",
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if(googleButtonClicked)"Signing In" else "Sign in with Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.height(24.dp)
                            )
                            if (googleButtonClicked) {
                                Spacer(modifier = Modifier.width(8.dp))
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(16.dp),
                                    color = Green
                                )
                            }
                        }
                    }
                }
            }
        }
    }



}

