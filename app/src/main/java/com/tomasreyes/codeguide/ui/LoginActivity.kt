package com.tomasreyes.codeguide.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.tomasreyes.codeguide.R
import com.tomasreyes.codeguide.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Inicializamos el Credential Manager de forma segura en Compose
    val credentialManager = remember { CredentialManager.create(context) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                // Ejecutamos el flujo asíncrono dentro del Scope de Compose
                coroutineScope.launch {
                    try {
                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId("587989380494-v89vq9t5vbdmn9vrj18hq75p6nf786j2.apps.googleusercontent.com")

                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        // Pedimos las credenciales al usuario
                        val result = credentialManager.getCredential(context, request)
                        val credential = result.credential

                        if (credential is CustomCredential &&
                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                            val realIdToken = googleIdTokenCredential.idToken // Token real de Google

                            // ➔ Enviamos el token real a tu AuthViewModel
                            authViewModel.signInWithGoogleToken(realIdToken) {
                                Toast.makeText(context, "¡Éxito al iniciar sesión!", Toast.LENGTH_SHORT).show()
                                onNavigateToHome()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("GoogleAuth", "Error al iniciar sesión: ${e.message}")
                        Toast.makeText(context, "Error o inicio cancelado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            Text("Iniciar sesión con Google")
        }
    }
}


