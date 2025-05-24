package com.example.rombeng.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
fun AddressListScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Alamat",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.Black
                    )
                }
            },
            backgroundColor = Color.White,
            contentColor = Color.Black
        )

        // Body Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AddressMenuItem(
                label = "Tambahkan Alamat Anda",
                icon = Icons.Default.Add,
                color = Color.Black,
                onClick = { /* Arahkan ke Form Tambah Alamat */ }
            )
            Spacer(modifier = Modifier.height(16.dp))

            AddressMenuItem(
                label = "Ganti Alamat Anda",
                icon = Icons.Default.Edit,
                color = Color.Black,
                onClick = { /* Arahkan ke Form Edit Alamat */ }
            )
            Spacer(modifier = Modifier.height(16.dp))

            AddressMenuItem(
                label = "Hapus Alamat Anda",
                icon = Icons.Default.Delete,
                color = Color.Red,
                onClick = { /* Hapus alamat atau tampilkan dialog konfirmasi */ }
            )
        }
    }
}

@Composable
fun AddressMenuItem(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp),
            tint = color
        )
        Text(
            text = label,
            fontSize = 16.sp,
            color = color
        )
    }
}