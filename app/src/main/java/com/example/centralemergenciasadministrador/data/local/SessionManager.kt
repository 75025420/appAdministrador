package com.example.centralemergenciasadministrador.data.local

import android.content.Context

class SessionManager(context: Context) {

    private val preferences =
        context.getSharedPreferences(
            "sesion_administrador",
            Context.MODE_PRIVATE
        )

    fun guardarToken(token: String) {
        preferences.edit()
            .putString("token", token)
            .apply()
    }

    fun obtenerToken(): String? {
        return preferences.getString("token", null)
    }

    fun eliminarToken() {
        preferences.edit()
            .remove("token")
            .apply()
    }

    fun haySesion(): Boolean {
        return !obtenerToken().isNullOrBlank()
    }
}