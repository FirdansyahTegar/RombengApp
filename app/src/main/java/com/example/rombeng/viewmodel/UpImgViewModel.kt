package com.example.rombeng.viewmodel

import android.app.Application
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
//import androidx.datastore.core.use
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rombeng.service.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class UpImgViewModel(application: Application) : AndroidViewModel(application) {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> = _uploadStatus

    // Gunakan instance Retrofit yang sudah dibuat
    private val apiService = RetrofitClient.myApi // Menggunakan RetrofitInstance

    fun uploadSingleImageToServer(imageUri: Uri, description: String? = null) {
        viewModelScope.launch {
            _uploadStatus.value = "Mengunggah gambar..."
            try {
                val context = getApplication<Application>().applicationContext

                // 1. Dapatkan InputStream dari URI
                val inputStream = ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor)
                if (inputStream == null) {
                    _uploadStatus.value = "Gagal mendapatkan input stream dari URI."
                    return@launch
                }

                // 2. Buat file temporer di cache directory
                val fileName = context.contentResolver.getFileName(imageUri)
                val file = File(context.cacheDir, fileName)
                val outputStream = FileOutputStream(file)

                // 3. Salin data dari InputStream ke FileOutputStream
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                // 4. Buat RequestBody dari file
                val mediaType = context.contentResolver.getType(imageUri)?.toMediaTypeOrNull()
                val requestFile = file.asRequestBody(mediaType)

                // 5. Buat MultipartBody.Part
                // Nama "uploaded_image" HARUS SAMA dengan yang diharapkan di sisi server PHP ($_FILES['uploaded_image'])
                val imagePart = MultipartBody.Part.createFormData("uploaded_image", file.name, requestFile)

                // 6. Buat RequestBody untuk data tambahan (jika ada)
                var descriptionPart: RequestBody? = null
                if (description != null) {
                    descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                }

                // 7. Panggil API
                val response = apiService.uploadImage(imagePart, descriptionPart)

                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    if (uploadResponse != null && uploadResponse.status == "success") {
                        _uploadStatus.value = "Upload berhasil: ${uploadResponse.message} - Path: ${uploadResponse.filePath}"
                    } else {
                        _uploadStatus.value = "Upload gagal dari server: ${uploadResponse?.message ?: "Respons tidak valid atau kosong"}"
                    }
                } else {
                    // Tangani error HTTP (misalnya 404, 500)
                    val errorBody = response.errorBody()?.string()
                    _uploadStatus.value = "Error HTTP ${response.code()}: ${errorBody ?: "Tidak ada pesan error dari server"}"
                }

                // 8. Hapus file cache setelah selesai (baik berhasil maupun gagal, opsional)
                file.delete()

            } catch (e: Exception) {
                _uploadStatus.value = "Exception saat mengunggah: ${e.message}"
                e.printStackTrace() // Penting untuk debugging
            }
        }
    }

    // Fungsi untuk mengupload banyak gambar
    fun uploadMultipleImagesToServer(imageUris: List<Uri>, descriptions: List<String>? = null) {
        if (imageUris.isEmpty()) {
            _uploadStatus.value = "Tidak ada gambar yang dipilih untuk diunggah."
            return
        }

        viewModelScope.launch {
            _uploadStatus.value = "Mengunggah ${imageUris.size} gambar..."
            var successCount = 0
            var failureCount = 0
            val totalImages = imageUris.size
            val results = mutableListOf<String>()

            imageUris.forEachIndexed { index, uri ->
                try {
                    val context = getApplication<Application>().applicationContext
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    if (inputStream == null) {
                        results.add("Gambar ${index + 1}: Gagal mendapatkan input stream.")
                        failureCount++
                        return@forEachIndexed // Lanjut ke URI berikutnya
                    }

                    val fileName = context.contentResolver.getFileName(uri)
                    val file = File(context.cacheDir, "temp_${index}_${fileName}") // Nama file unik untuk cache
                    val outputStream = FileOutputStream(file)
                    inputStream.use { input -> outputStream.use { output -> input.copyTo(output) } }

                    val mediaType = context.contentResolver.getType(uri)?.toMediaTypeOrNull()
                    val requestFile = file.asRequestBody(mediaType)
                    val imagePart = MultipartBody.Part.createFormData("uploaded_image", file.name, requestFile)

                    var descriptionPart: RequestBody? = null
                    if (descriptions != null && descriptions.size > index && descriptions[index].isNotBlank()) {
                        descriptionPart = descriptions[index].toRequestBody("text/plain".toMediaTypeOrNull())
                    }

                    // Panggil API untuk setiap gambar
                    val response = apiService.uploadImage(imagePart, descriptionPart)

                    if (response.isSuccessful && response.body()?.status == "success") {
                        results.add("Gambar ${index + 1} (${file.name}): Berhasil - ${response.body()?.message}")
                        successCount++
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: response.body()?.message ?: "Error tidak diketahui"
                        results.add("Gambar ${index + 1} (${file.name}): Gagal - ${response.code()} $errorMsg")
                        failureCount++
                    }
                    file.delete() // Hapus file cache
                } catch (e: Exception) {
                    results.add("Gambar ${index + 1}: Exception - ${e.message}")
                    failureCount++
                    e.printStackTrace()
                }
                // Update status progresif
                _uploadStatus.value = "Proses: ${index + 1}/$totalImages. Berhasil: $successCount, Gagal: $failureCount."
            }
            // Status akhir
            _uploadStatus.value = "Selesai mengunggah $totalImages gambar. Berhasil: $successCount, Gagal: $failureCount.\nDetail:\n${results.joinToString("\n")}"
        }
    }
}