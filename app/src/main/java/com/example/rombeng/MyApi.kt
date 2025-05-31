package com.example.rombeng

import com.example.rombeng.model.AddUserResponse
import com.example.rombeng.model.GoogleRegisterRequest
import com.example.rombeng.model.ImageUploadResponse
import com.example.rombeng.model.LoginRequest
import com.example.rombeng.model.LoginResponse
import com.example.rombeng.model.ProductListResponse
import com.example.rombeng.model.User
import com.example.rombeng.model.UploadResponse
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

    @Multipart
    @POST("upload_api.php") // Ganti dengan endpoint Anda
    suspend fun uploadMultipleImagesAndData( // Nama fungsi yang lebih deskriptif
        @Header("Authorization") authToken: String,
        @Part images: List<MultipartBody.Part>,
        @Part("judul_barang") judul: RequestBody, // Nama field harus sesuai dengan yang diharapkan backend
        @Part("harga_barang") harga: RequestBody,
        @Part("kategori_barang") kategori: RequestBody,
        @Part("deskripsi_barang") deskripsi: RequestBody,
        @Part("lokasi_barang") lokasi: RequestBody
    ): Response<ImageUploadResponse> // Pastikan ImageUploadResponse sesuai

    @GET("get_products.php") // Endpoint yang baru Anda buat
    suspend fun getProducts(): Response<ProductListResponse>

    @GET("search_products.php")
    suspend fun searchProducts(
        @Query("query") searchQuery: String
    ): Response<ProductListResponse>



    //END OF MYAPI
}



