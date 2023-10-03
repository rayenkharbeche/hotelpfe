package com.example.hotelapplication.Token

import com.example.hotelapplication.model.Client

object AuthClient {
    var client: Client? = null

    private var instance: AuthClient? = null

    fun getInstance(): AuthClient {
        if (instance == null) {
            instance = AuthClient
        }
        return instance!!
    }

    fun logout() {

        client = null

    }

    fun setConnectedClient(client: Client) {
        this.client = client
    }

    fun getConnectedClient(): Client? {
        return client
    }
}