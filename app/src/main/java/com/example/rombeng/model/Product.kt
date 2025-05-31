package com.example.rombeng.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nama_produk")
    val namaProduk: String,

    @SerializedName("harga")
    val harga: String,

    @SerializedName("kategori")
    val kategori: String,

    @SerializedName("deskripsi_produk")
    val deskripsiProduk: String,

    @SerializedName("lokasi")
    val lokasi: String,

    @SerializedName("gambar_urls")
    val gambarUrls: List<String>,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("penjual_username") // TAMBAHKAN FIELD BARU
    val penjualUsername: String?, // Bisa null jika ada kasus data user tidak ditemukan (meskipun JOIN seharusnya mencegah ini jika datanya konsisten)

    @SerializedName("tanggal_unggah")
    val tanggalUnggah: String
)

// Wrapper response jika API Anda mengembalikan struktur {status, message, data: [...]}
data class ProductListResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<Product>? // Bisa null jika tidak ada produk atau error
)