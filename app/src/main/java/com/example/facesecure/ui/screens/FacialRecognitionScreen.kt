package com.example.facesecure.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

    val navBackStackEntry = remember(navController) {
        navController.getBackStackEntry("register")
    }

    val registerViewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        viewModelStoreOwner = navBackStackEntry
    )

    val cameraManager = remember { CameraManager(context, lifecycleOwner) }
    var cameraStarted by remember { mutableStateOf(false) }

    val statusMessage by registerViewModel.statusMessage.collectAsState()
    val isSaving by registerViewModel.isSaving.collectAsState()

    LaunchedEffect(statusMessage) {
        if (statusMessage?.contains("correctamente") == true) {
            Log.d("FACIAL_SCREEN", "Registro exitoso, navegando a home.")
            registerViewModel.clearStatus()
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSaving) {
            Text("Registrando usuario...")
            Spacer(Modifier.height(10.dp))
            CircularProgressIndicator()
        } else {
            Text("📸 Reconocimiento facial")
            Spacer(Modifier.height(10.dp))
            Text("Apunta tu rostro hacia la cámara para continuar")

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(400.dp)
            ) {
                AndroidView(
                    factory = { viewContext ->
                        val previewView = PreviewView(viewContext)
                        if (!isSaving) {
                            val analyzer = FaceAnalyzer(context) { embedding ->
                                Log.d("FACIAL_SCREEN", "Rostro detectado, enviando embedding al ViewModel")
                                registerViewModel.onFaceCaptured(embedding.toList())
                            }
                            cameraManager.startCamera(previewView.surfaceProvider, analyzer)
                            cameraStarted = true
                        }
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(20.dp))

            statusMessage?.let {
                if (!it.contains("correctamente")) {
                    Text(it)
                    Spacer(Modifier.height(10.dp))
                }
            }

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
}
