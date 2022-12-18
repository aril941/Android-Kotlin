package com.example.jetpackapp.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackapp.R

@Composable
fun ProfileActivity(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        profile()
    }
}

@Composable
fun profile(modifier: Modifier = Modifier) {
    Column(modifier= Modifier){
        Box{
            Image(

                painter = painterResource(R.drawable.formal),
                contentDescription = "profil Image",
                modifier = modifier
                    .size(200.dp)
                    .padding(30.dp)


            )
        }
        Column(modifier = Modifier) {
            Text(
                text="Nama : Muhammad Kharirrushofa",
                fontSize= 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Justify
            )
            Text(
                text="Email : muhammadaril5525@gmail.com",
                fontSize= 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Justify
            )
        }
    }
}

