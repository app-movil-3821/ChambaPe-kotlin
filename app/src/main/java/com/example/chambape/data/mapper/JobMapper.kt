package com.example.chambape.data.mapper

import com.example.chambape.data.remote.dto.JobDto
import com.example.chambape.domain.model.Job

fun JobDto.toDomain() = Job(
    id             = id,
    contractorId   = contractorId,
    title          = title,
    description    = description,
    category       = category,
    requiredSkills = requiredSkills,
    paymentAmount  = paymentAmount,
    latitude       = location?.latitude  ?: 0.0,
    longitude      = location?.longitude ?: 0.0,
    address        = location?.address   ?: "",
    district       = location?.district  ?: "",
    scheduledStart = scheduledStart ?: "",
    scheduledEnd   = scheduledEnd   ?: "",
    status         = status
)