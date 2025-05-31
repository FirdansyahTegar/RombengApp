package com.example.rombeng.view

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.collection.isNotEmpty // Kemungkinan tidak terpakai, periksa penggunaan
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable // Kemungkinan tidak terpakai, periksa penggunaan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState // Kemungkinan tidak terpakai, periksa penggunaan
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation // Material 2, pertimbangkan migrasi ke Material 3 NavigationBar
import androidx.compose.material.BottomNavigationDefaults // Material 2
import androidx.compose.material.BottomNavigationItem // Material 2
import androidx.compose.material.DropdownMenu // Material 2, pertimbangkan Material 3 DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Pertimbangkan untuk mengimpor ikon secara spesifik jika tidak semua dipakai
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy // Kemungkinan tidak terpakai, periksa penggunaan
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.error // Kemungkinan tidak terpakai, periksa penggunaan
import androidx.compose.ui.semantics.semantics // Kemungkinan tidak terpakai, periksa penggunaan
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat // Kemungkinan tidak terpakai, periksa penggunaan
import android.graphics.Color as AndroidColor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.rombeng.R
import com.example.rombeng.viewmodel.RombengViewModel
import com.example.rombeng.viewmodel.LoginViewModel
import android.graphics.Color as Colour
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowInsetsCompat
//import androidx.preference.forEach
//import androidx.privacysandbox.tools.core.generator.build
//import androidx.wear.compose.material.placeholder
import coil.compose.AsyncImage
import com.example.rombeng.model.AddUserResponse
import com.example.rombeng.model.User
import com.example.rombeng.model.Product
import com.example.rombeng.service.RetrofitClient
import com.example.rombeng.viewmodel.LoginUIState
import com.example.rombeng.viewmodel.UpImgViewModel
import com.example.rombeng.viewmodel.UploadResult
import com.example.rombeng.viewmodel.ProductUIState
import com.example.rombeng.viewmodel.ProductViewModel
import com.example.rombeng.viewmodel.SearchUIState
import com.example.rombeng.viewmodel.SearchViewModel
import com.google.android.gms.common.api.ApiException // Kemungkinan tidak terpakai, periksa penggunaan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder
import java.net.URLEncoder // Untuk encoding query URL
import java.nio.charset.StandardCharsets
import kotlin.text.isNotEmpty
import kotlin.text.trim
import androidx.compose.runtime.saveable.Saver

