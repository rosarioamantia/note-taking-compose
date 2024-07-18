package com.rosario.notetakingcompose.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = HomeViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "This is Home Screen",
            fontSize = 30.sp
        )
        Button(
            onClick = {
                homeViewModel.logout()
            }
        ) {
            Text(
                text = "Logout",
                fontSize = 10.sp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevHomeScreen(){
    HomeScreen()
}