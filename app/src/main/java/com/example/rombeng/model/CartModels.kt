package com.example.rombeng.model

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("cart_item_id")
    val cartItemId: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("nama_produk")
    val productName: String,
    @SerializedName("harga")
    val harga: String, // Atau Double/BigDecimal jika Anda menangani sebagai angka
    @SerializedName("gambar_url")
    val imageUrl: String?
)

// Model untuk respons umum dari API (misalnya untuk add, update, remove)
data class CartActionResponse(
    val status: String,
    val message: String,
    @SerializedName("new_quantity")
    val newQuantity: Int? = null, // Untuk respons update quantity
    @SerializedName("cart_item_id")
    val cartItemId: Int? = null // Untuk respons add to cart
)

// Model untuk respons getCartItems
data class CartListResponse(
    val status: String,
    val message: String? = null, // Pesan error jika status bukan success
    val data: List<CartItem>? = null
)