package com.example.hotelapplication.data

import com.example.hotelapplication.model.Client

data class UniqueEmailValidationResponse (val isUnique: Boolean, val client: Client) {
}