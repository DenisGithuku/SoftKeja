package com.denisgithuku.softkeja.presentation.components.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisgithuku.softkeja.R

@Composable
fun AboutUi(
    onNavigateToProfile: () -> Unit,
    aboutViewModel: AboutViewModel = hiltViewModel()
) {
    val uiState = aboutViewModel.uiState.collectAsState().value;
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape
                )
                .size(120.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "App icon"
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "SoftKeja",
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )
        Divider(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp))
        Text(
            text = "Version: 1.0.0-rc04",
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Divider(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp))
        Text(
            text = "We are working everyday to improve the functionalities and a seamless performance.",
            style = TextStyle(
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = {
            onNavigateToProfile()
        },
        shape = RoundedCornerShape(12.dp)) {
            Text(text = "Profile")
            Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
        }
    }
}
