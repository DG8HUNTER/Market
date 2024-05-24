package com.example.test.Screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.integrity.internal.c
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

@Composable

fun Upload(){
val context = LocalContext.current
    Column(modifier= Modifier.fillMaxSize()){


        Button(onClick = {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val  imagesRef = storageRef.child("images")
            val picName = "Image.jpg"
            val picRef = imagesRef.child(picName)
            val file = Uri.fromFile(File("C:\\users\\user\\Downloads\\Jesus-Orthodox.jpg"))
            val riversRef = storageRef.child("images/${file.lastPathSegment}")
            val uploadTask = riversRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads

                Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show()
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }


        }){
            Text("Upload")
        }
    }


}