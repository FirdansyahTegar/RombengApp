package com.example.rombeng.model // atau package yang sesuai

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("file_path")
    val filePath: String?, // Jadikan nullable jika bisa tidak ada
    @SerializedName("file_name")
    val fileName: String?  // Jadikan nullable jika bisa tidak ada
)