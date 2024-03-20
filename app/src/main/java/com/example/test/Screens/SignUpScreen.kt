package com.example.test.Screens

import android.app.Activity
import android.content.ContentValues.TAG
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.focus.FocusDirection
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
import com.example.test.ui.theme.customGreen
import com.example.test.ui.theme.mediumGray
import com.example.test.ui.theme.onSurface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {

    val auth = Firebase.auth
    val focusManager = LocalFocusManager.current

    var email: String? by remember {
        mutableStateOf(null)
    }

    var password: String ? by remember {
        mutableStateOf(null)
    }
    var emailRequirementError: Boolean by remember {
        mutableStateOf(false)
    }
    var passwordRequirementError: Boolean by remember {
        mutableStateOf(false)
    }

    var passwordErrorMessage: String by remember {
        mutableStateOf("Required Field")
    }

    var emailErrorMessage: String? by remember {
        mutableStateOf("Required Field")
    }

    var createButtonClicked by remember {
        mutableStateOf(false)
    }

    val clearIconColor = animateColorAsState(
        targetValue = if (email != null) mediumGray else Color.Transparent,
        animationSpec = tween(1000, easing = FastOutSlowInEasing)
    )

    var passVisibility by remember {
        mutableStateOf(false)
    }

    val passTrailingIconColor = animateColorAsState(
        targetValue = if (password == null) Color.Transparent else mediumGray,
        animationSpec = tween(
            easing = FastOutSlowInEasing
        )
    )

    var passwordVerification: String? by remember {
        mutableStateOf(null)
    }

    var passwordVerificationVisibility: Boolean by remember {
        mutableStateOf(false)
    }
    var passwordVRequirementError: Boolean by remember {
        mutableStateOf(false)
    }

    var passwordVErrorMessage: String by remember {
        mutableStateOf("Required Field")
    }

    var passwordVerificationIconColor = animateColorAsState(
        targetValue = if (passwordVerification == null) Color.Transparent else mediumGray,
        animationSpec = tween(
            easing = FastOutSlowInEasing
        )
    )
    var compatibility by remember {
        mutableStateOf(true)
    }
    var legitPasswordLength by remember{
        mutableStateOf(true)
    }
    var creatingAccount by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val signInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!,navController,"SignUp")
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
    var googleButtonClicked by  remember{
        mutableStateOf(false)
    }






    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)

    ) {
        Text(
            text = "Sign Up",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OutlinedTextField(value = if (email != null) email.toString() else "",
                        onValueChange =
                        {
                            if (it.isNotEmpty()) {
                                email = it
                                emailRequirementError = false

                            } else {
                                email = null
                                if (createButtonClicked) {
                                    emailRequirementError = true
                                    emailErrorMessage = "Required Field"
                                }


                            }

                        },
                        label = {
                            Text(
                                text = "Email",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },

                        placeholder = {
                            Text(
                                text = "Enter Your Email",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.LightGray
                            )

                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = "Email Icon", tint = mediumGray
                            )

                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                email = null
                                if (createButtonClicked) {
                                    emailRequirementError = true
                                }
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
                                if (!focusState.isFocused) {
                                    if (email != null && !isValidEmail(email)) {
                                        emailRequirementError = true
                                        emailErrorMessage = "Invalid Email"

                                    }

                                    if (password != null && password!!.length >= 6) {
                                        passwordRequirementError = false
                                    }

                                } else {
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
                            focusedTextColor = onSurface,
                            unfocusedIndicatorColor = mediumGray,
                            focusedIndicatorColor = customGreen,
                            cursorColor = onSurface,
                            focusedLabelColor = customGreen,
                            unfocusedLabelColor = mediumGray,
                            containerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent


                            ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)


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
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OutlinedTextField(
                        value = if (password != null) password.toString() else "",
                        onValueChange = {
                            password = if(it.isNotEmpty()){
                                it
                            }else{
                                null
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
                                color = mediumGray

                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "",
                                tint = mediumGray
                            )

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
                                var pass = password
                                if (focusState.isFocused) {
                                    if (passwordRequirementError && passwordErrorMessage == "Required Field") {
                                        passwordRequirementError = false
                                    }
                                    if (email == null) {
                                        emailRequirementError = true
                                        emailErrorMessage = "Required Field"
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
                            focusedTextColor = onSurface,
                            unfocusedIndicatorColor = mediumGray,
                            focusedIndicatorColor = customGreen,
                            cursorColor = onSurface,
                            focusedLabelColor = customGreen,
                            unfocusedLabelColor = mediumGray,
                            containerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        isError = passwordRequirementError
                    )
                   if(password!=null){
                       if(password!!.length >=1 && password!!.length<6){
                           passwordRequirementError=true
                           passwordErrorMessage="Password must be at least 6 characters"
                       }
                       else{
                           passwordRequirementError=false

                       }
                   }


                    if (passwordRequirementError) {
                        Text(
                            text = " $passwordErrorMessage",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, top = 0.dp)
                        )
                    }




                }


            }

            item {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    OutlinedTextField(value = if (passwordVerification != null) passwordVerification.toString() else "",
                        onValueChange = {
                            if(it.isNotEmpty()){
                                passwordVerification=it
                            }
                            else{
                               passwordVerification=null
                                compatibility=true
                            }

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
                                text = "Reenter Password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.LightGray

                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "",
                                tint = mediumGray
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVerificationVisibility = !passwordVerificationVisibility
                            }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVerificationVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                    contentDescription = if (passwordVerificationVisibility) "ic_visibility" else "ic_visibility_off",
                                    tint = passwordVerificationIconColor.value
                                )
                            }
                        },
                        visualTransformation = if (passwordVerificationVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    passwordVRequirementError=false


                                    if (password == null) {
                                        passwordRequirementError = true
                                        passwordErrorMessage = "Required Field"
                                    }


                                    if (email == null) {
                                        emailRequirementError = true
                                        emailErrorMessage = "Required Field"
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
                            focusedTextColor = onSurface,
                            unfocusedIndicatorColor = mediumGray,
                            focusedIndicatorColor = customGreen,
                            cursorColor = onSurface,
                            focusedLabelColor = customGreen,
                            unfocusedLabelColor = mediumGray,
                            containerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                            ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                if(passwordVerification==null){
                                    passwordRequirementError=true
                                    passwordErrorMessage="RequiredField"
                                }
                            }
                        ),
                        isError = passwordVRequirementError
                    )
                    if(passwordVerification!=null && password!=passwordVerification){
                        compatibility=false
                        passwordVRequirementError=true
                        passwordVErrorMessage="Passwords do not match"
                    }

                    if(passwordVerification!=null && password==passwordVerification){
                        compatibility=true
                        passwordVRequirementError=false

                    }

                    if (!compatibility || passwordVRequirementError) {
                        Text(
                            text = passwordVErrorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, top = 0.dp)
                        )


                    }

                    }


                }

            item {

                Spacer(modifier = Modifier.size(10.dp))
            }

            item{

                Button(onClick = {

                    focusManager.clearFocus()

                    if(email==null){
                        emailRequirementError=true
                        emailErrorMessage="Required Field"
                    }

                    if(password==null){
                        passwordRequirementError=true
                        passwordErrorMessage="Required Field"
                    }

                    if(passwordVerification==null){
                        passwordVRequirementError=true
                        passwordVErrorMessage="Required Field"
                    }

                    if(!emailRequirementError && !passwordRequirementError && !passwordVRequirementError){
                        creatingAccount=true

                        auth.createUserWithEmailAndPassword(email!!, password!!)
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success")
                                    val user = auth.currentUser?.uid.toString()

                                   // navController.navigate("userCredentials/$user")
                                    navController.navigate(route="PersonalInfo")

                                    //navgate to userCrendentialScreen
                                   // updateUI(user)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    emailRequirementError=true
                                    emailErrorMessage="The email address is already in use by another account"
                                    creatingAccount=false


                                }
                            }


                    }



                },colors=ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp)), contentPadding = PaddingValues()
                ) {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = customGreen, shape = RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,){

                        Row( modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center){
                            Text(
                                text=if(creatingAccount) "Creating Account" else "Create Account",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                                )
                            if(creatingAccount) {
                                Spacer(modifier = Modifier.width(5.dp))
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            }
                            }




                        }



                    }





                }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account ?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Text(
                        text = " Sign In ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = customGreen,
                        modifier = Modifier.clickable {
                            navController.navigate(route = "SignInScreen") {
                                popUpTo("SignUpScreen")
                            }
                        })
                }
            }

            item {
                Spacer(modifier = Modifier.size(20.dp))
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
                Spacer(modifier = Modifier.size(20.dp))
                Button(
                    onClick = {
                        googleButtonClicked=true
                        signInLauncher.launch(googleSignInClient.signInIntent) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = mediumGray
                )
                        ,
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
                           ,
                        contentAlignment = Alignment.Center
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
                                text = if(googleButtonClicked) "Signing Up" else "Sign Up With Google",
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
                                    color = customGreen
                                )
                            }
                        }
                    }
                }
            }




            }



            }



        }
fun firebaseAuthWithGoogle(idToken: String, navController: NavController,Screen:String) {
    val auth = FirebaseAuth.getInstance()
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser?.uid.toString()
                if(Screen=="SignIn") {
                    navController.navigate("UserCredentials/$user") {
                        popUpTo(0)
                    }
                }else {
                    navController.navigate("PersonalInfo") {
                        popUpTo(0)
                        navController.navigate("SignUpScreen")
                    }
                }
                // Handle successful authentication
            } else {
                // If sign in fails, display a message to the user.
                // Handle failed authentication
            }
        }
}






    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
