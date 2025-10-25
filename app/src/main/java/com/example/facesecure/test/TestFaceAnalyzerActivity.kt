package com.example.facesecure.test

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.facesecure.camera.CameraManager
import com.example.facesecure.camera.FaceAnalyzer

class TestFaceAnalyzerActivity : ComponentActivity() {

    private lateinit var cameraManager: CameraManager

    // Launcher para permisos
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                Log.e("FaceAnalyzerTest", "Permiso de cámara denegado")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1️⃣ SetContent con Compose vacío para mantener la Activity viva
        setContent {
            TestScreen()
        }

        // 2️⃣ Inicializar CameraManager y FaceAnalyzer
        cameraManager = CameraManager(this, this)

        // 3️⃣ Solicitar permisos de cámara si es necesario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        // Crear el FaceAnalyzer
        val faceAnalyzer = FaceAnalyzer(this) { embedding ->
            // Este callback recibe el embedding del rostro
            Log.d("FaceAnalyzerTest", "📸 Embedding recibido: ${embedding.joinToString(",")}")
        }

        // Iniciar la cámara con el analyzer
        cameraManager.startCamera(faceAnalyzer)
        Log.d("FaceAnalyzerTest", "✅ Iniciando CameraManager + FaceAnalyzer...")
    }
}

@Composable
fun TestScreen() {
    // Solo un Box vacío que ocupa toda la pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Prueba FaceAnalyzer", modifier = Modifier)
    }
}
