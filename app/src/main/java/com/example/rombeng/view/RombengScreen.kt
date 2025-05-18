package com.example.rombeng.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Horizontal
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rombeng.model.User
import com.example.rombeng.service.RetrofitClient
import com.example.rombeng.R
import com.example.rombeng.viewmodel.RombengViewModel
import android.graphics.Color as Colour
import androidx.compose.runtime.SideEffect
import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.semantics
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat



@Composable //Loading Screen
fun RombengLoadScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEB7200), // Orange start
                        Color(0xFFFF8F24), // Lighter orange
                        Color.White        // White end
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        LogoRombeng()

    }
}

@Composable //login or register
fun RombengLanding(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    UpdateStatusBarColor(true)
    val context = LocalContext.current
    var dc by remember { mutableStateOf("default") }

//    LaunchedEffect(Unit) {
//        val isOnline = viewModel.hasInternetConnection(context)
//        if (isOnline) {
//            dc = "ada koneksi internet"
//        } else {
//            dc = "Tidak ada koneksi internet"
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.statusBarsPadding()
        ) {
//            Text(text = dc, color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
            LogoRombeng()
            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = { navController.navigate("signin") },
                modifier = Modifier
                    .size(width = 296.dp, height = 67.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD9D9D9),
                    contentColor = Color(0xFF822900)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.jockeyone)),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("signup") },
                modifier = Modifier
                    .size(width = 296.dp, height = 67.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF8D21),
                    contentColor = Color(0xFF822900)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.jockeyone)),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }


            Spacer(modifier = Modifier.height(60.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .height(523.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .background(Color(0xAAFFFFFF)) // Latar belakang semi-transparan
//                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Give Your Preloved Items a New Home",
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF822900),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Sell easily, declutter effortlessly, and let your items find a new life with Rombeng.",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF822900),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.bg_trash),
                        contentDescription = "Main Image",
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RombengLogin(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.resetTextField()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(44.dp))

            // Logo dan Judul
            LogoRombeng()

            Spacer(modifier = Modifier.height(40.dp))

            // Welcome Text
            Text(
                text = "Welcome to Rombeng",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Sign in below to manage your needs",
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Input Fields
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .navigationBarsPadding()

            ) {
                Spacer(modifier = Modifier.height(40.dp))
                TextFieldBuilder(
                    value = viewModel.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                PassTextFieldBuilder(
                    value = viewModel.password,
                    onValueChange = { viewModel.onPassChange(it) },
                    placeholder = "Enter your password",
                    leadingIcon = Icons.Default.Lock,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "or continue with your Google Account",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF6A6161),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sign In Button
                Button(
                    onClick = { navController.navigate("home") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .width(296.dp)
                        .height(67.dp),
                    shape = RoundedCornerShape(20.dp)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Posisi vertikal rata tengah
                        horizontalArrangement = Arrangement.Center // Pusatkan elemen dalam Row
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo), // Ganti dengan ID gambar Google
                            contentDescription = "Google Logo",
                            modifier = Modifier
                                .size(24.dp) // Ukuran logo
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Spasi antara logo dan teks
                        Text(
                            "Login with Google",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { navController.navigate("home") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8D21)
                    ),
                    modifier = Modifier
                        .width(296.dp)
                        .height(67.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Sign In", fontSize = 24.sp, color = Color(0xFF822900))
                }
                Spacer(modifier = Modifier.height(30.dp))

                // Forgot Password
                Text(
                    text = "Lupa Password ?",
                    fontSize = 14.sp,
                    color = Color(0xFF6A6161),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("forgot")
                        }
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.weight(1f))
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RombengRegister(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.resetTextField()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(44.dp))

            // Logo dan Judul
            LogoRombeng()

            Spacer(modifier = Modifier.height(40.dp))

            // Welcome Text
            Text(
                text = "Welcome to Rombeng",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Sign up below",
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Input Fields
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(top = 25.dp)

            ) {

                TextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    placeholder = { Text("Your name") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White, // Warna latar belakang
                        cursorColor = Color.Black, // Warna kursor
                        focusedIndicatorColor = Color.Transparent, // Garis bawah saat fokus
                        unfocusedIndicatorColor = Color.Transparent // Garis bawah saat tidak fokus
                    ),
                    leadingIcon = { // Ikon di sebelah kiri teks
                        Icon(
                            imageVector = Icons.Default.Person, // Ganti dengan ikon yang sesuai
                            contentDescription = "Person Icon",
                            tint = Color.Gray // Warna ikon
                        )
                    },
                    modifier = Modifier
                        .width(296.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                )
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    placeholder = { Text("Enter your email") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White, // Warna latar belakang
                        cursorColor = Color.Black, // Warna kursor
                        focusedIndicatorColor = Color.Transparent, // Garis bawah saat fokus
                        unfocusedIndicatorColor = Color.Transparent // Garis bawah saat tidak fokus
                    ),
                    leadingIcon = { // Ikon di sebelah kiri teks
                        Icon(
                            imageVector = Icons.Default.Email, // Ganti dengan ikon yang sesuai
                            contentDescription = "Email Icon",
                            tint = Color.Gray // Warna ikon
                        )
                    },
                    modifier = Modifier
                        .width(296.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.onPassChange(it) },
                    placeholder = { Text("Enter your password") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White, // Warna latar belakang
                        cursorColor = Color.Black, // Warna kursor
                        focusedIndicatorColor = Color.Transparent, // Garis bawah saat fokus
                        unfocusedIndicatorColor = Color.Transparent // Garis bawah saat tidak fokus
                    ),
                    leadingIcon = { // Ikon di sebelah kiri teks
                        Icon(
                            imageVector = Icons.Default.Lock, // Ganti dengan ikon yang sesuai
                            contentDescription = "Email Icon",
                            tint = Color.Gray // Warna ikon
                        )
                    },

                    modifier = Modifier
                        .width(296.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = viewModel.confPass,
                    onValueChange = { viewModel.onConfPassChange(it) },
                    placeholder = { Text("Confirm password") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White, // Warna latar belakang
                        cursorColor = Color.Black, // Warna kursor
                        focusedIndicatorColor = Color.Transparent, // Garis bawah saat fokus
                        unfocusedIndicatorColor = Color.Transparent // Garis bawah saat tidak fokus
                    ),
                    leadingIcon = { // Ikon di sebelah kiri teks
                        Icon(
                            imageVector = Icons.Default.Lock, // Ganti dengan ikon yang sesuai
                            contentDescription = "Email Icon",
                            tint = Color.Gray // Warna ikon
                        )
                    },

                    modifier = Modifier
                        .width(296.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Sign In Button
                Button(
                    onClick = {navController.navigate("home")},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8D21)
                    ),
                    modifier = Modifier
                        .width(256.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Sign Up", fontSize = 20.sp, color = Color(0xFF822900))
                }

                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Sudah Memiliki Akun ?",
                    fontSize = 14.sp,
                    color = Color(0xFF6A6161),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("signin")
                        }
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.weight(1f))

            }

        }
    }
}

