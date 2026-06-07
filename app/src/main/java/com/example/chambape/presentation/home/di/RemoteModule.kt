package com.example.chambape.presentation.home.di

import com.example.chambape.presentation.home.data.remote.JobService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteModule {

    // 1. Colocamos la URL oficial de Railway que te pasó tu compañero
    fun provideBaseUrl(): String {
        return "https://backend-chambaya-production-b2e5.up.railway.app/api/v1/"
    }

    // 2. Creamos el interceptor para inyectar la llave (Token) automáticamente
    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestWithToken = originalRequest.newBuilder()
                    // REEMPLAZAR EL TEXTO DE ABAJO por el Token largo de Swagger (sin borrar las comillas)
                    .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJtYXJpYS5jb250cmF0YW50ZUBnbWFpbC5jb20iLCJ1c2VySWQiOiI2YTI0NGFkMTNmMzdlNTdiYjQzNjFjNzEiLCJyb2xlIjoiQ09OVFJBVEFOVEUiLCJpYXQiOjE3ODA4MjkyNTksImV4cCI6MTc4MDkxNTY1OX0.AvETK-KmtzdxXbCaZI8VBoh3SvJGw4YZ1SwOW1LoIQtF5_mjXHj7hlNFmyiNBrNX")
                    .build()
                chain.proceed(requestWithToken)
            }
            .build()
    }

    // 3. Le pasamos el cliente con el token a Retrofit para abrir el candado en internet
    fun provideRetrofit(url: String = provideBaseUrl()): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttpClient()) // <- Conectamos el motor con el token de seguridad
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 4. Creamos el servicio
    fun provideJobService(retrofit: Retrofit = provideRetrofit()): JobService {
        return retrofit.create(JobService::class.java)
    }
}