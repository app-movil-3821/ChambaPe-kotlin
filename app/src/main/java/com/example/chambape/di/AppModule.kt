package com.example.chambape.di

import android.content.Context
import com.example.chambape.domain.repository.AuthRepository
import com.example.chambape.domain.repository.JobRepository
import com.example.chambape.domain.repository.ShiftRepository
import com.example.chambape.data.repository.TokenManager

/**
 * Objeto singleton que inicializa y provee todas las dependencias.
 * Llamar AppModule.init(context) desde MyApplication.onCreate()
 */
object AppModule {

    private lateinit var _authRepository: AuthRepository
    private lateinit var _jobRepository: JobRepository
    private lateinit var _shiftRepository: ShiftRepository

    val authRepository: AuthRepository get() = _authRepository
    val jobRepository: JobRepository   get() = _jobRepository
    val shiftRepository: ShiftRepository get() = _shiftRepository

    fun init(context: Context) {
        val tokenManager   = TokenManager(context)
        val okHttpClient   = RemoteModule.provideOkHttpClient(tokenManager)
        val retrofit       = RemoteModule.provideRetrofit(okHttpClient)

        val authService    = RemoteModule.provideAuthService(retrofit)
        val jobService     = RemoteModule.provideJobService(retrofit)
        val shiftService   = RemoteModule.provideShiftService(retrofit)

        _authRepository  = RepositoryModule.provideAuthRepository(authService, tokenManager)
        _jobRepository   = RepositoryModule.provideJobRepository(jobService)
        _shiftRepository = RepositoryModule.provideShiftRepository(shiftService)
    }
}
