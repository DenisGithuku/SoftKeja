package com.denisgithuku.softkeja.presentation.components.details

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun HomeDetailsUi(
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Unit,
    onViewOnMap: (String) -> Unit,
    homeDetailsViewModel: HomeDetailsViewModel = hiltViewModel()
) {
    val uiState = homeDetailsViewModel.uiState.collectAsState().value
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current as Activity

    val callLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:" + uiState.home.telephone)
            ).also {
                context.startActivity(it)
            }
        } else {
            Toast.makeText(
                context,
                "App needs permissions to make phone calls",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    val mapsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onViewOnMap(uiState.home.location)
        } else {
            Toast.makeText(
                context,
                "App needs to access location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            GlideImage(
                imageModel = uiState.home.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp)
                    )
                    .height(350.dp),
                contentScale = ContentScale.Crop
            )
            TopRow(
                onNavigateUp = {
                    onNavigateUp()
                },
                onBookMarkItem = {
                    homeDetailsViewModel.onEvent(
                        HomeDetailsUiEvent.BookMarkHome(
                            uiState.home
                        ).also {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Home bookmarked")
                            }
                        }
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = uiState.home.name,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Category: ${uiState.home.category}",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (uiState.home.available) "Available" else "Occupied",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = 12.dp
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Monthly rent: ${uiState.home.monthly_rent} Ksh",
                    style = TextStyle(
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "Features",
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(state = listState) {
                    items(uiState.home.features) { feature ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        MaterialTheme.colors.primary.copy(
                                            alpha = 0.4f
                                        ),
                                        shape = CircleShape
                                    ),
                            )
                            Text(
                                text = feature,
                                style = TextStyle(color = MaterialTheme.colors.primary)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        shape = CircleShape,
                        onClick = {
                            callLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    ) {
                        Text(text = "Book Home")
                    }
                    OutlinedButton(
                        shape = CircleShape,
                        onClick = {
                            mapsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    ) {
                        Text(text = "View on Map")
                    }
                }
            }
        }
    }
}

@Composable
fun TopRow(
    onNavigateUp: () -> Unit,
    onBookMarkItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier
            .size(40.dp)
            .clickable {
                onNavigateUp()
            }
            .background(
                MaterialTheme.colors.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            , contentAlignment = Alignment.Center) {

            Icon(
                modifier = Modifier.padding(4.dp),
                tint = Color.White,
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = "Back"
            )

        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    onBookMarkItem()
                }
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                modifier = Modifier.padding(4.dp),
                tint = Color.White,
                imageVector = Icons.Rounded.Bookmark,
                contentDescription = "Bookmark home"
            )

        }
    }
}
