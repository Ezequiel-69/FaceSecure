package com.example.facesecure.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.facesecure.R

@Composable
fun FacialRecognitionScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Prepara tu cámara para el reconocimiento facial")
        Button(onClick = { 
            val mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
            mediaPlayer.setOnCompletionListener { mp ->
                navController.navigate("home")
                mp.release()
            }
            mediaPlayer.start()
        }) {
            Text(text = "Iniciar reconocimiento facial")
        }
    }
}
