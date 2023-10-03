package com.example.hotelapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotelapplication.data.CommandResponse
import com.example.hotelapplication.data.ItemCategoryResponse
import com.example.hotelapplication.data.ItemResponse
import com.example.hotelapplication.data.ServiceResponse
import com.example.hotelapplication.model.ExtarServices
import com.example.hotelapplication.model.Item

class SharedViewModel : ViewModel() {

    private val _factureList = MutableLiveData<MutableList<CommandResponse>>(mutableListOf())
    val factureList: LiveData<MutableList<CommandResponse>> get() = _factureList
    private val _myServiceList = MutableLiveData<MutableList<ServiceResponse>>(mutableListOf())
    val myServiceList: LiveData<MutableList<ServiceResponse>> get() = _myServiceList



    private val _myItemList: MutableLiveData<MutableList<Item>> = MutableLiveData()
    val myItemList: LiveData<MutableList<Item>> get() = _myItemList

    private val _myCommandeServiceList = MutableLiveData<MutableList<Item>>(mutableListOf())
    val myCommandeServiceList: LiveData<MutableList<Item>> get() = _myCommandeServiceList

    var hasClickedButton = false


    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> get() = _totalPrice

    private val _myitemcList = MutableLiveData<MutableList<ItemCategoryResponse>>(mutableListOf())
    val myitemcList: LiveData<MutableList<ItemCategoryResponse>> get() = _myitemcList

    private val _myitemList = MutableLiveData<MutableList<ItemResponse>>(mutableListOf())
    val myitemList: LiveData<MutableList<ItemResponse>> get() = _myitemList


    fun addService(service: ServiceResponse) {
        val newList = _myServiceList.value?.toMutableList() ?: mutableListOf()
        newList.add(service)
        _myServiceList.postValue(newList)
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        myitemList.value?.forEach {
            totalPrice += it.price
        }
        return totalPrice
    }

    fun deleteItem(service: ItemResponse) {
        val currentList = _myitemList.value ?: mutableListOf()
        currentList.remove(service)
        _myitemList.value = currentList
    }

    fun clearServices() {
        _myitemList.value?.clear()
        _myitemList.postValue(_myitemList.value)
        _myCommandeServiceList.value?.clear()
        _myCommandeServiceList.postValue(_myCommandeServiceList.value)
    }

    fun updateTotalPriceAfterDeletion(deletedServicePrice: Double) {
        val totalPrice = getTotalPrice() - deletedServicePrice
        _totalPrice.value = totalPrice
    }

    fun updateTotalPrice() {
        _myServiceList.postValue(_myServiceList.value)
    }

    fun addFacture(facture: CommandResponse) {
        val newList = factureList.value?.toMutableList()?: mutableListOf()
        newList.add(facture)
        _factureList.postValue(newList)
    }

    fun additemC(Itemc: ItemCategoryResponse) {
        val newList = _myitemcList.value?.toMutableList() ?: mutableListOf()
        newList.add(Itemc)
        _myitemcList.postValue(newList)
    }

    fun addItem(Item: ItemResponse) {
        val newList = _myitemList.value?.toMutableList() ?: mutableListOf()
        newList.add(Item)
        _myitemList.postValue(newList)

        Log.d("SharedViewModel", "addItem() - New item added. List size: ${newList.size}")

    }



}
