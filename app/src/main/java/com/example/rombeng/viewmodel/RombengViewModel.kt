package com.example.rombeng.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rombeng.MyApi
//import com.example.rombeng.data.RombengRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.rombeng.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Patterns
import androidx.compose.runtime.remember
import com.example.rombeng.model.AddUserResponse
import com.example.rombeng.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.InetSocketAddress
import java.net.Socket

import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.input.delete
import com.example.rombeng.model.GoogleRegisterRequest
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException



class RombengViewModel : ViewModel() {

    private val _selectedUser   = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun fetchUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.myApi.getUsers()
                Log.d("RombengViewModel", "Fetched: $response")
                _users.value = response
            } catch (e: Exception) {
                Log.e("RombengViewModel", "Error fetching users", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = user
    }

    var email by mutableStateOf("")
        private set
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun validateEmail(text: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }

    var password by mutableStateOf("")
        private set
    fun onPassChange(newPass: String) {
        if (newPass.length <= 64) {
            password = newPass
        }
    }

    var confPass by mutableStateOf("")
        private set
    fun onConfPassChange(newConfPass: String) {
        if (newConfPass.length <= 64) {
            confPass = newConfPass
        }
    }

    var name by mutableStateOf("")
        private set
    fun onNameChange(newName: String) {
        if (newName.length <= 40) {
            name = newName
        }
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

    var error by mutableStateOf(false)
    var succes by mutableStateOf(false)
    var Loading by mutableStateOf(false)
    var regButton by mutableStateOf(true)
    var message by mutableStateOf("")

    // Untuk Google Sign-In
    private lateinit var oneTapClient: SignInClient
    private val _googleSignInIntentSender = MutableStateFlow<IntentSenderRequest?>(null)
    val googleSignInIntentSender: StateFlow<IntentSenderRequest?> = _googleSignInIntentSender.asStateFlow()

    private fun resetAuthStates() {
        error = false
        succes = false
        message = ""
        regButton = true
        Loading = false
        _googleSignInIntentSender.value = null // Reset intent sender
    }

    fun registerUser() {
        resetAuthStates()
        regButton = false
        succes = false
        if (name.isBlank() || email.isBlank() || password.isBlank() || confPass.isBlank()) {
            error = true
            message = "Semua field harus terisi"
        }

        else if (name.length < 6) {
            error = true
            message = "Username harus lebih dari 6 karakter"
        }

        else if (password.length < 8) {
            error = true
            message = "Password harus lebih dari 8 karakter"
        } else if (password != confPass) {
            error = true
            message = "Password tidak sama"
        } else if (!validateEmail(email)) {
            error = true
            message = "Email tidak valid"
        } else {
            error = false
            Loading = true
            RetrofitClient.myApi.addUser(name, password, email)
                .enqueue(object : Callback<AddUserResponse> {
                    override fun onResponse(
                        call: Call<AddUserResponse>,
                        response: Response<AddUserResponse>
                    ) {
                        Loading = false
                        if (response.isSuccessful) {
                            succes = true
                            message = response.body()?.message ?: "Register Berhasil"
                        } else {
                            error = true
                            val errorBody = response.errorBody()?.string()
                            message = try {
                                // Coba parse error JSON dari backend jika ada
                                org.json.JSONObject(errorBody ?: "{}").getString("error")
                            } catch (e: Exception) {
                                "Terjadi Kesalahan, Error: ${response.code()}"
                            }
                            Log.w("RombengVM_ManualReg", "Error: ${response.code()}, Body: $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                        Loading = false
                        error = true
                        message = "Error: ${t.localizedMessage}"
                        Log.e("Rombeng Register", "Failure", t)
                    }
                }
            )
        }
        regButton = true
    }


    // --- Registrasi dengan Google ---
    /**
     * Memulai proses Google Sign-Up.
     * Fungsi ini harus dipanggil dari UI (misalnya, saat tombol "Register with Google" diklik).
     *
     * @param activity Activity yang sedang berjalan, diperlukan untuk mendapatkan SignInClient.
     * @param serverClientId Web Client ID Anda dari Google Cloud Console.
     */
    fun beginGoogleSignUp(activity: Activity, serverClientId: String) {
        resetAuthStates() // Reset state sebelum memulai
        regButton = false // Nonaktifkan tombol selama proses
        Loading = true // Tampilkan loading indicator

        oneTapClient = Identity.getSignInClient(activity)
        val signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(serverClientId) // SERVER_CLIENT_ID Anda
                    .setFilterByAuthorizedAccounts(false) // Tampilkan semua akun Google
                    .build()
            )
            .setAutoSelectEnabled(false) // Jangan auto select, biarkan pengguna memilih
            .build()

        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    _googleSignInIntentSender.value = intentSenderRequest
                    // Loading tetap true, akan dihandle setelah hasil dari ActivityResultLauncher
                } catch (e: Exception) {
                    Log.e("RombengVM_GoogleSignUp", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    Loading = false
                    regButton = true
                    error = true
                    message = "Gagal memulai Google Sign-Up: ${e.localizedMessage}"
                }
            }
            .addOnFailureListener { e ->
                Log.e("RombengVM_GoogleSignUp", "Google Sign-Up failed: ${e.localizedMessage}")
                Loading = false
                regButton = true
                error = true
                message = "Registrasi Google gagal: ${e.localizedMessage}"
            }
    }

//    /**
//     * Menangani hasil dari ActivityResultLauncher setelah pengguna berinteraksi dengan dialog Google Sign-In.
//     *
//     * @param data Intent yang diterima dari hasil Activity.
//     */
//    fun handleGoogleSignUpResult(data: Intent?) {
//        // Loading sudah true dari beginGoogleSignUp
//        try {
//            val credential = oneTapClient.getSignInCredentialFromIntent(data)
//            val idToken = credential.googleIdToken
//            // val username = credential.displayName // Bisa digunakan jika perlu di UI atau dikirim ke server
//            // val email = credential.id // Email pengguna, juga bisa diambil dari token di backend
//
//            if (idToken != null) {
//                Log.d("RombengVM_GoogleSignUp", "Google ID Token: $idToken")
//                sendIdTokenToBackendForRegistration(idToken)
//            } else {
//                Log.w("RombengVM_GoogleSignUp", "Google ID Token is null.")
//                Loading = false
//                regButton = true
//                error = true
//                message = "Gagal mendapatkan Google ID Token."
//                _googleSignInIntentSender.value = null // Reset intent sender
//            }
//        } catch (e: ApiException) {
//            Loading = false
//            regButton = true
//            _googleSignInIntentSender.value = null // Reset intent sender
//            when (e.statusCode) {
//                CommonStatusCodes.CANCELED -> {
//                    Log.d("RombengVM_GoogleSignUp", "One-tap dialog was closed by user.")
//                    // Tidak set error, biarkan pengguna mencoba lagi jika mau
//                    message = "Proses Sign-Up Google dibatalkan." // Pesan opsional
//                }
//                CommonStatusCodes.NETWORK_ERROR -> {
//                    Log.w("RombengVM_GoogleSignUp", "One-tap network error.", e)
//                    error = true
//                    message = "Kesalahan jaringan saat Sign-Up Google."
//                }
//                else -> {
//                    Log.w("RombengVM_GoogleSignUp", "Couldn't get credential from result.", e)
//                    error = true
//                    message = "Gagal mendapatkan kredensial Google: ${e.localizedMessage}"
//                }
//            }
//        }
//    }
//
//    /**
//     * Mengirimkan ID Token Google ke backend (add_users.php) untuk proses registrasi.
//     *
//     * @param idToken Token ID Google yang diperoleh.
//     */
//    private fun sendIdTokenToBackendForRegistration(idToken: String) {
//        // Loading sudah true
//        val requestBody = GoogleRegisterRequest(google_id_token = idToken)
//
//        RetrofitClient.myApi.registerWithGoogle(requestBody)
//            .enqueue(object : Callback<AddUserResponse> {
//                override fun onResponse(
//                    call: Call<AddUserResponse>,
//                    response: Response<AddUserResponse>
//                ) {
//                    Loading = false
//                    regButton = true
//                    _googleSignInIntentSender.value = null // Reset intent sender setelah selesai
//                    if (response.isSuccessful) {
//                        succes = true
//                        message = response.body()?.message ?: "Registrasi Google Berhasil"
//                        Log.i("RombengVM_GoogleReg", "Registration successful: $message")
//                        // resetTextField() // Opsional: reset field jika ada yang terisi
//                    } else {
//                        error = true
//                        val errorBody = response.errorBody()?.string()
//                        message = try {
//                            org.json.JSONObject(errorBody ?: "{}").getString("error")
//                        } catch (e: Exception) {
//                            "Registrasi Google gagal, Error: ${response.code()}"
//                        }
//                        Log.w("RombengVM_GoogleReg", "Backend registration failed: ${response.code()}, Body: $errorBody")
//                    }
//                }
//
//                override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
//                    Loading = false
//                    regButton = true
//                    error = true
//                    message = "Error registrasi Google: ${t.localizedMessage}"
//                    Log.e("RombengVM_GoogleReg", "Failure sending token to backend", t)
//                    _googleSignInIntentSender.value = null // Reset intent sender
//                }
//            })
//    }

    /**
     * Panggil ini dari Composable ketika ActivityResultLauncher untuk Google Sign-In
     * telah selesai dan intent sender tidak lagi dibutuhkan.
     * Atau panggil di dalam blok onResponse/onFailure dari sendIdTokenToBackendForRegistration.
     */
    fun consumeGoogleSignInIntent() {
        _googleSignInIntentSender.value = null
    }


    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> = _uploadStatus

    private val apiService = RetrofitClient.myApi




//    suspend fun hasInternetConnection(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork ?: return false
//        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//
//        val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
//
//        if (!isConnected) return false
//
//        // Uji koneksi ke server (misal Google)
//        return withContext(Dispatchers.IO) {
//            try {
//                val socket = Socket()
//                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
//                socket.close()
//                true
//            } catch (e: Exception) {
//                false
//            }
//        }
//    }
//

}

