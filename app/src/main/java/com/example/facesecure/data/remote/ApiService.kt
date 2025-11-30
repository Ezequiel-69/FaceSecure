package com.example.facesecure.data.remote

import com.example.facesecure.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    suspend fun registerUser(@Body user: User): Response<User>

    @GET("email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<User>
}
