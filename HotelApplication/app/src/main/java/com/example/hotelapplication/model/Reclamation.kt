package com.example.hotelapplication.model

import java.util.*

data class Reclamation (

    val id: String = UUID.randomUUID().toString(),
    val titre:String ,
    val text:String ,
    val date:String ,
    val clientId: String,

        )