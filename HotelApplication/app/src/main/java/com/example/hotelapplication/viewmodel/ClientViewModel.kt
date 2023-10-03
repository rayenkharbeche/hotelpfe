package com.example.hotelapplication.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.Token.AuthAdmin
import com.example.hotelapplication.data.*
import com.example.hotelapplication.model.Admin
import com.example.hotelapplication.model.Client
import com.example.hotelapplication.model.Command
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Response


class ClientViewModel(private val repository: ClientRepository , val application: Application) : ViewModel() {
    val clientResponseListLiveData = MutableLiveData<List<ClientResponse>>()
    val errorMessage = MutableLiveData<String?>()
    private val _client = MutableLiveData<Client?>()
    val client: MutableLiveData<Client?>
        get() = _client

    private val _updateStatus = MutableLiveData<RequestStatus<Client>>()
    val updateStatus: LiveData<RequestStatus<Client>>
        get() = _updateStatus
    private val _admin = MutableLiveData<Admin?>()
    val admin: LiveData<Admin?>
        get() = _admin


    init {

        AuthAdmin.getInstance().admin?.let {
            _admin.postValue(it)
        }
    }

    private val _updatedClient = MutableLiveData<Client?>()
    val updatedClient: LiveData<Client?>
        get() = _updatedClient


    fun updateClient(id: String, updateClientBody: UpdateClientBody) {
        viewModelScope.launch {
            repository.updateClient(id, updateClientBody)
                .onStart { _updateStatus.value = RequestStatus.Waiting }
                .catch { e ->
                    _updateStatus.value = RequestStatus.Error(
                        hashMapOf("error" to (e.message ?: "Unknown error"))
                    )
                }
                .collect { status ->
                    _updateStatus.value = status
                    if (status is RequestStatus.Success) {
                        _updatedClient.value = status.data
                        // Vous n'avez plus besoin de la propriété statique ClientViewModel.client
                        // setClientId(_updatedClient.value) pour mettre à jour la LiveData _clientIdLiveData
                        setClientId(_updatedClient.value)
                        Log.d("ClientViewModel", "Updated client: ${status.data}")
                    }
                }
        }
    }




    val _clientIdLiveData: MutableLiveData<Client?> = MutableLiveData()
    val clientIdLiveData: MutableLiveData<Client?> = _clientIdLiveData

    fun setClientId(Client: Client?) {
        clientIdLiveData.value = Client
    }


    private val _commandHistoryLiveData = MutableLiveData<List<Command>?>()
    val commandHistoryLiveData: LiveData<List<Command>?>
        get() = _commandHistoryLiveData

    fun fetchCommandHistoryByQuery(clientId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCommandHistoryByQuery(clientId)
                if (response.isSuccessful) {
                    val commandList: List<Command>? = response.body()
                    _commandHistoryLiveData.postValue(commandList)
                    Log.d("ClientViewModel", "Command history loaded successfully.")
                } else {
                    val errorString = response.errorBody()?.string()
                    Log.e("ClientViewModel", "Error loading command history: $errorString")
                }
            } catch (t: Throwable) {
                Log.e("ClientViewModel", "Failed to load command history: ${t.message}")
            }
        }
    }


}
