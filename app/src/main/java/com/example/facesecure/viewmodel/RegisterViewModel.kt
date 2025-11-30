package com.example.facesecure.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facesecure.data.model.User
import com.example.facesecure.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "REGISTER_FLOW"
    }

    private val apiService = RetrofitClient.instance

    // --- Estado del formulario ---
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    // --- Estado del proceso ---
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    // Nuevo estado para controlar la navegación
    private val _navigateToFacialRecognition = MutableStateFlow(false)
    val navigateToFacialRecognition: StateFlow<Boolean> = _navigateToFacialRecognition


    fun onNombreChange(newNombre: String) {
        _nombre.value = newNombre
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail.trim()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword.trim()
    }

    /**
     * Paso 1: Registra al usuario en el backend.
     * Si tiene éxito, activa la navegación a la pantalla de reconocimiento facial.
     */
    fun registerUserAndProceed() {
        val nombreValue = _nombre.value
        val emailValue = _email.value
        val passwordValue = _password.value

        if (nombreValue.isBlank() || emailValue.isBlank() || passwordValue.isBlank()) {
            _statusMessage.value = "Debes completar todos los campos"
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            _statusMessage.value = "Registrando usuario..."
            try {
                val newUser = User(nombre = nombreValue, email = emailValue, password = passwordValue)
                val response = apiService.registerUser(newUser)

                if (response.isSuccessful) {
                    Log.d(TAG, "Usuario registrado en backend, procediendo a captura facial.")
                    _statusMessage.value = "Usuario creado. Ahora, la foto."
                    _navigateToFacialRecognition.value = true // Dispara la navegación
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    _statusMessage.value = "Error en el registro: $errorBody"
                    Log.e(TAG, "Error del backend: ${response.code()} - $errorBody")
                }

            } catch (e: Exception) {
                _statusMessage.value = "Error de conexión: ${e.message}"
                Log.e(TAG, "Error de red o conexión", e)
            } finally {
                _isSaving.value = false
            }
        }
    }

    /**
     * Paso 2: Se llama después de la captura facial.
     * TODO: Necesita un endpoint en el backend para guardar el embedding.
     */
    fun onFaceCaptured(embedding: List<Float>) {
        Log.d(TAG, "onFaceCaptured(): embeddingSize=${embedding.size}")
        // Aquí iría la lógica para enviar el embedding al backend y asociarlo al usuario recién creado.
        _statusMessage.value = "¡Registro completado!"
        // Por ejemplo:
        // viewModelScope.launch {
        //     val userId = _registeredUser.value?.id
        //     if (userId != null) { 
        //         apiService.updateUserEmbedding(userId, embedding)
        //     }
        // }
    }

    fun onNavigationComplete() {
        _navigateToFacialRecognition.value = false
    }

    fun clearStatus() {
        _statusMessage.value = null
    }
}
