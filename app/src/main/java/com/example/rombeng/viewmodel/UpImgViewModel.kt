package com.example.rombeng.viewmodel // Package dan nama ViewModel disesuaikan

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel // Ganti ViewModel dengan AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rombeng.model.ImageUploadResponse // Model disesuaikan
import com.example.rombeng.MyApi // API interface disesuaikan
import com.example.rombeng.service.RetrofitClient // Retrofit client disesuaikan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.example.rombeng.viewmodel.LoginViewModel

// Sealed class untuk merepresentasikan state hasil upload
sealed class UploadResult<out T> {
    data class Success<T>(val data: T) : UploadResult<T>()
    data class Error(val message: String) : UploadResult<Nothing>()
    object Loading : UploadResult<Nothing>()
    object Idle : UploadResult<Nothing>()
}

// ASUMSI: Anda memiliki cara untuk mendapatkan token, misalnya melalui SharedPreferences
// atau ViewModel lain yang di-inject.
// Contoh sederhana jika disimpan di SharedPreferences (perlu diimplementasikan):
// class TokenRepository(private val context: Context) {
//     private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
//     fun getToken(): String? = prefs.getString("auth_token", null)
// }


// Anda perlu menyediakan instance LoginViewModel atau repository yang bisa memberikan token
// Misalnya, dengan Hilt atau Koin untuk dependency injection, atau instance manual.
// Untuk contoh ini, saya akan asumsikan Anda memiliki fungsi untuk mendapatkan token.
class UpImgViewModel(
    application: Application,
    // Contoh jika Anda menginject repository untuk token:
    // private val tokenRepository: TokenRepository,
//    private val applicationContext: Context // Gunakan application context jika diperlukan untuk SharedPreferences
) : AndroidViewModel(application) {

    private val _uploadStatus = MutableLiveData<UploadResult<ImageUploadResponse>>(UploadResult.Idle)
    val uploadStatus: LiveData<UploadResult<ImageUploadResponse>> = _uploadStatus

    private val apiService: MyApi = RetrofitClient.myApi

    // --- FUNGSI LAMA uploadImage (single image) ---
    // Jika Anda masih membutuhkannya, pastikan untuk menambahkan pengiriman token juga.
    // Untuk saat ini, saya akan fokus pada uploadImagesFromPaths.
    // fun uploadImage(context: Context, imageUri: Uri) { ... }


    // Fungsi untuk mendapatkan token (INI PERLU ANDA IMPLEMENTASIKAN)
    // Ini adalah placeholder. Anda harus menggantinya dengan implementasi nyata
    // untuk mengambil token dari SharedPreferences atau LoginViewModel.
    private fun getAuthToken(): String? {
        val prefs = getApplication<Application>().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return prefs.getString("jwt_token", null)
    }



    private fun uriToFile(context: Context, uri: Uri): File? {
        val context = getApplication<Application>() // Gunakan application context
        var fileName: String? = null
        try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex)
                    }
                }
            }
            if (fileName == null) {
                fileName = "temp_image_${System.currentTimeMillis()}"
            }

            val file = File(context.cacheDir, fileName!!)
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            Log.e("UpImgViewModel", "Error converting URI to File", e)
            return null
        }
    }

    fun uploadImagesFromPaths(
        context: Context, // Dipertahankan untuk ContentResolver fallback
        imageFilePaths: List<String>,
        judul: String,
        harga: String,
        kategori: String,
        deskripsi: String,
        lokasi: String
    ) {
        if (imageFilePaths.isEmpty()) {
            _uploadStatus.value = UploadResult.Error("Tidak ada gambar yang dipilih.")
            return
        }

        // --- DAPATKAN TOKEN DI SINI ---
        val token = getAuthToken() // Panggil fungsi untuk mendapatkan token

        if (token == null || token.isEmpty()) {
            _uploadStatus.value = UploadResult.Error("Otentikasi gagal. Silakan login kembali.")
            Log.e("ViewModelUpload", "Auth token is null or empty.")
            return // Hentikan proses jika token tidak ada
        }

        // --- BARU SETELAH TOKEN DIPASTIKAN ADA ---
        _uploadStatus.value = UploadResult.Loading

        viewModelScope.launch {
            try {
                val imageParts = imageFilePaths.mapNotNull { filePath ->
                    val imageFile = File(filePath)
                    if (!imageFile.exists()) {
                        Log.e("ViewModelUpload", "File tidak ditemukan di path: $filePath")
                        return@mapNotNull null
                    }
                    val mediaTypeString = when (imageFile.extension.lowercase()) {
                        "jpg", "jpeg" -> "image/jpeg"
                        "png" -> "image/png"
                        "gif" -> "image/gif"
                        "webp" -> "image/webp" // Tambahkan webp
                        else -> context.contentResolver.getType(Uri.fromFile(imageFile))
                    }
                    val mediaType = mediaTypeString?.toMediaTypeOrNull()

                    // Menggunakan "application/octet-stream" sebagai fallback jika mediaType null
                    val actualMediaType = mediaType ?: "application/octet-stream".toMediaTypeOrNull()

                    val requestFile = imageFile.asRequestBody(actualMediaType)
                    MultipartBody.Part.createFormData("uploaded_images[]", imageFile.name, requestFile)
                }

                if (imageParts.isEmpty() && imageFilePaths.isNotEmpty()) {
                    _uploadStatus.value = UploadResult.Error("Gagal memproses file gambar untuk diunggah.")
                    return@launch
                }
                if (imageParts.isEmpty()) {
                    _uploadStatus.value = UploadResult.Error("Tidak ada gambar yang valid untuk diunggah.")
                    return@launch
                }

                val judulBody = judul.toRequestBody("text/plain".toMediaTypeOrNull())
                val hargaBody = harga.toRequestBody("text/plain".toMediaTypeOrNull())
                val kategoriBody = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
                val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
                val lokasiBody = lokasi.toRequestBody("text/plain".toMediaTypeOrNull())

                // --- BUAT HEADER OTORISASI ---
                // Token sudah dipastikan tidak null/kosong di atas
                val authorizationHeader = "Bearer $token"
                Log.d("ViewModelUpload", "Authorization Header: $authorizationHeader") // Untuk debugging

                val response = apiService.uploadMultipleImagesAndData(
                    authToken = authorizationHeader, // Kirim header di sini
                    images = imageParts,
                    judul = judulBody,
                    harga = hargaBody,
                    kategori = kategoriBody,
                    deskripsi = deskripsiBody,
                    lokasi = lokasiBody
                )

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.status == "success") {
                        _uploadStatus.value = UploadResult.Success(responseBody)
                    } else {
                        _uploadStatus.value = UploadResult.Error(responseBody.message ?: "Terjadi kesalahan pada server (multi-upload).")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ViewModelUpload", "Upload error: ${response.code()} - $errorBody")
                    _uploadStatus.value = UploadResult.Error("Gagal mengunggah. Kode: ${response.code()}. Server: $errorBody")
                }

            } catch (e: Exception) {
                Log.e("ViewModelUpload", "Exception during upload: ${e.message}", e)
                _uploadStatus.value = UploadResult.Error("Terjadi kesalahan: ${e.message ?: "Tidak diketahui"}")
            }
        }
    }

    fun resetUploadStatus() {
        _uploadStatus.value = UploadResult.Idle
    }

    fun processAndCacheUris(
        uris: List<Uri>,
        currentPaths: List<String>,
        maxImages: Int,
        context: Context, // Menggunakan context dari parameter fungsi, bukan applicationContext
        onComplete: (List<String>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newFilePaths = uris.mapNotNull { uri ->
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    // Menggunakan nama file yang lebih unik dari ContentResolver jika tersedia
                    var fileName: String? = null
                    context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (nameIndex != -1) {
                                fileName = cursor.getString(nameIndex)
                            }
                        }
                    }
                    if (fileName == null) {
                        fileName = "img_${System.currentTimeMillis()}_${uri.lastPathSegment ?: "temp"}"
                    }

                    val cacheDir = context.cacheDir
                    val file = File(cacheDir, fileName!!) // Non-null assertion karena sudah ada fallback
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()
                    file.absolutePath
                } catch (e: Exception) {
                    Log.e("ViewModelCache", "Error copying URI to cache: ${e.message}")
                    null
                }
            }
            val updatedPaths = (currentPaths + newFilePaths).distinct().take(maxImages)
            withContext(Dispatchers.Main) {
                onComplete(updatedPaths)
            }
        }
    }
}