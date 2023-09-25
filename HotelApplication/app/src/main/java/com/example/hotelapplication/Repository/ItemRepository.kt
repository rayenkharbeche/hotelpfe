package com.example.hotelapplication.Repository

import com.example.hotelapplication.Api.RetrofitItem
import com.example.hotelapplication.model.Item
import retrofit2.Response

class ItemRepository constructor(private val retrofitItem: RetrofitItem) {
    suspend fun getItem(itemCategoryId: String): Response<List<Item>> {
        return retrofitItem.getItem(itemCategoryId)
    }
}