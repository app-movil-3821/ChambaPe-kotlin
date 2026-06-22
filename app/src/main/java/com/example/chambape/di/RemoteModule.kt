package com.example.chambape.di

import com.example.chambape.data.remote.service.AuthService
import com.example.chambape.data.remote.service.JobService
import com.example.chambape.data.remote.service.MessageService
import com.example.chambape.data.remote.service.NotificationService
import com.example.chambape.data.remote.service.ReviewService
import com.example.chambape.data.remote.service.ShiftService
import com.example.chambape.data.repository.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteModule {

    private const val BASE_URL = "https://backend-chambaya-production-a24a.up.railway.app/api/v1/"

    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val token = tokenManager.getToken()
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
            .build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // ← Gson en vez de Kotlinx
            .build()

    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    fun provideJobService(retrofit: Retrofit): JobService =
        retrofit.create(JobService::class.java)

    fun provideShiftService(retrofit: Retrofit): ShiftService =
        retrofit.create(ShiftService::class.java)

    fun provideMessageService(retrofit: Retrofit): MessageService =
        retrofit.create(MessageService::class.java)

    fun provideNotificationService(retrofit: Retrofit): NotificationService =
        retrofit.create(NotificationService::class.java)

    fun provideReviewService(retrofit: Retrofit): ReviewService =
        retrofit.create(ReviewService::class.java)
}