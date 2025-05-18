package com.example.rombeng.service

import com.example.rombeng.MyApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://saysayur.web.id/"

    // 1. Buat logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Bisa diubah ke BASIC atau HEADERS
    }

    // 2. Buat OkHttpClient dan pasang interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // 3. Buat Retrofit dengan client custom
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Pasang client ke sini
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val myApi: MyApi by lazy {
        retrofit.create(MyApi::class.java)
    }
}
