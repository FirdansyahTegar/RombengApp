package com.example.rombeng.model

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String
)

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val private_key: String? = "",
    val created_at: String? = ""
//    val address: Address
)