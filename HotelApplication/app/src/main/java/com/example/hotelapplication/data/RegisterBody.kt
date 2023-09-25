package com.example.hotelapplication.data

import java.util.*

data class RegisterBody(
    val name: String,
    val email: String,
    val password : String,
    val phone: String,
    val credit: Float,
    val photo : String,
    )