@Composable
fun HomeScreen(navController: NavController, viewModel: RombengViewModel = viewModel()) {
        LaunchedEffect(Unit) {
            viewModel.resetTextField()
        }
        
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .background(Color.White)
                .fillMaxSize()

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            )   {

                // App Title & Logo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 10.dp)
                ) {
                    LogoRombeng()
                }
            }

            // Search Bar
            Box(
                modifier = Modifier

                    .height(200.dp)
                    .fillMaxWidth()

            ) {

                Image(
                    painter = painterResource(id = R.drawable.bg_home),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )

                Column(modifier = Modifier.clickable { navController.navigate("search") }) {
                    Spacer(Modifier.height(12.dp))
                    TextFieldBuilder(
                        enabled = false,
                        value = "",
                        onValueChange = { },
                        placeholder = "Mau Cari Apa?",
                        leadingIcon = Icons.Default.Search,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .clip(shape = RoundedCornerShape(10.dp))

                    )
                }
            }
            val categories = listOf(
                "Elektronik", "Furnitur", "Material Bangunan", "Kendaraan", "Lihat Semua"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                categories.forEachIndexed { index, category ->

                    val imgId = when (index) {
                        0 -> R.drawable.elektronik
                        1 -> R.drawable.furnitur
                        2 -> R.drawable.material_bangunan
                        3 -> R.drawable.kendaraan
                        4 -> R.drawable.lihat_semua
                        else -> R.drawable.placeholder // fallback image
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(60.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFF8000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = imgId),
                                contentDescription = "Icon",
                                modifier = Modifier.size(36.dp) // Ukuran bisa disesuaikan
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = category,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Serif, // Bisa diganti Font(R.font.crimson_text) kalau punya
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.height(27.dp)
                        )
                    }
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            ) {
                Text(
                    text = "Rekomendasi Barang Bekas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val items = listOf(
                        Triple("Rombeng Kulkas Sanyo", "Rp300.000", "Singosari"),
                        Triple("Rombeng Elektronik Rusak", "Rp350.000", "Merjosari")
                    )

                    items.forEach { (title, price, location) ->
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .height(160.dp)
                                .border(1.dp, Color(0xFFFF8000), shape = RoundedCornerShape(20.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img),
                                    contentDescription = title,
                                    modifier = Modifier
                                        .height(100.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                Text(
                                    text = price,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily.Serif,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                Text(
                                    text = location,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = Color(0xFF6A6161),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            // Bottom Navigation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .align(Alignment.CenterHorizontally)
                    .navigationBarsPadding()
            ) {
                        BottomNavBar()
            }
        }
}

@Composable
fun SearchScreen(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
//                    .width(30.dp)
                    .padding(start = 12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Back Button",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .clickable{ navController.navigate("home") }
                        .height(30.dp)
                )
            }
            // Search bar
            TextFieldBuilder(
                value = viewModel.search,
                onValueChange = { viewModel.onSearchChange(it) },
                placeholder = "Mau Cari Apa?",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(25.dp),
                        ambientColor = Color.Gray,
                        spotColor = Color.Gray
                    )
//                    .border(width = 2.dp, color = Color(0xFFFF8000), shape = RoundedCornerShape(25.dp))
                    .clip(shape = RoundedCornerShape(25.dp))

            )
        }

//         Kategori buttons
        val categories = listOf(
            listOf("kardus", "elektronik", "kulkas"),
            listOf("mainan", "sparepart", "tv")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            categories.forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { category ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            CategoryItem(category, modifier = Modifier
                                .height(40.dp).fillMaxWidth()
                                .clickable { navController.navigate("kulkasResult") }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun KulkasResult(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
//                    .width(30.dp)
                    .padding(start = 12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Back Button",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .clickable{ navController.navigate("home") }
                        .height(30.dp)
                )
            }
            // Search bar
            TextFieldBuilder(
                value = viewModel.search,
                onValueChange = { viewModel.onSearchChange(it) },
                placeholder = "Kulkas",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(25.dp),
                        ambientColor = Color.Gray,
                        spotColor = Color.Gray
                    )
//                    .border(width = 2.dp, color = Color(0xFFFF8000), shape = RoundedCornerShape(25.dp))
                    .clip(shape = RoundedCornerShape(25.dp))

            )
        }

        data class ItemData(val judul: String, val harga: String, val lokasi: String)
        val items = listOf(
            ItemData("Kulkas LL", "Rp300.000", "Sumbersari"),
            ItemData("Kulkas LG", "Rp250.000", "Kepanjen"),
            ItemData("Kulkas GL", "Rp400.000", "Lowokwaru"),
            ItemData("Kulkas GL", "Rp400.000", "Lowokwaru"),
            ItemData("Kulkas GL", "Rp400.000", "Lowokwaru"),
            ItemData("Kulkas GG", "Rp2.000.000", "Blimbing")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowItems.forEach { item ->
                        CardBuilder(
                            judul = item.judul,
                            harga = item.harga,
                            lokasi = item.lokasi,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { navController.navigate("search") }
                        )
                    }
                    // Jika hanya ada satu item di baris ini, tambahkan spacer agar tetap rata
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.width(150.dp))
                    }
                }
            }
        }


    }
}