var isErrorName by mutableStateOf(false)
var isErrorEmail by mutableStateOf(false)
var isErrorPass by mutableStateOf(false)
var isErrorConfPass by mutableStateOf(false)


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
fun RombengLogin(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    val view = LocalView.current
    LaunchedEffect(Unit) {
        val window = (view.context as Activity).window
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_VISIBLE
                )
    }
    val context = LocalContext.current

    // Mengamati LiveData dari ViewModel untuk hasil login dan status loading
    val loginState by loginViewModel.loginResult.observeAsState(initial = LoginUIState.Idle)
    val isLoading by loginViewModel.isLoading.observeAsState(initial = false)
    val emailValue = loginViewModel.email // Asumsi email adalah MutableState<String> di ViewModel
    val passwordValue = loginViewModel.password // Asumsi password adalah MutableState<String> di ViewModel

    // Efek untuk menangani perubahan state login (menampilkan Toast, navigasi)
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginUIState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                onLoginSuccess() // Panggil callback untuk navigasi
                // loginViewModel.resetLoginState() // Opsional: reset state di ViewModel agar tidak trigger lagi
            }
            is LoginUIState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            LoginUIState.LoggedOut -> {
                // Bisa ditambahkan logika jika diperlukan saat logout dari layar ini
            }
            LoginUIState.Idle -> {
                // State awal atau netral
            }
            null -> {
                // state awal sebelum ada observasi
            }
        }
    }

    // Opsional: Jika Anda memiliki fungsi untuk mereset field di ViewModel saat layar pertama kali muncul
    // LaunchedEffect(Unit) {
    //     loginViewModel.resetTextFields() // Anda perlu membuat fungsi ini di ViewModel
    // }

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

            LogoRombeng() // Asumsi Composable ini sudah ada

            Spacer(modifier = Modifier.height(40.dp))

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .navigationBarsPadding() // Menambahkan padding untuk navigation bar sistem
                    .padding(bottom = 16.dp) // Padding bawah agar konten tidak terlalu mepet
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                TextFieldBuilder( // Asumsi Composable ini sudah ada
                    value = emailValue,
                    onValueChange = { loginViewModel.onEmailChange(it) }, // Panggil fungsi ViewModel
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                PassTextFieldBuilder( // Asumsi Composable ini sudah ada
                    value = passwordValue,
                    onValueChange = { loginViewModel.onPassChange(it) }, // Panggil fungsi ViewModel
                    placeholder = "Enter your password",
                    leadingIcon = Icons.Default.Lock,
                    modifier = Modifier.padding(horizontal = 16.dp)
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

                // Tombol Login dengan Google (sementara navigasi ke "home")
                Button(
                    onClick = {
                        val activity = context as? Activity
                        if (activity != null) {
                            loginViewModel.signInWithGoogle(activity)
                        } else {
                            // Handle kasus di mana context bukan Activity
                            // (jarang terjadi di Composable layar penuh)
                            Toast.makeText(
                                context,
                                "Tidak dapat memulai Google Sign-In",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .width(296.dp)
                        .height(67.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo), // Pastikan resource ada
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Login with Google",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Tombol Sign In utama
                Button(
                    onClick = {
                        // Panggil fungsi login dari ViewModel dengan nilai email dan password saat ini
                        loginViewModel.login(emailValue, passwordValue)
                    },
                    enabled = !isLoading, // Nonaktifkan tombol saat proses login sedang berjalan
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8D21),
                        disabledContainerColor = Color(0xFFFF8D21).copy(alpha = 0.5f) // Warna saat disabled
                    ),
                    modifier = Modifier
                        .width(296.dp)
                        .height(67.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF822900) // Atau warna yang kontras dengan background tombol
                        )
                    } else {
                        Text("Sign In", fontSize = 24.sp, color = Color(0xFF822900))
                    }
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
                            // Pastikan "forgot" adalah route yang valid di NavController Anda
                            navController.navigate("forgot")
                        }
                        .fillMaxWidth(),
                )
                // Spacer untuk mendorong konten ke atas jika Column lebih tinggi dari kontennya
                // Jika Anda ingin konten tetap di tengah secara vertikal dalam Column abu-abu,
                // Anda mungkin perlu menyesuaikan atau menghapus Spacer ini dan mengatur
                // verticalArrangement pada Column abu-abu.
                // Untuk saat ini, saya biarkan agar mirip dengan struktur sebelumnya.
                Spacer(modifier = Modifier.weight(1f)) // Mendorong elemen "Lupa Password?" ke bawah jika ada ruang
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RombengRegister(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    viewModel: RombengViewModel = viewModel())
{

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

            Spacer(modifier = Modifier.height(25.dp))

            // Input Fields
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(top = 25.dp)

            ) {

                TextFieldBuilder(
                    value = viewModel.name,
                    onValueChange = {
                        viewModel.onNameChange(it)
                        isErrorName = it.length < 6 && it.isNotEmpty()
                        },
                    isError = isErrorName,
                    errorMessage = "Username harus lebih dari 6 karakter",
                    placeholder = "Enter your name",
                    leadingIcon = Icons.Default.Person,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextFieldBuilder(
                    value = viewModel.email,
                    onValueChange = {
                        viewModel.onEmailChange(it)
                        isErrorEmail = !viewModel.validateEmail(it) && it.isNotEmpty()
                    },
                    isError = isErrorEmail,
                    errorMessage = "Email tidak valid",
                    keyType = KeyboardType.Email,
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))
                PassTextFieldBuilder(
                    value = viewModel.password,
                    onValueChange = {
                        viewModel.onPassChange(it)
                        isErrorPass = it.length < 8 && it.isNotEmpty()
                    },
                    isError = isErrorPass,
                    errorMessage = "Password harus lebih dari 8 karakter",
                    placeholder = "Masukkan Password",
                    leadingIcon = Icons.Default.Lock,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))
                PassTextFieldBuilder(
                    value = viewModel.confPass,
                    onValueChange = {
                        viewModel.onConfPassChange(it)
                        isErrorConfPass = it != viewModel.password && it.isNotEmpty()
                                    },
                    isError = isErrorConfPass,
                    errorMessage = "Password tidak sama",
                    placeholder = "Confirm Password",
                    leadingIcon = Icons.Default.Lock,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sign In Button
                Button(
                    enabled = viewModel.regButton,
                    onClick = {viewModel.registerUser()},
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
                Spacer(modifier = Modifier.height(20.dp))


                if (viewModel.error or viewModel.succes) {
                    Text(
                        text = viewModel.message,
                        fontSize = 16.sp,
                        color = if (viewModel.succes) {Color.Green} else {Color.Red},
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }


                if (viewModel.succes) {
                    LaunchedEffect(Unit) {
                        delay(1000)
                        navController.navigate("signin")
                    }
                }

                val context = LocalContext.current
                // SERVER_CLIENT_ID Anda, idealnya diambil dari strings.xml
                val serverClientId = context.getString(R.string.your_web_client_id)

                // Mengamati state dari ViewModel
                val isLoading = viewModel.Loading
                val message = viewModel.message
                val isError = viewModel.error
                val isSuccess = viewModel.succes
                val isRegButtonEnabled = viewModel.regButton

                // Mengamati IntentSenderRequest untuk Google Sign-In
                val googleSignInIntentSender by viewModel.googleSignInIntentSender.collectAsState()

                // Launcher untuk hasil Google Sign-In
                val googleSignInLauncher =
                    rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        try {
                            viewModel.handleGoogleSignUpResult(result.data)
                        } catch (e: ApiException) {
                            Log.e("RegisterScreen", "Google Sign-In failed after activity result: ${e.localizedMessage}")
                            // ViewModel sudah menangani ini, tapi bisa ditambahkan logging tambahan jika perlu
                        }
                    } else {
                        // Pengguna mungkin membatalkan atau ada error lain
                        Log.w("RegisterScreen", "Google Sign-In canceled or failed. Result code: ${result.resultCode}")
                        // ViewModel akan menangani CommonStatusCodes.CANCELED jika itu kasusnya
                        // Jika tidak, pastikan ViewModel mereset state loading/button jika perlu
                        if (result.resultCode != Activity.RESULT_CANCELED) {
                            viewModel.message = "Proses Google Sign-In tidak berhasil."
                            viewModel.error = true // Set error jika bukan pembatalan eksplisit
                        }
                        viewModel.Loading = false
                        viewModel.regButton = true
                        viewModel.consumeGoogleSignInIntent() // Pastikan intent dikonsumsi
                    }
                }

                // LaunchedEffect untuk meluncurkan dialog Google Sign-In ketika intent sender tersedia
                LaunchedEffect(googleSignInIntentSender) {
                    googleSignInIntentSender?.let { intentSenderRequest ->
                        googleSignInLauncher.launch(intentSenderRequest)
                        // Penting: Konsumsi intent setelah diluncurkan agar tidak ter-trigger lagi
                        // ViewModel Anda mungkin sudah melakukan ini, atau Anda bisa melakukannya di sini
                        // rombengViewModel.consumeGoogleSignInIntent() // Panggil jika ViewModel tidak otomatis meresetnya
                    }
                }

                Button(
                    onClick = {
                        // Panggil fungsi beginGoogleSignUp dari ViewModel
                        // Pastikan activity yang valid diteruskan.
                        // Dalam Composable, LocalContext.current biasanya adalah Activity.
                        val activity = context as? Activity
                        if (activity != null) {
                            if (serverClientId == "MASUKKAN_SERVER_CLIENT_ID_ANDA_DISINI") {
                                viewModel.message = "Client ID Google belum dikonfigurasi."
                                viewModel.error = true
                            } else {
                                viewModel.beginGoogleSignUp(activity, serverClientId)
                            }
                        } else {
                            Log.e("RegisterScreen", "Context bukan Activity, tidak bisa memulai Google Sign-Up")
                            viewModel.message = "Tidak bisa memulai Google Sign-Up saat ini."
                            viewModel.error = true
                        }
                    },
                    enabled = isRegButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .width(256.dp)
                        .height(67.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo), // Pastikan resource ada
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Login with Google",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    if (isLoading) {
                        CircularProgressIndicator()
                    }

                    if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            color = if (isError) MaterialTheme.colorScheme.error else if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Contoh navigasi setelah sukses (jika diperlukan)
                    if (isSuccess) {
                        LaunchedEffect(Unit) {
                            // Lakukan navigasi atau tindakan lain setelah registrasi sukses
                            // Misalnya: navController.navigate("login_screen") { popUpTo("register_screen") { inclusive = true } }
                            Log.i("RegisterScreen", "Registrasi sukses, pesan: $message")
                            navController.navigate("signin") { // Ganti "signin" dengan route screen login Anda
                                popUpTo("RombengRegister") { // Ganti "register_screen" dengan route screen registrasi Anda
                                    inclusive = true
                            // Anda mungkin ingin mereset state di ViewModel setelah beberapa saat atau saat navigasi
                            // rombengViewModel.resetAuthStates()
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(), // ViewModel untuk login/logout
    productViewModel: ProductViewModel = viewModel() // ViewModel untuk produk
) {
    UpdateStatusBarColor(true)

    val view = LocalView.current
    LaunchedEffect(Unit) {
        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val insetsController = WindowInsetsControllerCompat(window, view)

        insetsController.isAppearanceLightStatusBars = true // true jika background status bar terang, false jika gelap

        window.navigationBarColor = AndroidColor.BLACK // Menggunakan android.graphics.Color.BLACK

        insetsController.isAppearanceLightNavigationBars = false // false karena background navbar gelap (hitam)

        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }


    var showExitDialog by remember { mutableStateOf(false) }
    var showLogOutDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        val activity = LocalActivity.current
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Keluar Aplikasi") },
            text = { Text("Apakah Anda yakin ingin keluar?") },
            confirmButton = {
                TextButton(onClick = {
                    activity?.finish()
                }) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Tidak")
                }
            }
        )
    }

    // ==== Observasi Product State DI SINI, di luar LazyColumn ====
    val productsUIState by productViewModel.productsState.observeAsState(initial = ProductUIState.Idle)

    // ==== Layout Utama ====
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Bagian atas (Logo, Logout, Search, Categories) tetap menggunakan item tunggal di LazyColumn
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    // Row untuk Logo dan Logout Icon
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LogoRombeng()
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            modifier = Modifier.clickable { showLogOutDialog = true }
                        )

                        if (showLogOutDialog) {
                            AlertDialog(
                                onDismissRequest = { showLogOutDialog = false },
                                title = { Text("Log Out") },
                                text = { Text("Apakah Anda yakin ingin log out?") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            loginViewModel.logout() // Gunakan loginViewModel
                                            showLogOutDialog = false // Tutup dialog
                                            navController.navigate("signin") {
                                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                                launchSingleTop = true
                                            }
                                        }
                                    ) { Text("Ya") }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showLogOutDialog = false }) { Text("Tidak") }
                                }
                            )
                        }
                    }

                    // Search Bar
                    Box(
                        modifier = Modifier
                            .height(200.dp) // Sesuaikan jika perlu
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bg_home),
                            contentDescription = "Background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize() // Memastikan Column mengisi Box
                                .clickable { navController.navigate("search") },
                            verticalArrangement = Arrangement.Center, // Menengahkan TextFieldBuilder
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextFieldBuilder(
                                enabled = false,
                                value = "",
                                onValueChange = { },
                                placeholder = "Mau Cari Apa?",
                                leadingIcon = Icons.Default.Search,
                                modifier = Modifier
                                    .fillMaxWidth() // Agar TextField mengambil lebar penuh
                                    .padding(horizontal = 24.dp)
                                    .clip(shape = RoundedCornerShape(10.dp))
                            )
                        }
                    }

                    // Categories
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
                                else -> R.drawable.img // fallback image (ganti jika ada)
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(60.dp) // Atau gunakan weight jika ingin lebih dinamis
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFFFF8000))
                                        .clickable {
                                            // TODO: Navigasi ke halaman kategori
                                            // navController.navigate("category/$category")
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = imgId),
                                        contentDescription = category,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = category,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.height(27.dp),
                                    maxLines = 2, // Izinkan dua baris jika perlu
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                } // Akhir dari item tunggal untuk header

                // --- BAGIAN INTEGRASI PRODUCT LIST ---
                item {
                    Text(
                        text = "Rekomendasi Barang Bekas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 10.dp), // Beri padding atas juga
                        color = Color.Black
                    )
                }

                when (val currentState = productsUIState) {
                    is ProductUIState.Loading -> {
                        item { // item untuk menempatkan CircularProgressIndicator di dalam LazyColumn
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is ProductUIState.Success -> {
                        val products = currentState.products
                        if (products.isNotEmpty()) {
                            // `items` ini akan membuat banyak item di LazyColumn, satu untuk setiap baris produk
                            items(products.chunked(2)) { rowItems ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp), // Padding untuk Row agar tidak terlalu mepet tepi
                                    horizontalArrangement = if (rowItems.size == 1) Arrangement.Start else Arrangement.SpaceBetween
                                ) {
                                    rowItems.forEach { product ->
                                        CardBuilder(
                                            judul = product.namaProduk,
                                            harga = product.harga,
                                            lokasi = product.lokasi,
                                            penjual = product.penjualUsername,
                                            imageUrl = product.gambarUrls.firstOrNull(),
                                            modifier = Modifier
                                                .weight(1f) // Agar kartu mengisi ruang secara merata
                                                .padding(horizontal = 4.dp) // Padding antar kartu
                                                .clickable {
                                                    navController.navigate("productDetail/${product.id}")
                                                }
                                        )
                                    }
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f)) // Mengisi sisa ruang jika hanya 1 item
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp)) // Spasi antar baris produk
                            }
                        } else {
                            // Ini seharusnya ditangani oleh ProductUIState.Empty
                            // jika API mengembalikan 'success' dengan data kosong
                            item {
                                Text(
                                    "Tidak ada rekomendasi barang bekas saat ini.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    is ProductUIState.Error -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Gagal memuat rekomendasi.")
                                Text("Error: ${currentState.message}")
                                Button(onClick = { productViewModel.fetchProducts() }) {
                                    Text("Coba Lagi")
                                }
                            }
                        }
                    }
                    is ProductUIState.Empty -> {
                        item {
                            Text(
                                "Tidak ada rekomendasi barang bekas tersedia.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
//                            Button(onClick = { productViewModel.fetchProducts() }) {
//                                Text("Refresh")
//                            }
                        }
                    }
                    is ProductUIState.Idle -> {
                        item {
                            // Bisa tampilkan shimmer/placeholder atau biarkan kosong jika loading cepat
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Text("Memuat rekomendasi...") // Atau tidak tampilkan apa-apa
                            }
                        }
                    }
                }
                // --- AKHIR BAGIAN INTEGRASI PRODUCT LIST ---

                item { // Spacer di akhir LazyColumn sebelum BottomNavBar
                    Spacer(modifier = Modifier.height(80.dp)) // Sesuaikan tinggi spacer ini
                }
            } // Akhir LazyColumn
        } // Akhir Column utama

        // BottomNavBar dipastikan di bawah
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController)
        }
    }
}

