package com.example.rombeng.viewmodel // Sesuaikan package

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rombeng.model.CartItem
import com.example.rombeng.MyApi
import com.example.rombeng.service.RetrofitClient
import kotlinx.coroutines.launch

// State untuk UI Keranjang
sealed class CartUIState {
    object Idle : CartUIState()
    object Loading : CartUIState()
    data class Success(val items: List<CartItem>) : CartUIState()
    data class Error(val message: String) : CartUIState()
    object Empty : CartUIState() // Untuk keranjang kosong setelah berhasil fetch
}

class CartViewModel(private val myApi: MyApi = RetrofitClient.myApi) : ViewModel() {

    var cartUiState: CartUIState by mutableStateOf(CartUIState.Idle)
        private set

    private var _currentUserId: Int? = null // Ubah menjadi private backing property
    val currentUserId: Int?
        get() = _currentUserId


    // Fungsi ini dipanggil dari Composable atau Activity/Fragment yang membuat CartViewModel
    fun initializeUser(userId: Int?) {
        if (userId == null || userId == -1) {
            _currentUserId = null
            cartUiState = CartUIState.Error("User not logged in or User ID is invalid.")
            Log.w("CartViewModel", "Attempted to initialize with invalid userId: $userId")
            return
        }

        if (_currentUserId != userId) { // Hanya re-initialize jika userId berubah atau pertama kali
            _currentUserId = userId
            Log.d("CartViewModel", "User ID set to: $userId. Fetching cart items.")
            fetchCartItems()
        } else if (cartUiState is CartUIState.Idle || cartUiState is CartUIState.Error) {
            // Jika userId sama tapi state adalah Idle/Error, coba fetch lagi
            Log.d("CartViewModel", "User ID is the same, but state is Idle/Error. Re-fetching cart items.")
            fetchCartItems()
        }
    }

    fun fetchCartItems() {
        val userId = currentUserId ?: run {
            cartUiState = CartUIState.Error("User ID not set.")
            return
        }

        cartUiState = CartUIState.Loading
        viewModelScope.launch {
            try {
                val response = myApi.getCartItems(userId)
                if (response.isSuccessful && response.body() != null) {
                    val cartResponse = response.body()!!
                    if (cartResponse.status == "success") {
                        val items = cartResponse.data
                        if (items.isNullOrEmpty()) {
                            cartUiState = CartUIState.Empty
                        } else {
                            cartUiState = CartUIState.Success(items)
                        }
                    } else {
                        cartUiState = CartUIState.Error(cartResponse.message ?: "Failed to load cart items.")
                    }
                } else {
                    cartUiState = CartUIState.Error("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception fetching cart: ${e.localizedMessage}", e)
                cartUiState = CartUIState.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun addProductToCart(productId: Int, quantity: Int = 1, onResult: (Boolean, String) -> Unit) {
        val userId = currentUserId ?: run {
            onResult(false, "User ID not set.")
            return
        }

        viewModelScope.launch {
            try {
                val response = myApi.addToCart(userId, productId, quantity)
                if (response.isSuccessful && response.body() != null) {
                    val actionResponse = response.body()!!
                    if (actionResponse.status == "success") {
                        onResult(true, actionResponse.message)
                        fetchCartItems() // Refresh keranjang setelah menambah
                    } else {
                        onResult(false, actionResponse.message)
                    }
                } else {
                    onResult(false, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception adding to cart: ${e.localizedMessage}", e)
                onResult(false, "Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun updateItemQuantity(productId: Int, newQuantity: Int, onResult: (Boolean, String) -> Unit) {
        val userId = currentUserId ?: run {
            onResult(false, "User ID not set.")
            return
        }

        if (newQuantity <= 0) { // Jika kuantitas 0 atau kurang, anggap sebagai remove
            removeItemFromCart(productId, onResult)
            return
        }

        viewModelScope.launch {
            try {
                val response = myApi.updateCartItemQuantity(userId, productId, newQuantity)
                if (response.isSuccessful && response.body() != null) {
                    val actionResponse = response.body()!!
                    if (actionResponse.status == "success") {
                        onResult(true, actionResponse.message)
                        // Update lokal atau fetch ulang
                        val currentState = cartUiState
                        if (currentState is CartUIState.Success) {
                            val updatedItems = currentState.items.map {
                                if (it.productId == productId) it.copy(quantity = newQuantity) else it
                            }
                            cartUiState = CartUIState.Success(updatedItems)
                        } else {
                            fetchCartItems() // Fallback jika state tidak terduga
                        }
                    } else {
                        onResult(false, actionResponse.message)
                    }
                } else {
                    onResult(false, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception updating quantity: ${e.localizedMessage}", e)
                onResult(false, "Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun removeItemFromCart(productId: Int, onResult: (Boolean, String) -> Unit) {
        val userId = currentUserId ?: run {
            onResult(false, "User ID not set.")
            return
        }

        viewModelScope.launch {
            try {
                val response = myApi.removeFromCart(userId, productId)
                if (response.isSuccessful && response.body() != null) {
                    val actionResponse = response.body()!!
                    if (actionResponse.status == "success") {
                        onResult(true, actionResponse.message)
                        // Update lokal atau fetch ulang
                        val currentState = cartUiState
                        if (currentState is CartUIState.Success) {
                            val updatedItems = currentState.items.filterNot { it.productId == productId }
                            if (updatedItems.isEmpty()) {
                                cartUiState = CartUIState.Empty
                            } else {
                                cartUiState = CartUIState.Success(updatedItems)
                            }
                        } else {
                            fetchCartItems() // Fallback
                        }
                    } else {
                        onResult(false, actionResponse.message)
                    }
                } else {
                    onResult(false, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception removing from cart: ${e.localizedMessage}", e)
                onResult(false, "Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun clearUserCart(onResult: (Boolean, String) -> Unit) {
        val userId = currentUserId ?: run {
            onResult(false, "User ID not set.")
            return
        }
        viewModelScope.launch {
            try {
                val response = myApi.clearCart(userId)
                if (response.isSuccessful && response.body() != null) {
                    val actionResponse = response.body()!!
                    if (actionResponse.status == "success") {
                        onResult(true, actionResponse.message)
                        cartUiState = CartUIState.Empty // Langsung set ke Empty
                    } else {
                        onResult(false, actionResponse.message)
                    }
                } else {
                    onResult(false, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception clearing cart: ${e.localizedMessage}", e)
                onResult(false, "Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    // Helper untuk menghitung total (bisa diletakkan di UI atau di sini)
    fun calculateSubTotal(items: List<CartItem>): Double {
        return items.sumOf { (it.harga.toDoubleOrNull() ?: 0.0) * it.quantity }
    }
}