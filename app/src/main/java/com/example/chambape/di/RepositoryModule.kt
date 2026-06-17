package com.example.chambape.di

import com.example.chambape.data.remote.service.AuthService
import com.example.chambape.data.remote.service.JobService
import com.example.chambape.data.remote.service.MessageService
import com.example.chambape.data.remote.service.NotificationService
import com.example.chambape.data.remote.service.ShiftService
import com.example.chambape.data.repository.AuthRepositoryImpl
import com.example.chambape.data.repository.JobRepositoryImpl
import com.example.chambape.data.repository.MessageRepositoryImpl
import com.example.chambape.data.repository.NotificationRepositoryImpl
import com.example.chambape.data.repository.ShiftRepositoryImpl
import com.example.chambape.data.repository.TokenManager
import com.example.chambape.domain.repository.AuthRepository
import com.example.chambape.domain.repository.JobRepository
import com.example.chambape.domain.repository.MessageRepository
import com.example.chambape.data.repository.ReviewRepositoryImpl
import com.example.chambape.data.remote.service.ReviewService
import com.example.chambape.domain.repository.ReviewRepository
import com.example.chambape.domain.repository.ShiftRepository
import com.example.chambape.domain.repository.NotificationRepository

object RepositoryModule {

    fun provideAuthRepository(
        authService: AuthService,
        tokenManager: TokenManager
    ): AuthRepository = AuthRepositoryImpl(authService, tokenManager)

    fun provideJobRepository(jobService: JobService): JobRepository =
        JobRepositoryImpl(jobService)

    fun provideShiftRepository(shiftService: ShiftService): ShiftRepository =
        ShiftRepositoryImpl(shiftService)

    fun provideMessageRepository(messageService: MessageService): MessageRepository =
        MessageRepositoryImpl(messageService)

    fun provideNotificationRepository(notificationService: NotificationService): NotificationRepository =
        NotificationRepositoryImpl(notificationService)

    fun provideReviewRepository(reviewService: ReviewService): ReviewRepository =
        ReviewRepositoryImpl(reviewService)
}