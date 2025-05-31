// Contoh ProductListResponse.kt
import com.example.rombeng.model.Product // atau path model Product Anda

data class ProductListResponse(
    val status: String,
    val message: String?,
    val data: List<Product>? // Tandai sebagai nullable jika API bisa mengembalikannya sebagai null
)