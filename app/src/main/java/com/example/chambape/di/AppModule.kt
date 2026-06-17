package com.example.chambape.di

import android.content.Context
import com.example.chambape.domain.repository.NotificationRepository
import com.example.chambape.domain.repository.AuthRepository
import com.example.chambape.domain.repository.JobRepository
import com.example.chambape.domain.repository.MessageRepository
import com.example.chambape.domain.repository.ReviewRepository
import com.example.chambape.domain.repository.ShiftRepository
import com.example.chambape.data.repository.SettingsPreferences
import com.example.chambape.data.repository.TokenManager
import com.example.chambape.data.location.GeocodingService

/**
 * Objeto singleton que inicializa y provee todas las dependencias.
 * Llamar AppModule.init(context) desde MyApplication.onCreate()
 */
object AppModule {

    private lateinit var _authRepository: AuthRepository
    private lateinit var _jobRepository: JobRepository
    private lateinit var _shiftRepository: ShiftRepository
    private lateinit var _messageRepository: MessageRepository
    private lateinit var _notificationRepository: NotificationRepository
    private lateinit var _reviewRepository: ReviewRepository
    private lateinit var _tokenManager: TokenManager
    private lateinit var _settingsPreferences: SettingsPreferences
    private lateinit var _geocodingService: GeocodingService

    val authRepository: AuthRepository                 get() = _authRepository
    val jobRepository: JobRepository                   get() = _jobRepository
    val shiftRepository: ShiftRepository               get() = _shiftRepository
    val messageRepository: MessageRepository           get() = _messageRepository
    val notificationRepository: NotificationRepository get() = _notificationRepository
    val reviewRepository: ReviewRepository             get() = _reviewRepository
    val tokenManager: TokenManager                     get() = _tokenManager
    val settingsPreferences: SettingsPreferences       get() = _settingsPreferences
    val geocodingService: GeocodingService             get() = _geocodingService

    fun init(context: Context) {
        val tm                   = TokenManager(context)
        _tokenManager            = tm
        _settingsPreferences     = SettingsPreferences(context)
        _geocodingService        = GeocodingService(context)
        val okHttpClient         = RemoteModule.provideOkHttpClient(tm)
        val retrofit             = RemoteModule.provideRetrofit(okHttpClient)

        val authService          = RemoteModule.provideAuthService(retrofit)
        val jobService           = RemoteModule.provideJobService(retrofit)
        val shiftService         = RemoteModule.provideShiftService(retrofit)
        val messageService       = RemoteModule.provideMessageService(retrofit)
        val notificationService  = RemoteModule.provideNotificationService(retrofit)
        val reviewService        = RemoteModule.provideReviewService(retrofit)

        _authRepository          = RepositoryModule.provideAuthRepository(authService, tm)
        _jobRepository           = RepositoryModule.provideJobRepository(jobService)
        _shiftRepository         = RepositoryModule.provideShiftRepository(shiftService)
        _messageRepository       = RepositoryModule.provideMessageRepository(messageService)
        _notificationRepository  = RepositoryModule.provideNotificationRepository(notificationService)
        _reviewRepository        = RepositoryModule.provideReviewRepository(reviewService)
    }
}