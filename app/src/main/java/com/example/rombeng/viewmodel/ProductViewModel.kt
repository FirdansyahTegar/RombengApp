// ProductViewModel.kt (misalnya di dalam package viewmodel)
package com.example.rombeng.viewmodel // Sesuaikan package Anda

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rombeng.MyApi
import com.example.rombeng.model.Product // Import model Product
import com.example.rombeng.service.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

// Sealed class untuk merepresentasikan state pengambilan produk
sealed class ProductUIState {
    object Loading : ProductUIState()
    data class Success(val products: List<Product>) : ProductUIState()
    data class Error(val message: String) : ProductUIState()
    object Empty : ProductUIState() // Untuk kasus ketika data berhasil diambil tapi kosong
    object Idle : ProductUIState()
}

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val _productsState = MutableLiveData<ProductUIState>(ProductUIState.Idle)
    val productsState: LiveData<ProductUIState> = _productsState

    private val apiService: MyApi = RetrofitClient.myApi

    fun fetchProducts() {
        _productsState.value = ProductUIState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    val productListResponse = response.body()
                    if (productListResponse != null) {
                        when (productListResponse.status) {
                            "success" -> {
                                if (!productListResponse.data.isNullOrEmpty()) {
                                    _productsState.value = ProductUIState.Success(productListResponse.data)
                                } else {
                                    // Status success tapi data null atau kosong dari API (seharusnya tidak terjadi jika API mengirim 'success_empty')
                                    _productsState.value = ProductUIState.Empty
                                }
                            }
                            "success_empty" -> {
                                _productsState.value = ProductUIState.Empty
                            }
                            else -> {
                                // Status dari API mengindikasikan error (misalnya, error_db_query)
                                _productsState.value = ProductUIState.Error(productListResponse.message ?: "Terjadi kesalahan pada server.")
                            }
                        }
                    } else {
                        _productsState.value = ProductUIState.Error("Respons tidak valid dari server.")
                    }
                } else {
                    // HTTP error (misalnya, 404, 500)
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProductViewModel", "Fetch products failed: Code: ${response.code()}, Message: ${response.message()}, ErrorBody: $errorBody")
                    _productsState.value = ProductUIState.Error("Gagal mengambil data produk. Kode: ${response.code()}")
                }
            } catch (e: IOException) {
                // Network error
                Log.e("ProductViewModel", "Network error fetching products: ", e)
                _productsState.value = ProductUIState.Error("Kesalahan jaringan: ${e.message}")
            } catch (e: Exception) {
                // Error lainnya
                Log.e("ProductViewModel", "Unexpected error fetching products: ", e)
                _productsState.value = ProductUIState.Error("Terjadi kesalahan tidak terduga: ${e.message}")
            }
        }
    }

    // Panggil fungsi ini saat pertama kali ViewModel dibuat atau saat refresh
    init {
        fetchProducts()
    }
}