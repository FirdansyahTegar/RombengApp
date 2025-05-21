package com.example.rombeng.viewmodel // Sesuaikan dengan package Anda

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rombeng.MyApi
import com.example.rombeng.model.LoginRequest
import com.example.rombeng.model.LoginResponse
import com.example.rombeng.model.User
import com.example.rombeng.repository.UserRepository
import com.example.rombeng.service.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException // Untuk menangani network error

// Konstanta untuk SharedPreferences
private const val PREFS_NAME = "auth_prefs"
private const val KEY_TOKEN = "jwt_token"
private const val KEY_USER_ID = "user_id"
private const val KEY_USERNAME = "username"
private const val KEY_EMAIL = "user_email"
private const val KEY_IS_LOGGED_IN = "is_logged_in"

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // LiveData untuk hasil login
    private val _loginResult = MutableLiveData<LoginUIState>()
    val loginResult: LiveData<LoginUIState> = _loginResult

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        // Inisialisasi UserRepository dengan instance ApiService dari RetrofitClient
        val apiService = RetrofitClient.myApi
        userRepository = UserRepository(apiService)
    }


    var email by mutableStateOf("")
        private set
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    var password by mutableStateOf("")
        private set
    fun onPassChange(newPass: String) {
        password = newPass
    }

    var confPass by mutableStateOf("")
        private set
    fun onConfPassChange(newConfPass: String) {
        confPass = newConfPass
    }

    var name by mutableStateOf("")
        private set
    fun onNameChange(newName: String) {
        name = newName
    }

    var search by mutableStateOf("")
        private set
    fun onSearchChange(newSearch: String) {
        search = newSearch
    }

    fun resetTextField() {
        onNameChange("")
        onPassChange("")
        onConfPassChange("")
        onEmailChange("")
    }


    fun login(emailOrUsername: String, password: String) {
        // Validasi input dasar di ViewModel (opsional, bisa juga di UI)
        if (emailOrUsername.isBlank() || password.isBlank()) {
            _loginResult.value = LoginUIState.Error("Email/Username dan password tidak boleh kosong.")
            return
        }

        _isLoading.value = true // Mulai loading
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(emailOrUsername, password)
                val response = userRepository.loginUser(loginRequest)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        if (loginResponse.status == "success" && loginResponse.token != null) {
                            // Login berhasil, simpan token dan data pengguna
                            saveAuthData(
                                loginResponse.token,
                                loginResponse.user?.id,
                                loginResponse.user?.username,
                                loginResponse.user?.email
                            )
                            _loginResult.value = LoginUIState.Success(loginResponse.message, loginResponse.user)
                        } else {
                            // Login gagal dari sisi server (misalnya, kredensial salah)
                            _loginResult.value = LoginUIState.Error(loginResponse.message)
                        }
                    } else {
                        // Respons body null, meskipun isSuccessful true (jarang terjadi untuk login)
                        _loginResult.value = LoginUIState.Error("Respons tidak valid dari server.")
                    }
                } else {
                    // HTTP error (misalnya, 404, 500)
                    val errorBody = response.errorBody()?.string() // Coba dapatkan pesan error dari body
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        "Error: ${response.code()} ${response.message()}"
                    } else {
                        // Coba parse errorBody jika formatnya JSON dan mengandung 'message'
                        // Untuk kesederhanaan, kita tampilkan langsung atau pesan default
                        try {
                            // Asumsi errorBody adalah JSON dengan field "message"
                            val errorResponse = com.google.gson.Gson().fromJson(errorBody, LoginResponse::class.java)
                            errorResponse.message
                        } catch (e: Exception) {
                            "Error: ${response.code()} - Gagal memproses pesan error server."
                        }
                    }
                    _loginResult.value = LoginUIState.Error(errorMessage)
                    Log.e("LoginViewModel", "Login failed: Code: ${response.code()}, Message: ${response.message()}, ErrorBody: $errorBody")
                }
            } catch (e: IOException) {
                // Network error (misalnya, tidak ada koneksi internet)
                _loginResult.value = LoginUIState.Error("Kesalahan jaringan: ${e.message}")
                Log.e("LoginViewModel", "Network error: ", e)
            } catch (e: Exception) {
                // Error lainnya yang tidak terduga
                _loginResult.value = LoginUIState.Error("Terjadi kesalahan: ${e.message}")
                Log.e("LoginViewModel", "Unexpected error: ", e)
            } finally {
                _isLoading.value = false // Selesai loading
            }
        }
    }

    private fun saveAuthData(token: String, userId: Int?, username: String?, email: String?) {
        with(sharedPreferences.edit()) {
            putString(KEY_TOKEN, token)
            if (userId != null) putInt(KEY_USER_ID, userId) else remove(KEY_USER_ID)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply() // Simpan secara asynchronous
        }
        Log.d("LoginViewModel", "Token and user data saved. Token: $token")
    }

    fun logout() {
        with(sharedPreferences.edit()) {
            remove(KEY_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            remove(KEY_EMAIL)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
        // Anda mungkin ingin menavigasi pengguna kembali ke layar login di sini
        // atau memberi tahu UI bahwa logout berhasil.
        _loginResult.value = LoginUIState.LoggedOut
        Log.d("LoginViewModel", "User logged out.")
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getCurrentUser(): User? {
        val userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)

        return if (userId != -1 && username != null && email != null) {
            User(userId, username, email)
        } else {
            // Data pengguna tidak lengkap, mungkin perlu logout atau membersihkan sisa data
            Log.w("LoginViewModel", "Current user data is incomplete. Forcing logout.")
            logout() // Opsional: paksa logout jika data tidak konsisten
            null
        }
    }
}

// Sealed class/interface untuk merepresentasikan state UI login
sealed class LoginUIState {
    object Idle : LoginUIState()
    data class Success(val message: String, val user: User?) : LoginUIState()
    data class Error(val message: String) : LoginUIState()
    object LoggedOut : LoginUIState() // State setelah logout
    // Anda bisa menambahkan state lain jika perlu, misalnya Loading, Idle
}