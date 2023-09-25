package com.example.hotelapplication.Adapter

import com.example.hotelapplication.data.ServiceResponse


interface OnServiceClickListener {
    fun onServiceClicked(service: ServiceResponse)
}