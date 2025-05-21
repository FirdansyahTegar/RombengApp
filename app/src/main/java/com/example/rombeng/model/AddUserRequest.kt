package com.example.rombeng.model

data class AddUserRequest(
    val username: String,
    val encrypted_password: String,
    val email: String
)

