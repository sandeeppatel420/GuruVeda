package com.example.guruveda.Notification

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServerKey(private val context: Context) {
    private val scope = "https://www.googleapis.com/auth/firebase.messaging"
    private val scopes = setOf(scope)

    fun getAccessToken(callback: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val assetManager = context.assets.open("severKey.json")
                val googleCredentials = GoogleCredentials
                    .fromStream(assetManager)
                    .createScoped(scopes)
                googleCredentials.refreshIfExpired()
                val serverKey = googleCredentials.accessToken.tokenValue

                withContext(Dispatchers.Main) {
                    callback(serverKey)
                }
            } catch (e: Exception) {
                Log.e("ServerKey", "Error fetching access token: ${e.message}")
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }
}