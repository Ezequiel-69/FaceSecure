package com.example.facesecure.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.facesecure.R

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "¡Hola! ¿ya estás registrado?")
        Button(
            onClick = { 
                val mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
                mediaPlayer.setOnCompletionListener { mp ->
                    navController.navigate("login")
                    mp.release()
                }
                mediaPlayer.start()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Mi rostro ya está registrado")
        }
        Button(
            onClick = { 
                val mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
                mediaPlayer.setOnCompletionListener { mp ->
                    navController.navigate("register")
                    mp.release()
                }
                mediaPlayer.start()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(text = "Aún no he registrado mi rostro")
        }
    }
}
