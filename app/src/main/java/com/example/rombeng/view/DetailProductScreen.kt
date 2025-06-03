package com.example.rombeng.view // Sesuaikan package Anda

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState // Jika ViewModel masih menggunakan LiveData
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage // Untuk memuat gambar dari URL
import com.example.rombeng.model.Product // Model Product Anda
import com.example.rombeng.viewmodel.ProductViewModel
import com.example.rombeng.viewmodel.ProductDetailUIState // Import UI state
import com.example.rombeng.viewmodel.CartViewModel
import com.example.rombeng.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(
    navController: NavController,
    productId: String,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current

    // Panggil fetchProductById saat composable pertama kali dibuat atau productId berubah
    LaunchedEffect(key1 = productId) {
        Log.d("DetailProductScreen", "Memuat detail untuk produk ID: $productId")
        productViewModel.fetchProductById(productId)
    }

    // Amati productDetailState dari ViewModel
    // Jika ProductViewModel menggunakan LiveData:
    val productDetailStateObserved = productViewModel.productDetailState.observeAsState()
    val productDetailState = productDetailStateObserved.value ?: ProductDetailUIState.Idle
    // Jika ProductViewModel menggunakan StateFlow (lebih direkomendasikan untuk Compose):
    // val productDetailState by productViewModel.productDetailStateFlow.collectAsState()


    var isLoadingAddToCart by remember { mutableStateOf(false) }
    val currentUser = loginViewModel.getCurrentUser() // Ambil user yang sedang login

    // Inisialisasi CartViewModel dengan userId dari currentUser
    LaunchedEffect(key1 = currentUser?.id) {
        if (currentUser != null && currentUser.id != -1) { // Asumsi ID -1 berarti tidak valid/tidak login
            cartViewModel.initializeUser(currentUser.id)
            Log.d("DetailProductScreen", "CartViewModel diinisialisasi dengan User ID: ${currentUser.id}")
        }
    }

    // Opsional: Clear state detail produk saat layar ini dihancurkan (keluar)
    DisposableEffect(Unit) {
        onDispose {
            Log.d("DetailProductScreen", "Membersihkan state detail produk.")
            productViewModel.clearProductDetailState() // Pastikan fungsi ini ada di ViewModel
        }
    }

    Scaffold(
        topBar = {
            var topBarTitle = "Detail Produk"
            if (productDetailState is ProductDetailUIState.Success) {
                topBarTitle = (productDetailState as ProductDetailUIState.Success).product.namaProduk?: "Nama Produk tidak tersedia"
            }
            TopAppBar(
                title = { Text(topBarTitle, maxLines = 1, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.Black // Sesuaikan warna ikon
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF9800) // Warna AppBar
                )
            )
        },
        bottomBar = {
            if (productDetailState is ProductDetailUIState.Success) {
                val product = (productDetailState as ProductDetailUIState.Success).product
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp) // Sesuaikan tinggi jika perlu
                        .background(Color.White) // Background untuk bottom bar
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button( // Tombol Chat
                        onClick = {
                            Toast.makeText(context, "Fitur chat belum diimplementasikan.", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray // Warna tombol chat
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Chat Penjual", color = Color.White, fontSize = 14.sp)
                    }

                    Button( // Tombol Masukkan Keranjang
                        onClick = {
                            val activeUser = loginViewModel.getCurrentUser()
                            val cartVmUserId = cartViewModel.currentUserId
                            val productToAddId = product.id // ID produk sudah Int dari model Product

                            Log.d("DetailProductScreen", "Tombol keranjang diklik. User ID: ${activeUser?.id}, CartVM UserID: $cartVmUserId, ProductID: $productToAddId")

                            if (activeUser != null && activeUser.id != -1) {
                                if (cartVmUserId != null && cartVmUserId == activeUser.id) {
                                    isLoadingAddToCart = true
                                    cartViewModel.addProductToCart(
                                        productId = productToAddId,
                                        onResult = { success, message ->
                                            isLoadingAddToCart = false
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                            if (success) {
                                                Log.i("DetailProductScreen", "Produk ${product.namaProduk} berhasil ditambahkan ke keranjang.")
                                                // Mungkin navigasi ke keranjang atau update UI lain
                                            } else {
                                                Log.e("DetailProductScreen", "Gagal menambahkan ${product.namaProduk}: $message")
                                            }
                                        }
                                    )
                                } else {
                                    Toast.makeText(context, "Sinkronisasi keranjang, silakan coba lagi.", Toast.LENGTH_SHORT).show()
                                    Log.w("DetailProductScreen", "UserID CartViewModel ($cartVmUserId) tidak cocok atau belum diset. Diharapkan: ${activeUser.id}. Menginisialisasi ulang.")
                                    // Inisialisasi ulang jika belum sinkron
                                    cartViewModel.initializeUser(activeUser.id)
                                }
                            } else {
                                Toast.makeText(context, "Anda harus login untuk menambahkan produk ke keranjang.", Toast.LENGTH_LONG).show()
                                // navController.navigate(Screen.Login.route) // Arahkan ke login jika perlu
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF7A00) // Warna tombol keranjang
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoadingAddToCart && currentUser != null && currentUser.id != -1
                    ) {
                        if (isLoadingAddToCart) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Masukkan Keranjang", color = Color.Black, fontSize = 10.sp, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        },
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val state = productDetailState) {
                is ProductDetailUIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFF9800))
                        Text("Memuat detail...", modifier = Modifier.padding(top = 60.dp))
                    }
                }
                is ProductDetailUIState.Success -> {
                    ProductDetailContent(product = state.product)
                }
                is ProductDetailUIState.Error -> {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "Gagal memuat detail produk: ${state.message}",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }
                }
                is ProductDetailUIState.NotFound -> {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "Produk yang Anda cari tidak ditemukan.",
                            fontSize = 16.sp
                        )
                    }
                }
                is ProductDetailUIState.Idle -> {
                    // State awal, bisa juga menampilkan loading atau teks placeholder
                    // Seharusnya sudah ditangani oleh Loading atau LaunchedEffect yang memicu fetch
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Menunggu data produk...", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Carousel atau Pager untuk gambar jika ada lebih dari satu
        if (!product.gambarUrls.isNullOrEmpty()) {
            // Untuk kesederhanaan, tampilkan gambar pertama.
            // Anda bisa menggunakan HorizontalPager dari Accompanist atau Foundation jika banyak gambar.
            AsyncImage(
                model = product.gambarUrls.firstOrNull(), // Ambil gambar pertama
                contentDescription = "Gambar Produk: ${product.namaProduk}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Sesuaikan tinggi gambar
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop // Atau ContentScale.Fit
            )
        } else {
            Box( // Placeholder jika tidak ada gambar
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Tidak ada gambar tersedia")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.namaProduk?: "Nama Produk Tidak Tersedia",
            style = MaterialTheme.typography.headlineMedium, // Lebih besar dari sebelumnya
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.harga?: "Harga tidak tersedia", // Harga sudah diformat dari PHP "RpX.XXX.XXX"
            style = MaterialTheme.typography.titleLarge, // Lebih besar
            color = Color(0xFFE65100), // Warna harga yang lebih menonjol
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kategori: ${product.kategori}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Informasi Penjual",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            // Placeholder gambar profil penjual (ganti dengan AsyncImage jika ada URL)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Icon(
                    contentDescription = "Penjual",
                    imageVector = Icons.Default.AccountCircle,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    product.penjualUsername ?: "Nama Penjual Tidak Tersedia",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    product.lokasi?: "Lokasi Tidak Tersedia", // Lokasi produk, bisa juga lokasi penjual
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Text(
            "Deskripsi Produk",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            product.deskripsiProduk?: "Deskripsi tidak tersedia",
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 22.sp // Jarak antar baris agar lebih mudah dibaca
        )

        // Informasi Tambahan jika ada (misal, tanggal unggah, stok jika ada)
        if (product.tanggalUnggah != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Info Tambahan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Diupload pada: ${product.tanggalUnggah}", style = MaterialTheme.typography.bodyMedium)
            // Jika ada stok:
            // Text("Stok: ${product.stok ?: 'Tidak tersedia'}", style = MaterialTheme.typography.bodyMedium)
        }


        Spacer(modifier = Modifier.height(24.dp)) // Spacer tambahan di akhir konten
    }
}

// Untuk Preview (opsional)
// @Preview(showBackground = true)
// @Composable
// fun DetailProductScreenPreview() {
//    // Buat NavController dummy
//    val navController = rememberNavController()
//    // Buat ViewModel dummy atau gunakan data dummy langsung
//    val dummyProduct = Product(
//        id = 1,
//        namaProduk = "Contoh Produk Keren",
//        harga = "Rp1.250.000",
//        kategori = "Elektronik",
//        deskripsiProduk = "Ini adalah deskripsi yang sangat panjang dan menarik untuk contoh produk yang sedang ditampilkan. Produk ini memiliki banyak fitur unggulan dan kualitas terbaik di kelasnya.",
//        lokasi = "Jakarta Pusat",
//        gambarUrls = listOf("https://via.placeholder.com/600x400.png?text=Gambar+Produk"),
//        userId = 101,
//        penjualUsername = "Toko Jaya Abadi",
//        tanggalUnggah = "2023-10-26"
//    )
//    val productViewModel = // ... (cara mock ViewModel atau state-nya)
//    val cartViewModel = // ...
//    val loginViewModel = // ...

//    // Untuk preview, Anda bisa langsung memanggil ProductDetailContent
//    // atau mock ProductViewModel untuk mengembalikan ProductDetailUIState.Success
//    RombengTheme { // Ganti dengan Theme aplikasi Anda
//        ProductDetailContent(product = dummyProduct)
//    }
// }