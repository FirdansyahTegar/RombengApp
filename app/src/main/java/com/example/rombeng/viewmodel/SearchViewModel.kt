package com.example.rombeng.viewmodel // Sesuaikan dengan package Anda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rombeng.model.Product // Model data produk Anda
import com.example.rombeng.service.RetrofitClient // Instance Retrofit Anda
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// State untuk UI Pencarian
sealed class SearchUIState {
    object Idle : SearchUIState() // State awal atau setelah query kosong
    object Loading : SearchUIState()
    data class Success(val products: List<Product>) : SearchUIState()
    object Empty : SearchUIState() // Query tidak kosong, tapi tidak ada hasil
    data class Error(val message: String) : SearchUIState()
}

class SearchViewModel : ViewModel() {

    var search by mutableStateOf("")
        private set // Hanya bisa diubah dari dalam ViewModel

    var searchResultsState by mutableStateOf<SearchUIState>(SearchUIState.Idle)
        private set

    private var searchJob: Job? = null

    fun onSearchChange(newQuery: String) {
        search = newQuery
        searchJob?.cancel() // Batalkan job pencarian sebelumnya jika ada

        if (newQuery.isBlank()) {
            searchResultsState = SearchUIState.Idle // Atau SearchUIState.Empty jika Anda mau
            return
        }

        // Debounce: Tunggu sebentar sebelum melakukan pencarian untuk menghindari terlalu banyak request API
        searchJob = viewModelScope.launch {
            delay(500L) // Tunggu 500ms setelah pengguna berhenti mengetik
            performSearch(newQuery)
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            searchResultsState = SearchUIState.Loading
            try {
                val response = RetrofitClient.myApi.searchProducts(query)
                if (response.isSuccessful) {
                    val productListResponse = response.body()
                    if (productListResponse != null && productListResponse.status == "success") {
                        val productsData = productListResponse.data // productsData adalah List<Product>?
                        if (productsData != null) { // Pemeriksaan null eksplisit
                            if (productsData.isNotEmpty()) {
                                searchResultsState = SearchUIState.Success(productsData) // Aman karena sudah dicek null
                            } else {
                                searchResultsState = SearchUIState.Empty
                            }
                        } else {
                            // productListResponse.data adalah null
                            searchResultsState = SearchUIState.Empty // Atau Error
                        }
                    } else if (productListResponse != null && productListResponse.status == "success_no_results") {
                        // Kasus khusus dari backend jika tidak ada hasil
                        searchResultsState = SearchUIState.Empty
                    }
                    else if (productListResponse != null && productListResponse.status == "success_empty_query") {
                        // Kasus khusus dari backend jika query dikirim kosong (meskipun sudah dicek di onSearchChange)
                        searchResultsState = SearchUIState.Idle
                    }
                    else {
                        // Status dari API bukan 'success' atau body null
                        searchResultsState = SearchUIState.Error(productListResponse?.message ?: "Gagal mengambil data pencarian.")
                    }
                } else {
                    // Respons HTTP tidak sukses (misalnya 404, 500)
                    searchResultsState = SearchUIState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                // Exception saat panggilan jaringan atau pemrosesan
                searchResultsState = SearchUIState.Error("Terjadi kesalahan: ${e.localizedMessage ?: "Tidak diketahui"}")
            }
        }
    }

    // Panggil ini jika Anda ingin melakukan pencarian secara manual, misalnya saat tombol search ditekan
    fun triggerSearch() {
        if (search.isNotBlank()) {
            searchJob?.cancel()
            performSearch(search)
        } else {
            searchResultsState = SearchUIState.Idle
        }
    }
}