@Composable
fun ForgotPasswordScreen(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.resetTextField()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 44.dp) // Untuk status bar
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(55.dp))
            Text(
                text = "Lupa Password",
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.jockeyone)),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Masukkan akun email Anda untuk mengatur ulang kata sandi",
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .fillMaxWidth()

        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Email Anda",
                fontSize = 19.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start).padding(start = 52.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Email Input Box
            Box(
                modifier = Modifier
                    .width(309.dp)
                    .height(51.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {

                TextFieldBuilder(
                    value = viewModel.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    placeholder = "Masukkan alamat email",
                    leadingIcon = Icons.Default.Email,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

            }

            Spacer(modifier = Modifier.height(40.dp))

            // Reset Password Button
            Button(
                onClick = { navController.navigate("reset") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF8D21)
                    ),
                enabled = true,
                modifier = Modifier
                    .width(309.dp)
                    .height(51.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Reset Password", fontSize = 19.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))
        }

    }
}

@Composable
fun ResetPasswordScreen(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.resetTextField()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 44.dp) // Untuk status bar
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(55.dp))
            Text(
                text = "Reset Password",
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.jockeyone)),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Buat kata sandi baru. Pastikan kata sandi tersebut berbeda dari kata sandi sebelumnya untuk keamanan.",
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .fillMaxWidth()

        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Email Anda",
                fontSize = 19.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start).padding(start = 52.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .width(309.dp)
                    .height(51.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {
                PassTextFieldBuilder(
                    value = viewModel.password,
                    onValueChange = { viewModel.onPassChange(it) },
                    placeholder = "Masukkan Password Baru",
                    leadingIcon = Icons.Default.Lock,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .width(309.dp)
                    .height(51.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {
                PassTextFieldBuilder(
                    value = viewModel.confPass,
                    onValueChange = { viewModel.onConfPassChange(it) },
                    placeholder = "Masukkan Password Baru",
                    leadingIcon = Icons.Default.Lock,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

            }

            Spacer(modifier = Modifier.height(40.dp))

            // Reset Password Button
            Button(
                onClick = { navController.navigate("resetSucced") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF8D21)
                ),
                enabled = true,
                modifier = Modifier
                    .width(309.dp)
                    .height(51.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Reset Password", fontSize = 19.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))
        }

    }
}

@Composable
fun ResetPasswordSuccedScreen(navController: NavController, viewModel: RombengViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.resetTextField()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()

    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.big_green_check),
            contentDescription = "Circle Green Checkmark",
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Berhasil",
            fontSize = 40.sp,
            fontFamily = FontFamily(Font(R.font.jockeyone)),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Kata sandi Anda telah diubah. Klik lanjutkan untuk masuk.",
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { navController.navigate("signin") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF8D21)
            ),
            enabled = true,
            modifier = Modifier
                .width(309.dp)
                .height(51.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Lanjutkan", fontSize = 19.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun LogoRombeng() {
    Row(
//        modifier = Modifier.padding(top = 60.dp, start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Logo",
            modifier = Modifier.size(58.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text("Rombeng", fontSize = 36.sp, fontFamily = FontFamily.Cursive, color = Color(0xFF822900), fontWeight = FontWeight.W900)
            Text("Say Yes To Second Chance", fontSize = 10.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF581C00))
        }
    }
}

@Composable
fun DataViewingScreen(
    navController: NavController,
    viewModel: RombengViewModel,
    onUserClick: (User) -> Unit
) {
    UpdateStatusBarColor(isLightBackground = true)

    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Loading...")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            items(users) { user ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onUserClick(user)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Icon",
                            modifier = Modifier
                                .size(48.dp)
                                .padding(end = 12.dp),
                            tint = Color.Gray
                        )

                        Column {
                            Text(text = user.id)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = user.username, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserDetailScreen(navController: NavController, viewModel: RombengViewModel) {
    val user by viewModel.selectedUser.collectAsState()

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No user selected")
        }
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(color = Color.LightGray)
        ) {
            Column (
                Modifier.padding(start = 12.dp)
            ){
                Spacer(modifier = Modifier.height(80.dp))
                Text("Name: ${user!!.username}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
//                Text("bio: ${user!!.bio}")
//                Spacer(modifier = Modifier.height(8.dp))
                Text("password: ${user!!.private_key}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("account created at: ${user!!.created_at}")
//                Text("Address:")
//                Text("${user!!.address.street}, ${user!!.address.suite}")
//                Text("${user!!.address.city} - ${user!!.address.zipcode}")
            }
        }
    }
}


@Composable
fun UpdateStatusBarColor(isLightBackground: Boolean) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = if (isLightBackground) Colour.WHITE else Colour.BLACK //warna bg putih/hitam
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightBackground //warna ikon light/dark
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldBuilder(
    enabled: Boolean? = true,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White, // Warna latar belakang
        cursorColor = Color.Black, // Warna kursor
        focusedIndicatorColor = Color.LightGray, // Garis bawah saat fokus
        unfocusedIndicatorColor = Color.Transparent // Garis bawah saat tidak fokus
    ),
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    TextField(
        enabled = enabled ?: true,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        colors = colors,
        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = it, contentDescription = null) }
        },
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassTextFieldBuilder(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White, // Warna latar belakang
        cursorColor = Color.Black, // Warna kursor
        focusedIndicatorColor = Color.LightGray, // Garis bawah saat fokus
        unfocusedIndicatorColor = Color.Transparent // Garis bawah saat tidak fokus
    ),
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        colors = colors,
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    )
}

