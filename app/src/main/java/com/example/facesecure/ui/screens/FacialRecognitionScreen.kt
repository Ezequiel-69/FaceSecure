package com.example.facesecure.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.facesecure.camera.CameraManager
import com.example.facesecure.camera.FaceAnalyzer
import com.example.facesecure.viewmodel.RegisterViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun FacialRecognitionScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Forma compatible con versiones antiguas y nuevas
    val navBackStackEntry = remember(navController) {
        navController.getBackStackEntry("register")
    }

    val registerViewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        viewModelStoreOwner = navBackStackEntry
    )

    val cameraManager = remember { CameraManager(context, lifecycleOwner) }
    var cameraStarted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📸 Reconocimiento facial")
        Spacer(Modifier.height(10.dp))
        Text("Apunta tu rostro hacia la cámara para continuar")

        Spacer(Modifier.height(20.dp))

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                val analyzer = FaceAnalyzer(ctx) { embedding ->
                    Log.d("FACIAL_SCREEN", "Rostro detectado, enviando embedding al ViewModel")
                    registerViewModel.onFaceCaptured(embedding.toList())
                    Log.d("FACIAL_SCREEN", "Embedding enviado. Navegando a HomeScreen...")
                    navController.navigate("home")
                }

                cameraManager.startCamera(analyzer)
                cameraStarted = true

                previewView
            }
        )

        Spacer(Modifier.height(20.dp))

        if (cameraStarted) {
            Button(onClick = {
                cameraManager.stopCamera()
                navController.popBackStack()
            }) {
                Text("Cancelar")
            }
        } else {
            Text("Iniciando cámara...")
        }
    }
}
