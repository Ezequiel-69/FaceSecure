package com.example.facesecure.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.facesecure.R

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current

    // Interaction sources for button press animations
    val interactionSource1 = remember { MutableInteractionSource() }
    val estaPresionado1 by interactionSource1.collectIsPressedAsState()

    val interactionSource2 = remember { MutableInteractionSource() }
    val estaPresionado2 by interactionSource2.collectIsPressedAsState()
<<<<<<< HEAD
    
=======

>>>>>>> 1f4d551ffe33ba663fcd97cb689aad5777a9f175
    val button1ContainerColor = if (estaPresionado1) Color.Green else Color.LightGray
    val button2ContainerColor = if (estaPresionado2) Color.Green else Color.DarkGray

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
            interactionSource = interactionSource1,
            colors = ButtonDefaults.buttonColors(
                containerColor = button1ContainerColor,
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
            interactionSource = interactionSource2,
            colors = ButtonDefaults.buttonColors(
                containerColor = button2ContainerColor,
                contentColor = Color.White
            )
        ) {
            Text(text = "Aún no he registrado mi rostro")
        }
    }
}
