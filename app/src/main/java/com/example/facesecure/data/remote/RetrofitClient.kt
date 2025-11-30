
package com.example.facesecure.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Usamos 10.0.2.2 para conectar al localhost de la máquina anfitriona desde el emulador
    private const val BASE_URL = "http://10.0.2.2:8080/api/users/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
