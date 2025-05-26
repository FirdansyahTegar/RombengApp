package com.example.rombeng.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rombeng.R

@Composable
fun UserProfileScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {

        // === Top Bar ===
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
            backgroundColor = Color.White
        )

        // === Profile Header ===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profilepicture),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Jonathan Damanik", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("jonathan@gmail.com", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* TODO: Navigate to Edit Profile */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8D21)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(45.dp)
            ) {
                Text("Edit Profile", color = Color.White)
            }
        }

        // === Identity Section ===
        Text(
            text = "Identity",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    navController.navigate("addressList")
                },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xFFF9F9F9)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_address_48), // Icon alamat
                    contentDescription = "Alamat",
                    tint = Color(0xFFFF8D21),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Alamat", fontSize = 14.sp, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))


        // === Preferences Section ===
        Text(
            text = "Preferences",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    navController.navigate("addressList")
                },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xFFF9F9F9)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF9F9F9))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Push Notification Settings */ }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification_48), // Icon notifikasi
                        contentDescription = "Push Notifications",
                        tint = Color(0xFFFF8D21),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Push Notifications", fontSize = 14.sp, color = Color.Black)
                }

                Divider(color = Color.LightGray)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Logout */ }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout_48), // Icon logout
                        contentDescription = "Logout",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Logout", fontSize = 14.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }

    }
}
