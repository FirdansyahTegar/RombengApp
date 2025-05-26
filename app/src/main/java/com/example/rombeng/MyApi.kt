package com.example.rombeng

import com.example.rombeng.model.AddUserResponse
import com.example.rombeng.model.GoogleRegisterRequest
import com.example.rombeng.model.LoginRequest
import com.example.rombeng.model.LoginResponse
import com.example.rombeng.model.User
import com.google.firebase.appdistribution.gradle.models.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface MyApi {
    @GET("get_users.php")
    suspend fun getUsers(): List<User> //jika JSON lebih dari 1 data

//    suspend fun getDataLogin(): DataLogin //jika JSON hanya berisi 1 data
    @FormUrlEncoded
    @POST("add_users.php")
    fun addUser(
        @Field("username") username: String,
        @Field("encrypted_password") encrypted_password: String,
        @Field("email") email: String
    ): Call<AddUserResponse> //Call

    @POST("add_users.php") // Endpoint yang sama jika add_users.php menangani keduanya
    fun registerWithGoogle(
        @Body request: GoogleRegisterRequest
    ): Call<AddUserResponse> // Bisa menggunakan AddUserResponse yang sama jika strukturnya cocok

    @POST("login.php")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @Multipart // Menandakan bahwa request ini adalah multipart (untuk upload file)
    @POST("upload_api.php") // Sesuaikan dengan path ke file PHP Anda
    suspend fun uploadImage(
        @Part image: MultipartBody.Part, // Bagian file gambar
        @Part("description") description: RequestBody? = null // Contoh data tambahan (opsional)
    ): Response<UploadResponse> // Menggunakan Response<T> untuk mendapatkan detail HTTP
}