@Composable
fun UploadItem(
    navController: NavController,
    upImgViewModel: UpImgViewModel = viewModel(),
    rombengViewModel: RombengViewModel = viewModel() // Pastikan RombengViewModel di-provide dengan benar
) {
    val view = LocalView.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val window = (view.context as Activity).window
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    var judul by rememberSaveable { mutableStateOf("") }
    var harga by rememberSaveable { mutableStateOf("") }
    var deskripsi by rememberSaveable { mutableStateOf("") }
    var lokasi by rememberSaveable { mutableStateOf("") }
    var selectedKategori by rememberSaveable { mutableStateOf<String?>(null) }

    // Menggunakan List<String> untuk menyimpan path file gambar
    var imageFilePathsToUpload by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

    // Kembali menggunakan List<Uri> untuk MultipleImagePickerRow
    var urisToUpload by rememberSaveable(stateSaver = listUriSaver()) { mutableStateOf<List<Uri>>(emptyList()) }

    val uploadState by upImgViewModel.uploadStatus.observeAsState(initial = UploadResult.Idle)
    val isFormValid2 by remember(judul, harga, lokasi, imageFilePathsToUpload, selectedKategori) {
        derivedStateOf {
            judul.isNotBlank() &&
                    harga.isNotBlank() &&
                    harga.all { it.isDigit() }  &&
                    lokasi.isNotBlank() &&
                    imageFilePathsToUpload.isNotEmpty() &&
                    selectedKategori != null && selectedKategori!!.isNotBlank()
        }
    }


    val remainingQuota = 3

    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler {
        showExitDialog = true
    }
    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirm = { (context as? Activity)?.finish() },
            onDismiss = { showExitDialog = false }
        )
    }

    LaunchedEffect(uploadState) {
        when (val result = uploadState) {
            is UploadResult.Loading -> {
                // UI akan menampilkan CircularProgressIndicator
            }
            is UploadResult.Success -> {
                Toast.makeText(context, result.data.message ?: "Upload berhasil", Toast.LENGTH_LONG).show()
                judul = ""
                harga = ""
                deskripsi = ""
                lokasi = ""
                selectedKategori = null
                urisToUpload = emptyList() // Reset list URI
                upImgViewModel.resetUploadStatus()
                navController.navigate("cart")
            }
            is UploadResult.Error -> {
                Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                upImgViewModel.resetUploadStatus()
            }
            is UploadResult.Idle -> {
                // Idle state
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text("Unggah Barang Bekasmu", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))

            // Menggunakan MultipleImagePickerRow Anda
            MultipleImagePickerRow(
                maxImages = 5,
                // initialFilePaths = imageFilePathsToUpload, // Jika ingin mempertahankan state dari luar
                onImageFilePathsChanged = { newPaths ->
                    imageFilePathsToUpload = newPaths
                }
                // context = context // Tidak perlu jika sudah LocalContext.current di dalam MultipleImagePickerRow
            )
            if (imageFilePathsToUpload.isEmpty() && uploadState !is UploadResult.Loading) {
                Text(
                    "Pilih minimal 1 gambar",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul Barang") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = judul.isBlank() && uploadState is UploadResult.Loading // Contoh validasi sederhana
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = harga,
                onValueChange = { harga = if (it.all { char -> char.isDigit() }) it else harga },
                label = { Text("Harga (Rp)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = (harga.isBlank() || !harga.all { it.isDigit() }) && uploadState is UploadResult.Loading
            )
            Spacer(modifier = Modifier.height(8.dp))

            KategoriDropdown(
                selectedKategori = selectedKategori,
                onKategoriSelected = { kategori -> selectedKategori = kategori },
                isError = selectedKategori == null && uploadState is UploadResult.Loading
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi (Opsional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 200.dp),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lokasi,
                onValueChange = {
                    lokasi = it
                },
                label = { Text("Lokasi (Kota/Daerah)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = lokasi.isBlank() && uploadState is UploadResult.Loading
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Sisa kuota unggah gratis: $remainingQuota dari 3",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uploadState is UploadResult.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            } else {
                UploadButton(
                    remainingQuota = remainingQuota,
                    isFormValid = isFormValid2,
                    onUploadClick = {
                        if (isFormValid2) {
                            // Panggil fungsi ViewModel dengan List<String> path file
                            upImgViewModel.uploadImagesFromPaths(
                                context = context, // Mungkin tidak lagi dibutuhkan jika path sudah absolut
                                imageFilePaths = imageFilePathsToUpload,
                                judul = judul, // Teruskan data lain jika ViewModel Anda menerimanya
                                harga = harga,
                                kategori = selectedKategori!!,
                                deskripsi = deskripsi,
                                lokasi = lokasi
                            )

                        } else {
                            Toast.makeText(context, "Harap lengkapi semua data yang diperlukan dan pilih minimal satu gambar.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onNavigateToPembayaran = {
                        navController.navigate("paymentList")
                    }
                )
            }

            Spacer(modifier = Modifier.height(64.dp))
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController)
        }
    }
}

@Composable
fun listUriSaver() = Saver<List<Uri>, List<String>>(
    save = { list -> list.map { it.toString() } },
    restore = { list -> list.map { Uri.parse(it) } }
)

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
                        .clickable { navController.navigate("home") }
                        .height(30.dp)
                )
            }
            val sviewModel: SearchViewModel = viewModel()
            val localFocusManager = LocalFocusManager.current
            // Search bar

            TextFieldBuilder(
                value = viewModel.search,
                onValueChange = { viewModel.onSearchChange(it) },
                placeholder = "Mau Cari Apa?",
                leadingIcon = Icons.Default.Search,
                // --- ATUR IME ACTION DAN ONSEARCH ACTION ---
                imeAction = ImeAction.Search, // Ini akan mengubah tombol Enter menjadi ikon/teks Search
                onSearch = {
                    val searchQuery = viewModel.search.trim()
                    if (searchQuery.isNotEmpty()) {
                        localFocusManager.clearFocus() // Sembunyikan keyboard
                        // Encode query untuk keamanan URL
                        val encodedQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8.toString())
                        navController.navigate("searchResult/$encodedQuery")
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 10.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(25.dp),
                        ambientColor = Color.Gray,
                        spotColor = Color.Gray
                    )
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
                                .height(40.dp)
                                .fillMaxWidth()
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
fun SearchResultScreen(
    navController: NavController,
    initialQuery: String?, // Terima initialQuery dari argumen navigasi
    searchViewModel: SearchViewModel = viewModel()
) {
    val localFocusManager = LocalFocusManager.current
    val decodedInitialQuery = remember(initialQuery) {
        try {
            initialQuery?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) } ?: ""
        } catch (e: Exception) {
            // Tangani error decoding jika ada, meskipun jarang terjadi dengan UTF-8
            initialQuery ?: "" // fallback ke query asli jika decode gagal
        }
    }


    // Set search text di ViewModel saat pertama kali screen dibuka atau initialQuery berubah
    LaunchedEffect(decodedInitialQuery) {
        if (decodedInitialQuery.isNotEmpty()) {
            searchViewModel.onSearchChange(decodedInitialQuery) // Update text di ViewModel
            searchViewModel.triggerSearch() // Langsung lakukan pencarian dengan query awal
        } else {
            // Jika initialQuery kosong (seharusnya tidak terjadi jika navigasi benar)
            // Anda bisa memutuskan untuk tidak melakukan apa-apa atau menampilkan state Idle
            searchViewModel.onSearchChange("") // Kosongkan search text
            // searchViewModel.searchResultsState = SearchUIState.Idle // Atau set state secara manual
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
    ) {
        // --- Search Bar Row ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp) // Beri sedikit jarak bawah
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.back), // Pastikan R.drawable.back ada
                    contentDescription = "Back Button",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .height(30.dp)
                )
            }
            TextFieldBuilder(
                value = searchViewModel.search, // Ambil value dari SearchViewModel
                onValueChange = { searchViewModel.onSearchChange(it) }, // Update SearchViewModel
                placeholder = "Cari produk...", // Placeholder bisa lebih spesifik
                leadingIcon = Icons.Default.Search,
                imeAction = ImeAction.Search,
                onSearch = {
                    searchViewModel.triggerSearch() // Panggil triggerSearch dari ViewModel
                    localFocusManager.clearFocus()
                },
                modifier = Modifier
                    .weight(1f) // Agar TextField mengisi sisa ruang
                    .padding(horizontal = 16.dp) // Sesuaikan padding
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(25.dp),
                        ambientColor = Color.Gray,
                        spotColor = Color.Gray
                    )
                    .clip(shape = RoundedCornerShape(25.dp))
            )
        }

        // --- Hasil Pencarian ---
        val searchState = searchViewModel.searchResultsState
        when (searchState) {
            is SearchUIState.Idle -> {
                if (searchViewModel.search.isBlank() && decodedInitialQuery.isBlank()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ketikkan sesuatu untuk dicari.")
                    }
                }
                // Jika tidak, biarkan loading atau state lain yang akan muncul berikutnya
            }
            is SearchUIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SearchUIState.Success -> {
                if (searchState.products.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchState.products.chunked(2)) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (rowItems.size < 2) Arrangement.Start else Arrangement.SpaceBetween
                            ) {
                                rowItems.forEach { product ->
                                    CardBuilder(
                                        judul = product.namaProduk,
                                        harga = product.harga,
                                        lokasi = product.lokasi,
                                        penjual = product.penjualUsername,
                                        imageUrl = product.gambarUrls.firstOrNull(),
                                        modifier = Modifier
                                            .weight(1f) // Agar kartu mengisi ruang yang tersedia
                                            .padding(4.dp) // Padding antar kartu
                                            .clickable {
                                                navController.navigate("productDetail/${product.id}")
                                            }
                                    )
                                }
                                // Tambahkan Spacer jika hanya ada satu item di baris terakhir untuk menjaga layout
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp))
                                }
                            }
                        }
                    }
                } else {
                    // Ini seharusnya ditangani oleh SearchUIState.Empty jika API mengembalikan data kosong
                    // Tapi sebagai fallback jika Success tapi listnya kosong
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tidak ada produk yang cocok dengan '${searchViewModel.search}'.")
                    }
                }
            }
            is SearchUIState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada produk yang cocok dengan '${searchViewModel.search}'.")
                }
            }
            is SearchUIState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Gagal memuat hasil pencarian.")
                        Text("Error: ${searchState.message}")
                        // Anda bisa menambahkan tombol "Coba Lagi" di sini
                        // Button(onClick = { searchViewModel.triggerSearch() }) { Text("Coba Lagi") }
                    }
                }
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
            .navigationBarsPadding()
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
                        .clickable { navController.navigate("home") }
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
                    .padding(bottom = 10.dp)
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
                            penjual = "Rombeng",
                            imageUrl = null,
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
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 52.dp)
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
                    placeholder = "Konfirmasi Password Baru",
                    leadingIcon = Icons.Default.Lock,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

            }

            Spacer(modifier = Modifier.height(40.dp))

            // Reset Password Button
            Button(
                onClick = { navController.navigate("resetSucced") {
                    popUpTo("reset") { inclusive = true }
                } },
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
            onClick = { navController.navigate("signin") {
                popUpTo("resetSucced") { inclusive = true }
            }},
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
                            Text(text = user.id.toString())
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
fun AddUserForm(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .padding(16.dp)
        .navigationBarsPadding()) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                RetrofitClient.myApi.addUser(username, password, email)
                    .enqueue(object : Callback<AddUserResponse> {
                        override fun onResponse(
                            call: Call<AddUserResponse>,
                            response: Response<AddUserResponse>
                        ) {
                            isLoading = false
                            if (response.isSuccessful) {
                                message = response.body()?.message ?: "Berhasil"
                            } else {
                                message = "Gagal: ${response.code()}"
                            }
                        }

                        override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                            isLoading = false
                            message = "Error: ${t.localizedMessage}"
                        }
                    })
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Mengirim..." else "Tambah User")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = Color.Blue)
        }
    }
}


