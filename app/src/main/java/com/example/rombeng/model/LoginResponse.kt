package com.example.rombeng.model

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String?, // Token bisa null jika login gagal
    val user: User? // Data user bisa null jika login gagal
)


