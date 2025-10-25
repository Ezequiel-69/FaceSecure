package com.example.facesecure.test

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.core.content.ContextCompat
import com.example.facesecure.camera.CameraManager

/**
 * TestCameraManagerActivity
 * --------------------------
 * Esta Activity sirve como prueba manual del funcionamiento de CameraManager.
 * No muestra ninguna interfaz visible: solo inicia la cámara, procesa frames y
 * muestra logs en Logcat (DummyAnalyzer).
 *
 * Flujo:
 * 1️⃣ Verifica permiso de cámara.
 * 2️⃣ Inicia CameraManager con un DummyAnalyzer.
 * 3️⃣ DummyAnalyzer recibe frames y los registra en Logcat.
 * 4️⃣ Si algo falla (sin frames, sin permisos, error de hardware), se muestra en Logcat.
 */
class TestCameraManagerActivity : ComponentActivity() {

    private lateinit var cameraManager: CameraManager
    private val tag = "TestCameraManager"

    // Registramos un launcher para pedir permiso de cámara si no lo tiene
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(tag, "✅ Permiso de cámara concedido, iniciando cámara...")
            startCamera()
        } else {
            Log.e(tag, "❌ Permiso de cámara denegado. No se puede probar CameraManager.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "🟢 Iniciando TestCameraManagerActivity...")

        // Paso 1️⃣: Verificar permiso
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(tag, "📸 Permiso de cámara ya concedido.")
            startCamera()
        } else {
            Log.d(tag, "⚠️ Solicitando permiso de cámara...")
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    /**
     * Inicia CameraManager con el DummyAnalyzer
     */
    private fun startCamera() {
        try {
            // Paso 2️⃣: Inicializar CameraManager
            cameraManager = CameraManager(this, this)

            // Paso 3️⃣: Iniciar cámara con analizador de prueba
            cameraManager.startCamera(DummyAnalyzer())

            Log.d(tag, "🚀 Cámara iniciada correctamente.")
        } catch (e: Exception) {
            Log.e(tag, "💥 Error al iniciar CameraManager: ${e.message}", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Paso 4️⃣: Detener cámara al destruir la Activity
        if (::cameraManager.isInitialized) {
            cameraManager.stopCamera()
            Log.d(tag, "🛑 Cámara detenida correctamente.")
        }
    }
}

/**
 * DummyAnalyzer
 * --------------------------
 * Analizador de prueba que solo cuenta cuántos frames recibe.
 * No realiza procesamiento real de imagen.
 */
class DummyAnalyzer : ImageAnalysis.Analyzer {

    private var frameCount = 0
    private val tag = "DummyAnalyzer"

    override fun analyze(imageProxy: androidx.camera.core.ImageProxy) {
        try {
            frameCount++
            Log.d(tag, "📸 Frame #$frameCount recibido.")
        } catch (e: Exception) {
            Log.e(tag, "⚠️ Error procesando frame: ${e.message}", e)
        } finally {
            // Importante: cerrar el frame para permitir que lleguen los siguientes
            imageProxy.close()
        }
    }
}
