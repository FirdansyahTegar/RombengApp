package com.example.rombeng.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Import untuk items di LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator // Material (bukan M3) untuk konsistensi dengan TODO lama
import androidx.compose.material.Divider
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete // Icon untuk hapus
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button // Material3 Button
import androidx.compose.material3.ButtonDefaults // Material3 ButtonDefaults
import androidx.compose.material3.Checkbox // Material3 Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Material3 IconButton
import androidx.compose.material3.Text // Material3 Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter // Untuk memuat gambar dari URL
import com.example.rombeng.R
import com.example.rombeng.model.CartItem
import com.example.rombeng.viewmodel.CartUIState
import com.example.rombeng.viewmodel.CartViewModel
import com.example.rombeng.viewmodel.LoginViewModel // Pastikan ini diimport
import java.text.NumberFormat
import java.util.Locale

// Helper function untuk format mata uang (opsional, tapi bagus untuk tampilan)
fun formatCurrency(amount: Double): String {
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    formatRupiah.maximumFractionDigits = 0 // Tidak menampilkan desimal
    return formatRupiah.format(amount)
}


@Composable
fun CartScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentUser = loginViewModel.getCurrentUser()

    // State untuk item yang dipilih (untuk kalkulasi pembayaran hanya item terpilih)
    val selectedItems = remember { mutableStateListOf<Int>() } // Simpan productId dari item yang dipilih

    LaunchedEffect(currentUser?.id) {
        cartViewModel.initializeUser(currentUser?.id)
    }

    // Kalkulasi subTotal, biayaLayanan, dan total berdasarkan item yang dipilih
    val (subTotal, biayaLayanan, totalPembayaran) = remember(cartViewModel.cartUiState, selectedItems.toList()) {
        var currentSubTotal = 0.0
        var currentBiayaLayanan = 0.0
        val itemsInCart = (cartViewModel.cartUiState as? CartUIState.Success)?.items ?: emptyList()

        itemsInCart.forEach { item ->
            if (selectedItems.contains(item.productId)) {
                currentSubTotal += (item.harga.toDoubleOrNull() ?: 0.0) * item.quantity
                currentBiayaLayanan += 1000.0 * item.quantity // Biaya layanan per item (bukan per produk unik)
            }
        }
        val currentTotal = currentSubTotal + currentBiayaLayanan
        Triple(currentSubTotal, currentBiayaLayanan, currentTotal)
    }


    if (currentUser == null || !loginViewModel.isLoggedIn()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = "Not Logged In",
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Anda harus login untuk melihat keranjang.", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("signin") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8D21))
            ) {
                Text("Login Sekarang", color = Color.White)
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding() // Menggunakan systemBarsPadding untuk keseluruhan screen
    ) {
        // Header
        TopAppBar(
            modifier = Modifier.fillMaxWidth(), // Tidak perlu navigationBarsPadding lagi karena sudah di Column utama
            title = { Text("Keranjang", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 20.sp) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.Black)
                }
            },
            backgroundColor = Color(0xFFFF8D21), // Warna header
            contentColor = Color.Black
        )

        // Konten Utama
        when (val state = cartViewModel.cartUiState) {
            is CartUIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF8D21))
                }
            }
            is CartUIState.Success -> {
                val cartItems = state.items
                // Jika keranjang berhasil dimuat tapi kosong (setelah filter atau dari awal)
                if (cartItems.isEmpty()) {
                    EmptyCartView(Modifier.weight(1f))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f) // Mengisi ruang yang tersedia
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        item { // Header untuk memilih semua
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val allSelected = cartItems.isNotEmpty() && selectedItems.containsAll(cartItems.map { it.productId })
                                Checkbox(
                                    checked = allSelected,
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            selectedItems.clear()
                                            selectedItems.addAll(cartItems.map { it.productId })
                                        } else {
                                            selectedItems.clear()
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF8D21))
                                )
                                Text("Pilih Semua", modifier = Modifier.padding(start = 8.dp))
                            }
                            Divider()
                        }

                        items(cartItems, key = { it.cartItemId }) { item -> // Gunakan cartItemId sebagai key unik
                            CartItemRow(
                                item = item,
                                isSelected = selectedItems.contains(item.productId),
                                navController = navController,
                                onItemSelected = { productId, isChecked ->
                                    if (isChecked) {
                                        selectedItems.add(productId)
                                    } else {
                                        selectedItems.remove(productId)
                                    }
                                },
                                onQuantityChange = { productId, newQuantity ->
                                    cartViewModel.updateItemQuantity(productId, newQuantity) { success, message ->
                                        if (!success) {
                                            Toast.makeText(context, "Gagal update: $message", Toast.LENGTH_SHORT).show()
                                        }
                                        // Pembaruan UI akan ditangani oleh recomposition dari cartUiState
                                    }
                                },
                                onRemoveItem = { productId ->
                                    cartViewModel.removeItemFromCart(productId) { success, message ->
                                        if (success) {
                                            selectedItems.remove(productId) // Hapus juga dari selected items
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Gagal hapus: $message", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                            Divider() // Pembatas antar item
                        }
                    }
                }
            }
            is CartUIState.Empty -> {
                EmptyCartView(Modifier.weight(1f))
            }
            is CartUIState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().weight(1f).padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${state.message}", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { cartViewModel.fetchCartItems() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8D21))
                    ) {
                        Text("Coba Lagi", color = Color.White)
                    }
                }
            }
            CartUIState.Idle -> {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("Menginisialisasi keranjang...")
                }
            }
        }


        // Informasi Total Pembayaran & Tombol Bayar
        // Hanya tampilkan jika ada item yang dipilih
        if (selectedItems.isNotEmpty() && cartViewModel.cartUiState is CartUIState.Success && (cartViewModel.cartUiState as CartUIState.Success).items.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF9F9F9),
                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Sub Total", color = Color.Gray)
                    Text(formatCurrency(subTotal), color = Color.Gray, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Biaya Layanan", color = Color.Gray)
                    Text(formatCurrency(biayaLayanan), color = Color.Gray, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Pembayaran", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(formatCurrency(totalPembayaran), fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFFFF8D21))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Kumpulkan detail item yang dipilih untuk proses pembayaran
                        val itemsToCheckout = (cartViewModel.cartUiState as? CartUIState.Success)?.items
                            ?.filter { selectedItems.contains(it.productId) }
                            ?: emptyList()

                        if (itemsToCheckout.isNotEmpty()) {
                            // TODO: Navigasi ke layar pembayaran dengan membawa data itemsToCheckout dan totalPembayaran
                            Toast.makeText(context, "Mengarahkan ke pembayaran...", Toast.LENGTH_SHORT).show()
                            Log.d("CartScreen", "Items to checkout: $itemsToCheckout, Total: $totalPembayaran")
                            // navController.navigate("paymentScreen") // Ganti dengan rute yang benar
                        } else {
                            Toast.makeText(context, "Pilih item untuk dibayar.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = selectedItems.isNotEmpty() && totalPembayaran > 0, // Aktif jika ada item dipilih dan total > 0
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8D21), // Warna tombol saat aktif
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFFFD4A3), // Warna saat disabled
                        disabledContentColor = Color.Gray
                    )
                ) {
                    Text("Bayar (${selectedItems.size})")
                }
            }
        }
        Box(modifier = Modifier.navigationBarsPadding()) { // Padding untuk bottom nav agar tidak tertutup system bar
            BottomNavBar(navController = navController)
        }
    }
}

