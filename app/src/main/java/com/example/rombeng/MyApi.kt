package com.example.rombeng

import com.example.rombeng.model.User
import retrofit2.http.GET

interface MyApi {
    @GET("get_users.php")
    suspend fun getUsers(): List<User> //jika JSON lebih dari 1 data

//    suspend fun getDataLogin(): DataLogin //jika JSON hanya berisi 1 data

}