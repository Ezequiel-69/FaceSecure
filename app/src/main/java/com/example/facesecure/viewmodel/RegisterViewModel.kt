package com.example.facesecure.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facesecure.data.local.AppDatabase
import com.example.facesecure.data.model.User
import com.example.facesecure.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "REGISTER_FLOW"
    }

    // --- Base de datos y repositorio ---
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val repository = UserRepository(userDao)

    // --- Estado del formulario ---
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _faceEmbedding = MutableStateFlow<List<Float>?>(null)
    val faceEmbedding: StateFlow<List<Float>?> = _faceEmbedding

    // --- Estado del resultado ---
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    // --- Funciones para actualizar los valores del usuario ---
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail.trim()
        Log.d(TAG, "onEmailChange(): ${_email.value}")
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword.trim()
        Log.d(TAG, "onPasswordChange(): (password updated)")
    }

    fun onFaceCaptured(embedding: List<Float>) {
        _faceEmbedding.value = embedding
        Log.d(TAG, "onFaceCaptured(): embeddingSize=${embedding.size}")
        registerUser()
    }

    // --- Guardar el usuario completo ---
    fun registerUser() {
        val emailValue = _email.value
        val passwordValue = _password.value
        val embeddingValue = _faceEmbedding.value

        if (emailValue.isBlank() || passwordValue.isBlank() || embeddingValue == null) {
            _statusMessage.value = "Faltan datos del usuario"
            Log.e(TAG, "registerUser(): datos incompletos — email=$emailValue, pass=${passwordValue.isNotEmpty()}, embedding=${embeddingValue != null}")
            return
        }

        viewModelScope.launch {
            try {
                _isSaving.value = true
                Log.d(TAG, "registerUser(): guardando usuario en Room...")

                val newUser = User(
                    email = emailValue,
                    password = passwordValue,
                    faceEmbedding = embeddingValue
                )

                repository.insertUser(newUser)

                // 🔎 Confirmación visual
                Log.d(TAG, "registerUser(): Usuario guardado en BD: $emailValue (${embeddingValue.size} floats)")
                _statusMessage.value = "Usuario registrado correctamente ✅"

                // Debug extra: mostrar todos los usuarios actuales
                val users = repository.getAllUsers()
                Log.d(TAG, "Usuarios actuales en BD: ${users.map { it.email }}")

            } catch (e: Exception) {
                Log.e(TAG, "Error al registrar: ${e.message}", e)
                _statusMessage.value = "Error al registrar: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearStatus() {
        _statusMessage.value = null
    }
}
