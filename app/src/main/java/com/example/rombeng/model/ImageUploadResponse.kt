package com.example.rombeng.model // atau package yang sesuai

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("status")
    val status: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("file_path")
    val filePath: String?,

    @SerializedName("file_name")
    val fileName: String?
)

data class UploadedFileInfo(
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("new_name")
    val newName: String?,
    @SerializedName("path")
    val path: String?
)

data class UploadErrorDetail(
    @SerializedName("file")
    val fileName: String?,
    @SerializedName("message")
    val errorMessage: String?
)