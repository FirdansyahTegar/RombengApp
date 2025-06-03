package com.example.rombeng.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rombeng.MyApi
import com.example.rombeng.model.Product
import com.example.rombeng.model.ProductListResponse // Asumsi Anda punya ini untuk getProducts
import com.example.rombeng.model.ProductDetailResponse // Anda mungkin perlu model response baru untuk detail
import com.example.rombeng.service.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

// Sealed class untuk merepresentasikan state pengambilan LIST produk
sealed class ProductListUIState { // Ubah nama agar lebih spesifik
    object Loading : ProductListUIState()
    data class Success(val products: List<Product>) : ProductListUIState()
    data class Error(val message: String) : ProductListUIState()
    object Empty : ProductListUIState()
    object Idle : ProductListUIState()
}

// Sealed class untuk merepresentasikan state pengambilan DETAIL produk
sealed class ProductDetailUIState {
    object Loading : ProductDetailUIState()
    data class Success(val product: Product) : ProductDetailUIState() // Hanya satu produk
    data class Error(val message: String) : ProductDetailUIState()
    object NotFound : ProductDetailUIState() // Jika produk dengan ID tertentu tidak ditemukan
    object Idle : ProductDetailUIState()
}

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    // State untuk daftar produk (yang sudah Anda miliki)
    private val _productsListState = MutableLiveData<ProductListUIState>(ProductListUIState.Idle)
    val productsListState: LiveData<ProductListUIState> = _productsListState

    // State BARU untuk detail produk tunggal
    private val _productDetailState = MutableLiveData<ProductDetailUIState>(ProductDetailUIState.Idle)
    val productDetailState: LiveData<ProductDetailUIState> = _productDetailState

    private val myApi: MyApi = RetrofitClient.myApi

    // Fungsi untuk mengambil SEMUA produk (sudah ada)
    fun fetchProducts() {
        _productsListState.value = ProductListUIState.Loading
        viewModelScope.launch {
            try {
                val response = myApi.getProducts() // Endpoint untuk mendapatkan list produk
                if (response.isSuccessful) {
                    val productListResponse = response.body() // Tipe ProductListResponse?
                    if (productListResponse != null) {
                        when (productListResponse.status) { // Asumsi ProductListResponse punya field 'status' dan 'data'
                            "success" -> {
                                if (!productListResponse.data.isNullOrEmpty()) {
                                    _productsListState.value = ProductListUIState.Success(productListResponse.data)
                                } else {
                                    _productsListState.value = ProductListUIState.Empty
                                }
                            }
                            "success_empty" -> {
                                _productsListState.value = ProductListUIState.Empty
                            }
                            else -> {
                                _productsListState.value = ProductListUIState.Error(
                                    productListResponse.message ?: "Terjadi kesalahan pada server (list)."
                                )
                            }
                        }
                    } else {
                        _productsListState.value = ProductListUIState.Error("Respons tidak valid dari server (list).")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProductViewModel", "Fetch products list failed: Code: ${response.code()}, Message: ${response.message()}, ErrorBody: $errorBody")
                    _productsListState.value = ProductListUIState.Error("Gagal mengambil daftar produk. Kode: ${response.code()}")
                }
            } catch (e: IOException) {
                Log.e("ProductViewModel", "Network error fetching products list: ", e)
                _productsListState.value = ProductListUIState.Error("Kesalahan jaringan (list): ${e.message}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Unexpected error fetching products list: ", e)
                _productsListState.value = ProductListUIState.Error("Terjadi kesalahan (list): ${e.message}")
            }
        }
    }

    // Fungsi BARU untuk mengambil SATU produk berdasarkan ID
    fun fetchProductById(productId: String) {
        // Jika produk sudah ada di list dan Anda ingin optimasi, bisa diambil dari sana
        // Namun, biasanya lebih baik fetch langsung untuk data terbaru
        _productDetailState.value = ProductDetailUIState.Loading
        viewModelScope.launch {
            try {
                // Anda perlu endpoint API baru di MyApi, contoh: getProductById(productId: String): Response<ProductDetailResponse>
                // Dan model ProductDetailResponse, contoh:
                // data class ProductDetailResponse(val status: String, val data: Product?, val message: String?)
                val response = myApi.getProductById(productId) // PANGGIL ENDPOINT DETAIL

                if (response.isSuccessful) {
                    val productDetailResponse = response.body()
                    if (productDetailResponse != null) {
                        when (productDetailResponse.status) {
                            "success" -> {
                                Log.d("ProductViewModel", "API Success. Data: ${productDetailResponse.data}") // LOG DATA MENTAH
                                if (productDetailResponse.data != null) {
                                    // PERIKSA FIELD PRODUCT SEBELUM SET STATE
                                    val product = productDetailResponse.data
                                    Log.d("ProductViewModel", "Product to display: ID=${product.id}, Nama=${product.namaProduk}, Harga=${product.harga}, Kategori=${product.kategori}, Deskripsi=${product.deskripsiProduk}, Lokasi=${product.lokasi}, Penjual=${product.penjualUsername}, TglUnggah=${product.tanggalUnggah}")

                                    _productDetailState.value = ProductDetailUIState.Success(productDetailResponse.data)
                                } else {
                                    // Status success tapi data null, bisa dianggap not found atau error data
                                    _productDetailState.value = ProductDetailUIState.NotFound
                                    Log.w("ProductViewModel", "Product detail success but data is null for ID: $productId")
                                }
                            }
                            // Anda mungkin punya status khusus jika produk tidak ditemukan oleh API
                            "not_found" -> { // Contoh status dari API jika produk tidak ada
                                _productDetailState.value = ProductDetailUIState.NotFound
                            }
                            else -> {
                                _productDetailState.value = ProductDetailUIState.Error(
                                    productDetailResponse.message ?: "Terjadi kesalahan pada server (detail)."
                                )
                            }
                        }
                    } else {
                        _productDetailState.value = ProductDetailUIState.Error("Respons tidak valid dari server (detail).")
                    }
                } else {
                    if (response.code() == 404) { // HTTP 404 Not Found
                        _productDetailState.value = ProductDetailUIState.NotFound
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ProductViewModel", "Fetch product detail failed: Code: ${response.code()}, Message: ${response.message()}, ErrorBody: $errorBody")
                        _productDetailState.value = ProductDetailUIState.Error("Gagal mengambil detail produk. Kode: ${response.code()}")
                    }
                }
            } catch (e: IOException) {
                Log.e("ProductViewModel", "Network error fetching product detail: ", e)
                _productDetailState.value = ProductDetailUIState.Error("Kesalahan jaringan (detail): ${e.message}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Unexpected error fetching product detail: ", e)
                _productDetailState.value = ProductDetailUIState.Error("Terjadi kesalahan (detail): ${e.message}")
            }
        }
    }

    // init {
    //    fetchProducts() // Mungkin tidak perlu dipanggil di init jika ViewModel ini juga untuk detail
    // }

    // Fungsi untuk mereset state detail produk, bisa berguna saat keluar dari halaman detail
    fun clearProductDetailState() {
        _productDetailState.value = ProductDetailUIState.Idle
    }
}