@Composable
fun BottomNavBar() {
    var selectedIndex by remember { mutableStateOf(0) }
    val icons = listOf(
        Icons.Default.Home to "Home",
        Icons.Default.Add to "Add",
        Icons.Default.ShoppingCart to "ShoppingCart",
        Icons.Default.Person to "User"
    )

    BottomNavigation(
        windowInsets = BottomNavigationDefaults.windowInsets,
        backgroundColor = Color(0xFFFF7B34)
    ) {
        icons.forEachIndexed { index, (icon, description) ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = description,
                        tint = if (selectedIndex == index) Color.White else Color.Black
                    )
                },
                selected = selectedIndex == index,
                onClick = { selectedIndex = index }
            )
        }
    }
}

@Composable
fun CardBuilder(judul: String, harga: String, lokasi: String, modifier: Modifier = Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, Color(0xFFFF8000)),
//        onClick = action,
        modifier = modifier
            .width(150.dp)
            .height(220.dp)
//            .padding(12.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Card Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = judul,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = harga,
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = lokasi,
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp)
        )

    }
}

@Composable
fun CategoryItem(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color(0xFFFF8000), RoundedCornerShape(15.dp))
            .background(Color.White, RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            color = Color.Black
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SimpleSearchBar(
//    textFieldState: TextFieldState,
//    onSearch: (String) -> Unit,
//    searchResults: List<String>,
//    modifier: Modifier = Modifier
//) {
//    // Controls expansion state of the search bar
//    var expanded by rememberSaveable { mutableStateOf(false) }
//
//    Box(
//        modifier
//            .fillMaxSize()
//            .semantics { isTraversalGroup = true }
//    ) {
//        SearchBar(
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .semantics { traversalIndex = 0f },
//            inputField = {
//                SearchBarDefaults.InputField(
//                    query = textFieldState.text.toString(),
//                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
//                    onSearch = {
//                        onSearch(textFieldState.text.toString())
//                        expanded = false
//                    },
//                    expanded = expanded,
//                    onExpandedChange = { expanded = it },
//                    placeholder = { Text("Search") }
//                )
//            },
//            expanded = expanded,
//            onExpandedChange = { expanded = it },
//        ) {
//            // Display search results in a scrollable column
//            Column(Modifier.verticalScroll(rememberScrollState())) {
//                searchResults.forEach { result ->
//                    ListItem(
//                        headlineContent = { Text(result) },
//                        modifier = Modifier
//                            .clickable {
//                                textFieldState.edit { replace(0, length, result) }
//                                expanded = false
//                            }
//                            .fillMaxWidth()
//                    )
//                }
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun RombengPreview() {
//    RombengLanding()
}