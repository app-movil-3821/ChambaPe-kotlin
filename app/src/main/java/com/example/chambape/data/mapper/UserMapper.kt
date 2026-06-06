package com.example.chambape.data.mapper

import com.example.chambape.data.remote.dto.UserDto
import com.example.chambape.domain.model.User

fun UserDto.toDomain() = User(
    id         = id,
    name       = name,
    email      = email,
    role       = role,
    phone      = profile?.phone ?: "",
    skills     = profile?.skills ?: emptyList(),
    experience = profile?.experience ?: "",
    district   = profile?.district ?: "",
    photoUrl   = profile?.photoUrl,
    verified   = profile?.verified ?: false
)