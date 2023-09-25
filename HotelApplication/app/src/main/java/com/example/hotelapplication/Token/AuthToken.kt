package com.example.hotelapplication.Token

import android.content.Context
import android.content.SharedPreferences


class AuthToken private constructor(context: Context) {

    companion object {
        private const val TOKEN = "TOKEN"
        private const val TOKEN_VALUE = "TOKEN_VALUE"
        private const val NAME = "NAME"
        private const val EMAIL = "EMAIL"
        private const val EMAILTOKEN = "EMAILTOKEN"

        @Volatile
        private var instance: AuthToken? = null

         fun getInstance(context: Context): AuthToken = instance ?: synchronized(this)
        { AuthToken(context).apply { instance = this } } }

        private val sharedPreference: SharedPreferences =context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        var token: String? = null
            set(value) = sharedPreference.edit().putString(TOKEN_VALUE, value).apply()
                .also { field = value }
            get() = field ?: sharedPreference.getString(TOKEN_VALUE, null)



    var name: String?
        set(value) = sharedPreference.edit().putString(NAME, value).apply()
        get() = sharedPreference.getString(NAME, null)

    var email: String?
        set(value) = sharedPreference.edit().putString(EMAIL, value).apply()
        get() = sharedPreference.getString(EMAIL, null)

     var emailtoken: String?
    set(value) = sharedPreference.edit().putString(EMAILTOKEN, value).apply()
    get() = sharedPreference.getString(EMAILTOKEN, null)
}
