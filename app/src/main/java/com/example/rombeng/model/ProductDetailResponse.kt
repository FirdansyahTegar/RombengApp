package com.example.rombeng.model // atau package yang sesuai

 import com.google.gson.annotations.SerializedName // Sudah diimpor di atas jika dalam file yang sama

data class ProductDetailResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String?, // Pesan bisa jadi null

    @SerializedName("data")
    val data: Product? // Ini adalah SATU objek Product, bisa null jika tidak ditemukan
)