package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class HomeFeedViewModel : ViewModel() {

    private val jobRepository  = AppModule.jobRepository
    private val authRepository = AppModule.authRepository
    private val tokenManager   = AppModule.tokenManager

    private val _allJobs      = MutableStateFlow<List<Job>>(emptyList())

    private val _searchQuery  = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    val jobs: StateFlow<List<Job>> = combine(_allJobs, _searchQuery) { jobs, query ->
        if (query.isBlank()) jobs
        else {
            val q = query.trim().lowercase()
            jobs.filter {
                it.title.lowercase().contains(q)    ||
                it.category.lowercase().contains(q) ||
                it.district.lowercase().contains(q)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading    = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _userName     = MutableStateFlow("")
    val userName: StateFlow<String> get() = _userName

    init {
        loadUserName()
        loadJobs()
    }

    fun loadJobs() {
        viewModelScope.launch {
            _isLoading.value    = true
            _errorMessage.value = null
            jobRepository.getPublishedJobs()
                .onSuccess { _allJobs.value = it }
                .onFailure { _errorMessage.value = it.message ?: "Error al cargar trabajos." }
            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun loadUserName() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            authRepository.getUser(userId)
                .onSuccess { _userName.value = it.name.split(" ").firstOrNull() ?: it.name }
        }
    }
}