package com.example.rombeng.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Profil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            backgroundColor = Color.White,
            contentColor = Color.Black
        )

        // Menu Items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileMenuItem(
                icon = Icons.Outlined.Person,
                label = "Profil",
                onClick = { navController.navigate("profilDetail") }
            )
            ProfileMenuItem(
                icon = Icons.Outlined.Home,
                label = "Alamat",
                onClick = { navController.navigate("addressList") }
            )
            ProfileMenuItem(
                icon = Icons.Outlined.AccountBalanceWallet,
                label = "Pembayaran",
                onClick = { navController.navigate("paymentList") }
            )
            ProfileMenuItem(
                icon = Icons.Outlined.ChatBubbleOutline,
                label = "Customer Service",
                onClick = { navController.navigate("customerService") }
            )
            ProfileMenuItem(
                icon = Icons.Outlined.Settings,
                label = "Pengaturan",
                onClick = { navController.navigate("paymentListScreen") }
            )
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .height(60.dp)
            .background(color = Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp),
            tint = Color.Black
        )
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}