@Composable
fun UpdateStatusBarColor(isLightBackground: Boolean) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = if (isLightBackground) Color.White.toArgb() else Color.Black.toArgb() //warna bg putih/hitam
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightBackground //warna ikon light/dark
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldBuilder(
    enabled: Boolean? = true,
    value: String,
    isError: Boolean? = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit,
    placeholder: String,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.LightGray,
        unfocusedIndicatorColor = Color.Transparent
    ),
    leadingIcon: ImageVector? = null,
    keyType: KeyboardType? = KeyboardType.Text,
    modifier: Modifier = Modifier,
    // --- TAMBAHAN BARU ---
    imeAction: ImeAction = ImeAction.Default, // Defaultnya bisa Next, Done, dll.
    onSearch: (() -> Unit)? = null // Aksi yang dijalankan saat tombol search ditekan
    // ---------------------
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
        // --- MODIFIKASI KeyboardOptions dan KeyboardActions ---
        keyboardOptions = KeyboardOptions(
            keyboardType = keyType ?: KeyboardType.Text,
            imeAction = imeAction // Gunakan imeAction yang di-pass
        ),
        keyboardActions = KeyboardActions(
            onSearch = { // Ini akan dieksekusi jika imeAction adalah ImeAction.Search
                onSearch?.invoke()
            },
            onDone = { // Anda juga bisa menangani onDone jika imeAction adalah ImeAction.Done
                if (imeAction == ImeAction.Done) {
                    // Lakukan sesuatu jika tombol Done ditekan,
                    // misalnya, jika onSearch juga null
                    // atau panggil onSearch jika itu yang diinginkan untuk Done juga
                    onSearch?.invoke()
                }
            }
            // Anda bisa menambahkan handler lain seperti onNext, onGo, dll.
        ),
        // ----------------------------------------------------
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    )

    if (isError == true) {
        Text(
            text = errorMessage ?: "",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassTextFieldBuilder(
    value: String,
    isError: Boolean? = false,
    errorMessage: String? = null,
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
    if (isError == true) {
        Text(
            text = errorMessage ?: "",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("upload", Icons.Default.Add, "Add"),
        BottomNavItem("cart", Icons.Default.ShoppingCart, "Cart"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    // Ambil route saat ini
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color(0xFFFF7B34),
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) Color.White else Color.Black
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Hindari banyak tumpukan duplikat
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)


@Composable
fun CardBuilder(
    judul: String,
    harga: String,
    lokasi: String,
    penjual: String?, // Tambahkan parameter untuk nama penjual
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFF8000)),
        modifier = modifier
            .width(150.dp)
            .height(240.dp) // Mungkin perlu menambah tinggi kartu sedikit
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.img)
                .error(R.drawable.img)
                .build(),
            contentDescription = "Gambar Produk: $judul",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = judul,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
            modifier = Modifier.padding(start = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        // Tampilkan Nama Penjual
        if (!penjual.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
//                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Penjual",
                    imageVector = Icons.Default.AccountCircle,
                    modifier = Modifier.size(12.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = penjual,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp)) // Beri sedikit padding di bawah
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriDropdown(
    selectedKategori: String?,
    onKategoriSelected: (String) -> Unit,
    isError: Boolean = false
) {
    val kategoriList = listOf("Elektronik", "Fashion", "Rumah Tangga", "Hobi", "Lainnya")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedKategori ?: "Pilih Kategori",
            onValueChange = {}, // Tidak perlu diubah langsung
            readOnly = true,
            label = { Text("Kategori") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            isError = isError
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            kategoriList.forEach { kategori ->
                DropdownMenuItem(
                    text = { Text(kategori) },
                    onClick = {
                        onKategoriSelected(kategori)
                        expanded = false

                    }
                )
            }
        }
    }
}

@Composable
fun MultipleImagePickerRow(
    modifier: Modifier = Modifier,
    maxImages: Int = 5,
    // initialFilePaths: List<String> = emptyList(), // Ganti initialUris dengan initialFilePaths
    onImageFilePathsChanged: (List<String>) -> Unit,
    // Tambahkan ViewModel atau context jika perlu untuk operasi file
    context: Context = LocalContext.current // Atau teruskan dari Composable induk
) {
    // Simpan path file, bukan URI
    var selectedImageFilePaths by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
    var uriToDisplayInDialog by remember { mutableStateOf<Uri?>(null) } // Untuk dialog, URI masih ok

    val canAddMoreImages = selectedImageFilePaths.size < maxImages

    // Fungsi untuk menyalin URI ke file cache dan mengembalikan path-nya
    fun copyUriToInternalCache(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "img_${System.currentTimeMillis()}_${uri.lastPathSegment ?: "temp"}"
            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            Log.e("ImagePicker", "Error copying URI to cache: ${e.message}")
            null
        }
    }
    val upImgViewModel: UpImgViewModel = viewModel()
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxImages - selectedImageFilePaths.size),
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxImages),
        onResult = { uris: List<Uri> ->
            if (uris.isNotEmpty()) {
                upImgViewModel.processAndCacheUris(uris, selectedImageFilePaths, maxImages, context) { updatedPaths ->
                    selectedImageFilePaths = updatedPaths
                    onImageFilePathsChanged(updatedPaths)
                }
            }
        }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null && canAddMoreImages) {
                copyUriToInternalCache(uri)?.let { filePath ->
                    val updatedPaths = (selectedImageFilePaths + filePath).distinct().take(maxImages)
                    selectedImageFilePaths = updatedPaths
                    onImageFilePathsChanged(updatedPaths)
                }
            }
        }
    )

    val editOverwriteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxImages),
        onResult = { uris: List<Uri> ->
            val newFilePaths = uris.mapNotNull { copyUriToInternalCache(it) }
            val updatedPaths = newFilePaths.distinct().take(maxImages)
            selectedImageFilePaths = updatedPaths
            onImageFilePathsChanged(updatedPaths)
        }
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedImageFilePaths, key = { it }) { filePath ->
                val imageUri = Uri.fromFile(File(filePath)) // Buat URI dari path untuk Coil
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFEFEF))
                        .clickable { uriToDisplayInDialog = imageUri } // Tampilkan URI di dialog
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(imageUri) // Gunakan URI dari path file
                                .crossfade(true)
