package com.example.hotelapplication.data

class SignUpResponse (     val data: Boolean?,
                          val statusCode: Int,
                          val success: Boolean,
                          val error: Boolean,
                          val email: String,
                           val emailToken: String) {
}