package com.example.hotelapplication.data

data class ApiResponse<T>(
    var data: T?,
    var statusCode: Int,
    var success: Boolean,
    var error: Boolean,
)