//                                .size(Size(100.dp.toPx().toInt(), 100.dp.toPx().toInt())) // Minta Coil untuk resize
                                .error(android.R.drawable.ic_menu_gallery)
                                .build()
                        ),
                        contentDescription = "Gambar yang Dipilih",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    val scope = rememberCoroutineScope()
                    IconButton(
                        onClick = {
                            val newList = selectedImageFilePaths.toMutableList()
                            if (newList.remove(filePath)) {
                                // Hapus file dari cache di background thread
                                scope.launch(Dispatchers.IO) { // Gunakan scope yang sesuai
                                    try {
                                        File(filePath).delete()
                                    } catch (e: Exception) {
                                        Log.e("ImagePicker", "Error deleting cached file: $filePath, ${e.message}")
                                    }
                                }
                            }
                            selectedImageFilePaths = newList
                            onImageFilePathsChanged(newList)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Hapus gambar", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            if (canAddMoreImages) {
                item {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEFEFEF))
                            .clickable {
                                val remainingSlots = maxImages - selectedImageFilePaths.size
                                if (remainingSlots > 1 || selectedImageFilePaths.isEmpty()) {
                                    multiplePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                } else if (remainingSlots == 1) {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah Foto",
                            tint = Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gambar dipilih: ${selectedImageFilePaths.size}/$maxImages",
                fontSize = 14.sp
            )
            if (selectedImageFilePaths.isNotEmpty()) {
                Text(
                    text = "Edit Gambar",
                    color = Color(0xFFFF8D21),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        editOverwriteLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }

    uriToDisplayInDialog?.let { uriForDialog -> // URI dari path file untuk dialog
        Dialog(onDismissRequest = { uriToDisplayInDialog = null }) {
            // ... (sisa implementasi dialog, pastikan menggunakan uriForDialog)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(uriForDialog) // Gunakan URI yang disimpan untuk dialog
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Gambar Diperbesar",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
                IconButton(
                    onClick = { uriToDisplayInDialog = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup Dialog",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun UploadButton(
    modifier: Modifier = Modifier,
    remainingQuota: Int,
    isFormValid: Boolean,
    onUploadClick: () -> Unit,
    onNavigateToPembayaran: () -> Unit
) {
    val context = LocalContext.current
    var showQuotaDialog by remember { mutableStateOf(false) }

    val upImgViewModel: UpImgViewModel = viewModel()
    val uploadState by upImgViewModel.uploadStatus.observeAsState(initial = UploadResult.Idle)

    Column(modifier = modifier) {
        Button(
            onClick = {
                if (!isFormValid) {
                    Toast.makeText(
                        context,
                        "Semua kolom wajib diisi dan minimal satu gambar dipilih!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (remainingQuota == 0) {
                    showQuotaDialog = true
                } else {
                    onUploadClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF8D21),
                contentColor = Color.White
            ),
            enabled = isFormValid && uploadState !is UploadResult.Loading
        ) {
            Text("Unggah", color = Color.White)
        }
    }

    if (showQuotaDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showQuotaDialog = false },
            title = { Text("Kuota Habis") },
            text = { Text("Kuota gratis Anda sudah habis. Untuk mengunggah barang ini, Anda akan dikenakan biaya Rp250.") },
            confirmButton = {
                TextButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFFFF8D21),
                        contentColor = Color.White
                    ),
                    onClick = {
                        showQuotaDialog = false
                        onNavigateToPembayaran()
                    }
                ) {
                    Text("Bayar")
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFFFF3D3D),
                        contentColor = Color.White
                    ),
                    onClick = { showQuotaDialog = false }
                ) {
                    Text("Batal")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false),
        )
    }
}

@Composable
fun ExitConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Keluar Aplikasi") },
        text = { Text("Apakah Anda yakin ingin keluar dari aplikasi?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Ya") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Tidak") }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun RombengPreview() {
////    RombengLanding()
//}


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