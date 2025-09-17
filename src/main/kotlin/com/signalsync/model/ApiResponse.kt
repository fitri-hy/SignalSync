package com.signalsync.model

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: String? = null
)
