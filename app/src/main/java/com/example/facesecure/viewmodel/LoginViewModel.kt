package com.example.facesecure.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facesecure.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LOGIN_FLOW"
    }

    private val apiService = RetrofitClient.instance

    // --- Estado del formulario ---
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    // --- Estado del proceso ---
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // --- Estado para la navegación ---
    private val _navigateToLoginSuccess = MutableStateFlow(false)
    val navigateToLoginSuccess: StateFlow<Boolean> = _navigateToLoginSuccess

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail.trim()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword.trim()
    }

    fun login() {
        val emailValue = _email.value
        val passwordValue = _password.value

        if (emailValue.isBlank() || passwordValue.isBlank()) {
            _statusMessage.value = "Por favor, ingresa email y contraseña"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _statusMessage.value = "Iniciando sesión..."
            try {
                val response = apiService.getUserByEmail(emailValue)

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null && user.password == passwordValue) {
                        // ¡Éxito!
                        Log.d(TAG, "Login exitoso para el usuario: $emailValue")
                        _statusMessage.value = "¡Bienvenido!"
                        _navigateToLoginSuccess.value = true
                    } else {
                        // Usuario encontrado, pero contraseña incorrecta
                        _statusMessage.value = "La contraseña es incorrecta"
                        Log.w(TAG, "Contraseña incorrecta para el usuario: $emailValue")
                    }
                } else {
                    // El usuario no fue encontrado (ej. error 404)
                    _statusMessage.value = "El usuario no existe o los datos son incorrectos"
                    Log.w(TAG, "Usuario no encontrado con email: $emailValue")
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error de conexión. Inténtalo de nuevo."
                Log.e(TAG, "Error de red durante el login", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onNavigationComplete() {
        _navigateToLoginSuccess.value = false
    }
}
