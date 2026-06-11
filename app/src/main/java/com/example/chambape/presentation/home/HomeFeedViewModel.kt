package com.example.chambape.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _selectedCategory = MutableStateFlow("TODOS")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _userRole = MutableStateFlow("")
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    // ─── LÓGICA DE FILTRADO COMBINADA Y REACTIVA ───
    val jobs: StateFlow<List<Job>> = combine(_allJobs, _searchQuery, _selectedCategory) { rawJobs, query, category ->
        rawJobs.filter { job ->
            // 1. Filtro por barra de búsqueda (Título, Categoría o Distrito)
            val matchesSearch = if (query.isBlank()) true else {
                val q = query.trim().lowercase()
                job.title.lowercase().contains(q)    ||
                        job.category.lowercase().contains(q) ||
                        job.district.lowercase().contains(q)
            }

            // 2. Filtro por Chips horizontales de Figma
            val matchesCategory = when (category) {
                "URGENTE" -> job.status.uppercase() == "URGENT" || job.status.uppercase() == "IN_PROGRESS"
                "HOY"     -> job.status.uppercase() == "PUBLISHED"
                else      -> true // "TODOS" o "CERCA" despliegan la lista completa por defecto
            }

            // El trabajo debe cumplir ambas condiciones para mostrarse
            matchesSearch && matchesCategory
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
        loadUserRole()
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

    // Nueva función para actualizar la categoría desde el click de la UI
    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }

    private fun loadUserName() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            authRepository.getUser(userId)
                .onSuccess { _userName.value = it.name.split(" ").firstOrNull() ?: it.name }
        }
    }
    private fun loadUserRole() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            authRepository.getUser(userId)
                .onSuccess {
                    // Guardamos el rol en mayúsculas para evitar problemas de formato
                    _userRole.value = it.role.uppercase()
                }
        }
    }

}