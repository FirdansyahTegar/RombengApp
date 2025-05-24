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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PaymentActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(20.dp)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}


@Composable
fun PaymentListScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Pembayaran",
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

        // Isi Menu
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            PaymentActionItem(
                icon = Icons.Default.Add,
                label = "Tambahkan Metode Pembayaran Anda",
                color = Color.Black
            ) {
                navController.navigate("addPaymentScreen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            PaymentActionItem(
                icon = Icons.Default.Add,
                label = "Ganti Metode Pembayaran Anda",
                color = Color.Black
            ) {
                navController.navigate("changePaymentScreen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            PaymentActionItem(
                icon = Icons.Default.Add,
                label = "Hapus Metode Pembayaran Anda",
                color = Color.Red
            ) {
                navController.navigate("deletePaymentScreen")
            }
        }
    }
}
