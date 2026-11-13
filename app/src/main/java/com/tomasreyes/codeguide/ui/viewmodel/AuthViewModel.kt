package com.tomasreyes.codeguide.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userEmail: String) : AuthState()
    data class Error(val message: String) : AuthState()
    data class ResetSent(val email: String) : AuthState()
}



class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val firebaseAuth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    // Login Real con Email y Contraseña mediante Firebase Auth
    fun loginUser(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Por favor ingresa tu correo y contraseña maestra.")
            return
        }

        _authState.value = AuthState.Loading

        val auth = firebaseAuth ?: try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
        if (auth != null) {
            auth.signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    _authState.value = AuthState.Success(user?.email ?: email)
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    _authState.value = AuthState.Error(exception.localizedMessage ?: "Error de autenticación con Firebase. Verifique sus credenciales.")
                }
        } else {
            _authState.value = AuthState.Error("Servicio de Firebase Auth no disponible en este dispositivo.")
        }
    }

    // Validación Real de ID Token de Google con Firebase Auth
    fun signInWithGoogleToken(idToken: String, onSuccess: () -> Unit) {
        if (idToken.isBlank()) {
            _authState.value = AuthState.Error("Token de Google inválido.")
            return
        }

        _authState.value = AuthState.Loading

        val auth = firebaseAuth ?: try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
        if (auth != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    val user = result.user
                    _authState.value = AuthState.Success(user?.email ?: "usuario.google@codeguide.io")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    _authState.value = AuthState.Error(exception.localizedMessage ?: "Fallo de validación de Google ID Token en Firebase Auth.")
                }
        } else {
            _authState.value = AuthState.Error("No se pudo conectar con Firebase Auth para validar el token de Google.")
        }
    }

    // Registro Real con Email y Contraseña mediante Firebase Auth
    fun registerUser(name: String, email: String, password: String, confirmPass: String, onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Por favor completa todos los campos del formulario.")
            return
        }

        if (password != confirmPass) {
            _authState.value = AuthState.Error("Las contraseñas no coinciden.")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        _authState.value = AuthState.Loading

        val auth = firebaseAuth ?: try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
        if (auth != null) {
            auth.createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    _authState.value = AuthState.Success(user?.email ?: email)
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    _authState.value = AuthState.Error(exception.localizedMessage ?: "Error al registrar usuario en Firebase Auth.")
                }
        } else {
            _authState.value = AuthState.Error("Servicio de Firebase Auth no disponible.")
        }
    }

    // Restablecer Contraseña Real mediante Firebase Auth
    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Ingresa tu correo dev registrado.")
            return
        }

        _authState.value = AuthState.Loading

        val auth = firebaseAuth ?: try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
        if (auth != null) {
            auth.sendPasswordResetEmail(email.trim())
                .addOnSuccessListener {
                    _authState.value = AuthState.ResetSent(email)
                }
                .addOnFailureListener { exception ->
                    _authState.value = AuthState.Error(exception.localizedMessage ?: "No se pudo enviar el correo de recuperación.")
                }
        } else {
            _authState.value = AuthState.Error("Servicio de Firebase no disponible.")
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
