package com.example.test.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.ui.theme.superLightGray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreInfo(navController: NavController, storeId:String, storeName:String) {

    // State to track whether the bottom sheet is expanded or not
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = SheetState(skipHiddenState = false , skipPartiallyExpanded = false))
    var isClicked by remember {
        mutableStateOf(false)
    }



      /*  if(isVisible){
            ModalBottomSheet(onDismissRequest = {isVisible=false}, sheetState = bottomSheetState) {
                Column(modifier=Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Hello world")
                }

            }
        }*/

        BottomSheetScaffold(
            sheetContent = {
                Column(){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {

                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "clear",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(0.5f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Hello world")
                    }
                }


            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContainerColor = superLightGray,


            ) {
Column(modifier= Modifier
    .fillMaxSize()
    .padding(20.dp)) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            navController.popBackStack()

        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "KeyboardArrowLeft",
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = storeName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )

        IconButton(onClick = {
            if (!scaffoldState.bottomSheetState.isVisible ) {
                scope.launch { scaffoldState.bottomSheetState.expand() }
            } else {
                scope.launch { scaffoldState.bottomSheetState.hide() }
            }
            isClicked = !isClicked

        }) {
            Icon(
                imageVector = if (!scaffoldState.bottomSheetState.isVisible) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                contentDescription = "",
                modifier = Modifier.size(25.dp),
                tint = Color.Gray
            )

        }
    }





}
        }










}

 /*@Composable
     fun bottomSheetContent(){
     Column {
         Text("Bottom Sheet Content")
         Button(onClick = {scope.launch{ bottomSheetState.hide()} }) {
             Text("Close Bottom Sheet")
         }
     }
     }

*/