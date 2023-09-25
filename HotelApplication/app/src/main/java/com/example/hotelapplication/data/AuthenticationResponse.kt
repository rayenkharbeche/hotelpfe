package com.example.hotelapplication.data

import com.example.hotelapplication.model.Client

data class AuthenticationResponse(val token: String) {
    val client: Client?
        get() {
            TODO()
        }
}