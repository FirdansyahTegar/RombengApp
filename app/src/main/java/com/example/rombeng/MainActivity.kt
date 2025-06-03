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
import com.example.rombeng.viewmodel.ProductViewModel


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
            route = "product_detail/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType // ID produk dari PHP mungkin string, atau sesuaikan
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId != null) {
                // Dapatkan instance ViewModel di sini.
                // viewModel() akan memberikan instance yang sama jika sudah ada dalam scope yang benar
                // atau membuat yang baru.
                val productViewModel: ProductViewModel = viewModel()
                val cartViewModel: CartViewModel = viewModel()
                val loginViewModel: LoginViewModel = viewModel()

                DetailProductScreen(
                    navController = navController,
                    productId = productId,
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    loginViewModel = loginViewModel
                )
            } else {
                // Handle kasus productId null, mungkin navigasi kembali atau tampilkan error
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ID Produk tidak valid.")
                }
                // Navigasi kembali jika ID tidak ada setelah beberapa saat
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000) // Tunda sebentar agar pesan error terlihat
                    navController.popBackStack()
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


