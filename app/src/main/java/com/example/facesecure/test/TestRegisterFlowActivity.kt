package com.example.facesecure.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.facesecure.AppNavigation
import com.example.facesecure.ui.theme.FaceSecureTheme

/**
 * Esta Activity reutiliza la navegación real (AppNavigation)
 * y permite probar manualmente todo el flujo de registro facial.
 */
class TestRegisterFlowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaceSecureTheme {
                /* 🚀 Usa el mismo flujo real de la app */
                AppNavigation()
            }
        }
    }
}

