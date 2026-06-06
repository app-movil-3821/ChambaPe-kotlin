package com.example.chambape.data.mapper

import com.example.chambape.data.remote.dto.ShiftDto
import com.example.chambape.domain.model.Shift

fun ShiftDto.toDomain() = Shift(
    id           = id,
    jobId        = jobId,
    workerId     = workerId,
    contractorId = contractorId,
    status       = status,
    appliedAt    = appliedAt
)