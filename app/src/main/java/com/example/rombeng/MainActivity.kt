package com.example.rombeng

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rombeng.ui.theme.RombengTheme
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rombeng.view.*
import com.example.rombeng.viewmodel.RombengViewModel
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.activity.viewModels
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.compose.ui.platform.LocalContext // Untuk Toast
import android.widget.Toast
import androidx.navigation.navArgument
import com.example.rombeng.viewmodel.LoginViewModel
import com.example.rombeng.viewmodel.CartViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: RombengViewModel by viewModels()
        val lviewModel: LoginViewModel by viewModels()

        setContent {
            AppNavigator(viewModel, lviewModel)
        }
    }

}

@Composable
fun AppNavigator(viewModel: RombengViewModel, lviewModel: LoginViewModel) {
    val navController = rememberNavController()

    // Splash logic
    LaunchedEffect(Unit) {
        delay(1000L)
        if (lviewModel.isLoggedIn()) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
//            return@LaunchedEffect
        } else {
            navController.navigate("landing") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { RombengLoadScreen() }
        composable("landing") { RombengLanding(navController) }
        composable("signup") { RombengRegister(navController) }
        composable("signin") { RombengLogin(
            navController,
            onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("signin") { inclusive = true }
                }
            }
        ) }
        composable("addUser") { AddUserForm(navController) }
        composable("home") { HomeScreen(navController) }
        composable("upload") { UploadItem(navController) }
        composable("cart") { CartScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("paymentList") { PaymentListScreen(navController) }
        composable("addressList"){ AddressListScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }
        composable("reset") { ResetPasswordScreen(navController) }
        composable("resetSucced") { ResetPasswordSuccedScreen(navController) }
        composable("search") { SearchScreen(navController) }

        composable("dataView") {
            DataViewingScreen(
                navController = navController,
                viewModel = viewModel,
                onUserClick = { user ->
                    viewModel.selectUser(user)
                    navController.navigate("detail")
                }
            )
        }

        composable("detail") {
            UserDetailScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = "searchResult/{searchQuery}", // Definisikan argumen searchQuery
            arguments = listOf(navArgument("searchQuery") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("searchQuery")
            SearchResultScreen(navController = navController, initialQuery = query)
        }
        composable("kulkasResult") { KulkasResult(navController) }
        composable(
            route = "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productIdString = backStackEntry.arguments?.getString("productId")
            val cartViewModel: CartViewModel = viewModel()
            val loginViewModel: LoginViewModel = viewModel() // Pastikan LoginViewModel juga ada

            val context = LocalContext.current
            var isLoadingAddToCart by remember { mutableStateOf(false) }

            // 1. Dapatkan currentUser dan productId yang valid
            val currentUser = loginViewModel.getCurrentUser()
            val productId = productIdString?.toIntOrNull()

            // 2. Inisialisasi CartViewModel dengan userId dari currentUser
            //    LaunchedEffect akan menjalankan ini ketika currentUser.id berubah (setelah login)
            //    atau saat composable ini pertama kali ditampilkan dengan user yang valid.
            LaunchedEffect(currentUser?.id) {
                if (currentUser != null && currentUser.id != -1) {
                    cartViewModel.initializeUser(currentUser.id)
                    Log.d("ProductDetail", "CartViewModel initialized with User ID: ${currentUser.id}")
                } else {
                    // Handle kasus jika user tidak login saat masuk ke detail
                    // Ini mungkin tidak akan terjadi jika Anda memproteksi rute ini,
                    // tapi sebagai pengaman.
                    Log.w("ProductDetail", "User not logged in or ID invalid when trying to initialize CartViewModel.")
                    // Anda bisa langsung menonaktifkan tombol atau menampilkan pesan
                }
            }

            if (productId != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Detail untuk Produk ID: $productId")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                // 3. Pastikan currentUser dan userId di CartViewModel sudah ada sebelum menambah ke keranjang
                                val activeUser = loginViewModel.getCurrentUser() // Ambil lagi untuk kepastian
                                val cartVmUserId = cartViewModel.currentUserId // Ambil userId dari CartViewModel

                                Log.d("ProductDetail", "Attempting to add to cart. LoginViewModel User: ${activeUser?.id}, CartViewModel UserID: $cartVmUserId")

                                if (activeUser != null && activeUser.id != -1) {
                                    if (cartVmUserId != null && cartVmUserId == activeUser.id) { // Pastikan CartVM sudah diinisialisasi dengan user yang benar
                                        isLoadingAddToCart = true
                                        cartViewModel.addProductToCart(
                                            productId = productId,
                                            onResult = { success, message ->
                                                isLoadingAddToCart = false
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                if (success) {
                                                    Log.d("ProductDetail", "Produk $productId berhasil ditambahkan ke keranjang.")
                                                    // Opsional: Navigasi ke keranjang atau berikan feedback lain
                                                    // navController.navigate("cart")
                                                } else {
                                                    Log.e("ProductDetail", "Gagal menambahkan produk $productId ke keranjang: $message")
                                                }
                                            }
                                        )
                                    } else {
                                        // Ini bisa terjadi jika LaunchedEffect belum selesai atau ada masalah sinkronisasi
                                        Toast.makeText(context, "CartViewModel belum siap. Coba lagi.", Toast.LENGTH_SHORT).show()
                                        Log.w("ProductDetail", "CartViewModel UserID ($cartVmUserId) mismatch or not set. Expected: ${activeUser.id}. Re-initializing.")
                                        // Coba inisialisasi lagi sebagai fallback, atau tampilkan pesan error yang lebih jelas
                                        cartViewModel.initializeUser(activeUser.id) // Coba paksa inisialisasi lagi
                                    }
                                } else {
                                    Toast.makeText(context, "Anda harus login untuk menambahkan item ke keranjang.", Toast.LENGTH_LONG).show()
                                    // Opsional: Navigasi ke halaman login
                                    // navController.navigate("signin")
                                }
                            },
                            // Nonaktifkan tombol jika user belum login, atau productId tidak valid, atau sedang loading
                            enabled = !isLoadingAddToCart && currentUser != null && currentUser.id != -1 && productId != null
                        ) {
                            if (isLoadingAddToCart) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Masukkan ke keranjang")
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (productIdString == null) "Error: Product ID tidak ditemukan." else "Error: Product ID tidak valid.")
                }
            }
        }



        //END OF NAVHOST
    }
}


@Preview(showBackground = true)
@Composable
fun RombengPreview() {
//        RombengScreen(viewModel)
}


