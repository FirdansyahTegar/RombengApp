package com.example.rombeng.repository // Sesuaikan dengan package Anda

import com.example.rombeng.model.LoginRequest
import com.example.rombeng.model.LoginResponse
import com.example.rombeng.MyApi
import com.example.rombeng.service.RetrofitClient
import retrofit2.Response

class UserRepository(private val myApi: MyApi) {

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return myApi.loginUser(loginRequest)
    }

    // Tambahkan fungsi lain untuk interaksi dengan API terkait user
}