@Composable
fun EmptyCartView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), // Padding di dalam box agar konten tidak terlalu mepet
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F3F3), RoundedCornerShape(16.dp))
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = "Empty Cart",
                modifier = Modifier.size(64.dp),
                tint = Color.Gray // Warna ikon yang lebih lembut
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Keranjangmu masih kosong", fontSize = 18.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Yuk, cari barang impianmu sekarang!",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun CartItemRow(
    item: CartItem,
    navController: NavController,
    isSelected: Boolean,
    onItemSelected: (productId: Int, isSelected: Boolean) -> Unit,
    onQuantityChange: (productId: Int, newQuantity: Int) -> Unit,
    onRemoveItem: (productId: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top // Agar checkbox sejajar dengan bagian atas gambar/teks
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onItemSelected(item.productId, it) },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF8D21)),
            modifier = Modifier.align(Alignment.CenterVertically) // Checkbox di tengah secara vertikal
        )

        Spacer(modifier = Modifier.width(8.dp))
        // Gambar Produk
        Image(
            painter = rememberAsyncImagePainter(
                model = item.imageUrl ?: R.drawable.img, // Placeholder jika URL null
                onError = { Log.e("CartItemRow", "Error loading image: ${item.imageUrl}", it.result.throwable) }
            ),
            contentDescription = item.productName,
            modifier = Modifier
                .size(80.dp)
                .clickable { navController.navigate("product_detail/${item.productId}") }
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray), // Background jika gambar gagal load
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Informasi Produk & Aksi
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.productName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrency(item.harga.toDoubleOrNull() ?: 0.0),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFE91E63) // Warna harga yang menonjol
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Kontrol Kuantitas & Tombol Hapus
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Kontrol Kuantitas
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (item.quantity > 1) { // Minimal kuantitas 1
                                onQuantityChange(item.productId, item.quantity - 1)
                            }
                        },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Kurangi Kuantitas", tint = Color.Gray)
                    }
                    Text(
                        text = "${item.quantity}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = {
                            // Idealnya ada pengecekan stok di sini
                            onQuantityChange(item.productId, item.quantity + 1)

                        },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(Icons.Default.AddCircleOutline, contentDescription = "Tambah Kuantitas", tint = Color(0xFFFF8D21))
                    }
                }

                // Tombol Hapus (Text)
                Text(
                    text = "Hapus",
                    color = Color.Red,
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { onRemoveItem(item.productId) }
                        .padding(start = 8.dp) // Beri sedikit jarak
                )
            }
        }
    }
}