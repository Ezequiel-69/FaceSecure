package com.example.facesecure.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.facesecure.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    // Usamos "register" como el scope del ViewModel para compartirlo con la pantalla de reconocimiento
    val registerViewModel: RegisterViewModel = viewModel(navController.getBackStackEntry("register"))

    val nombre by registerViewModel.nombre.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val isSaving by registerViewModel.isSaving.collectAsState()
    val statusMessage by registerViewModel.statusMessage.collectAsState()
    val navigate by registerViewModel.navigateToFacialRecognition.collectAsState()

    // Efecto para manejar la navegación
    LaunchedEffect(navigate) {
        if (navigate) {
            navController.navigate("facial_recognition")
            registerViewModel.onNavigationComplete() // Resetea el estado de navegación
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Crea tu cuenta")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { registerViewModel.onNombreChange(it) },
            label = { Text("Nombre") },
            enabled = !isSaving
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { registerViewModel.onEmailChange(it) },
            label = { Text("Usuario / Email") },
            enabled = !isSaving
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { registerViewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isSaving
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isSaving) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { registerViewModel.registerUserAndProceed() },
                enabled = !isSaving
            ) {
                Text("Registrar y continuar")
            }
        }

        statusMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it)
        }
    }
}
