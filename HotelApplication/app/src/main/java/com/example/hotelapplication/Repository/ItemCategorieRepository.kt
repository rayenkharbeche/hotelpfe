package com.example.hotelapplication.Repository

import com.example.hotelapplication.Api.RetrofitItemCategorie
import com.example.hotelapplication.model.ItemCategory
import retrofit2.Response

class ItemCategorieRepository constructor(private val retrofitItemCategorie: RetrofitItemCategorie) {
    suspend fun getItemCategorie(serviceId: String): Response<List<ItemCategory>> {
        return retrofitItemCategorie.getItemCategorie(serviceId)
    }
}