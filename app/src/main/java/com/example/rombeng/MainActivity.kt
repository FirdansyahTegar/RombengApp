package com.example.rombeng

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import kotlinx.coroutines.delay



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigator()
//            RombengPreview() //Preview di Android Studio
        }
    }
}

@Composable
fun AppNavigator() {
    var showRegisterScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L) // Tunggu selama 2 detik
        showRegisterScreen = true
    }

    if (showRegisterScreen) {
        HomeScreen()
    } else {
        RombengScreen()
    }
}

@Composable //Loading Screen
fun RombengScreen() {
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

@Composable
fun RombengLanding() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            LogoRombeng()
            Spacer(modifier = Modifier.height(50.dp))
            Box(
                modifier = Modifier
                    .size(width = 296.dp, height = 67.dp)
                    .background(Color(0xFFD9D9D9), shape = RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.jockeyone)),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF822900),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .size(width = 296.dp, height = 67.dp)
                    .background(Color(0xFFFF8D21), shape = RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.jockeyone)),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF822900),
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
fun RombengLogin() {
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

            Spacer(modifier = Modifier.height(50.dp))

            // Input Fields
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(top = 50.dp)

            ) {
                TextField(
                    value = "",
                    onValueChange = {},
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
                    value = "",
                    onValueChange = {},
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

                // Login with Google
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.google_logo),
//                        contentDescription = "Google Logo",
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Login with Google", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//                }
//                Spacer(modifier = Modifier.height(20.dp))

                // Sign In Button
                Button(
                    onClick = {},
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
                    onClick = {},
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
                    text = "Forgot Password ?",
                    fontSize = 14.sp,
                    color = Color(0xFF6A6161),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.weight(1f))
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RombengRegister() {
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
                    value = "",
                    onValueChange = {},
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
                    value = "",
                    onValueChange = {},
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
                    value = "",
                    onValueChange = {},
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
                    value = "",
                    onValueChange = {},
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
                    onClick = {},
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
                Spacer(modifier = Modifier.weight(1f))

            }

        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(248.dp)
                .background(Color(0xFFD9D9D9))
                .align(Alignment.TopCenter)
        )

        // App Title & Logo
        Box(
            modifier = Modifier.padding(start = 16.dp, top = 40.dp)
        ) {
            LogoRombeng()
        }

        // Search Bar
        Row(
            modifier = Modifier
                .padding(top = 141.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(60.dp)
//                .clip(RoundedCornerShape(20.dp))
                .background(Color.White),
//                .shadow(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search Icon",
//                modifier = Modifier.padding(start = 16.dp)
//            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Text("Search", fontSize = 18.sp, color = Color.Black)
//            Spacer(modifier = Modifier.weight(1f))
            TextFieldBuilder(
                value = "",
                onValueChange = {  },
                placeholder = "Search",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.padding(start = 16.dp)
            )
            Box(
                modifier = Modifier
                    .size(60.dp)
//                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFF8000)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }

        // Bottom Navigation

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFFFF7B34))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavBar()
                Spacer(modifier = Modifier.navigationBarsPadding())
//                Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(40.dp), tint = Color.White)
//                Icon(Icons.Default.Add, contentDescription = "Favorites", modifier = Modifier.size(40.dp))
//                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", modifier = Modifier.size(40.dp))
//                Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(40.dp))
            }
        }

        // Placeholder Boxes (Content)
        Row(
            modifier = Modifier.padding(top = 513.dp, start = 45.dp, end = 45.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(145.dp, 106.dp)
                    .background(Color(0xFFD9D9D9))
            )
            Box(
                modifier = Modifier
                    .size(146.dp, 106.dp)
                    .background(Color(0xFFD9D9D9))
            )
        }
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
            Text("Rombeng", fontSize = 24.sp, fontFamily = FontFamily.Cursive, color = Color(0xFF822900), fontWeight = FontWeight.ExtraBold)
            Text("Say Yes To Second Chance", fontSize = 10.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF581C00))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldBuilder(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White, // Warna latar belakang
        cursorColor = Color.Black, // Warna kursor
        focusedIndicatorColor = Color.Transparent, // Garis bawah saat fokus
        unfocusedIndicatorColor = Color.Transparent // Garis bawah saat tidak fokus
    ),
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier
    ) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        colors = colors,
        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = it, contentDescription = null, tint = Color.Gray) }
        },
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    )
}

@Composable
fun BottomNavBar() {
    var iconColorH by remember { mutableStateOf(Color.Black) }
    var iconColorA by remember { mutableStateOf(Color.Black) }
    var iconColorS by remember { mutableStateOf(Color.Black) }
    var iconColorU by remember { mutableStateOf(Color.Black) }

    BottomNavigation(
        windowInsets = BottomNavigationDefaults.windowInsets,
        backgroundColor = Color(0xFFFF7B34)
        )
    {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = iconColorH) },
            selected = true,
            onClick = {
                iconColorH = Color.White
                iconColorA = Color.Black
                iconColorS = Color.Black
                iconColorU = Color.Black
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Add", tint = iconColorA) },
            selected = true,
            onClick = {
                iconColorH = Color.Black
                iconColorA = Color.White
                iconColorS = Color.Black
                iconColorU = Color.Black
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "ShoppingCart", tint = iconColorS) },
            selected = false,
            onClick = {
                iconColorH = Color.Black
                iconColorA = Color.Black
                iconColorS = Color.White
                iconColorU = Color.Black
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "User", tint = iconColorU) },
            selected = true,
            onClick = {
                iconColorH = Color.Black
                iconColorA = Color.Black
                iconColorS = Color.Black
                iconColorU = Color.White
            }
        )

    }
}


@Preview(showBackground = true)
@Composable
fun RombengPreview() {
        RombengLanding()
}


