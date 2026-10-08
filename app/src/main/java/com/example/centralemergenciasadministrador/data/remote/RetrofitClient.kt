package com.example.centralemergenciasadministrador.data.remote

import com.example.centralemergenciasadministrador.data.remote.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IP de la PC donde está ejecutándose el backend Node.js
    private const val BASE_URL = "http://10.185.212.185:3000/api/"

    private var token: String? = null

    fun actualizarToken(nuevoToken: String?) {
        token = nuevoToken
    }

    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder = chain.request()
                .newBuilder()

            token?.let {
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer $it"
                )
            }

            chain.proceed(requestBuilder.build())
        }
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}