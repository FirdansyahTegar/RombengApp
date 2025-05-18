package com.example.rombeng.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rombeng.MyApi
//import com.example.rombeng.data.RombengRepository
import com.example.rombeng.model.DataLogin
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.rombeng.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.rombeng.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.InetSocketAddress
import java.net.Socket


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RombengViewModel : ViewModel() {

    private val _selectedUser   = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun fetchUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.myApi.getUsers()
                Log.d("RombengViewModel", "Fetched: $response")
                _users.value = response
            } catch (e: Exception) {
                Log.e("RombengViewModel", "Error fetching users", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = user
    }

    var email by mutableStateOf("")
        private set
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    var password by mutableStateOf("")
        private set
    fun onPassChange(newPass: String) {
        password = newPass
    }

    var confPass by mutableStateOf("")
        private set
    fun onConfPassChange(newConfPass: String) {
        confPass = newConfPass
    }

    var name by mutableStateOf("")
        private set
    fun onNameChange(newName: String) {
        name = newName
    }

    var search by mutableStateOf("")
        private set
    fun onSearchChange(newSearch: String) {
        search = newSearch
    }

    fun resetTextField() {
        onNameChange("")
        onPassChange("")
        onConfPassChange("")
        onEmailChange("")
    }

//    suspend fun hasInternetConnection(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork ?: return false
//        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//
//        val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
//
//        if (!isConnected) return false
//
//        // Uji koneksi ke server (misal Google)
//        return withContext(Dispatchers.IO) {
//            try {
//                val socket = Socket()
//                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
//                socket.close()
//                true
//            } catch (e: Exception) {
//                false
//            }
//        }
//    }
//

}

