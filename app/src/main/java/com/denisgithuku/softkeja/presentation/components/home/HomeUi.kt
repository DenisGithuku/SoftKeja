package com.denisgithuku.softkeja.presentation.components.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisgithuku.softkeja.domain.model.Home
import com.denisgithuku.softkeja.domain.model.HomeCategory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalMaterialApi
@Composable
fun HomeUi(
    scaffoldState: ScaffoldState,
    onOpenHome: (Home) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    sheetState: ModalBottomSheetState,
    onNavigateToAbout: () -> Unit
) {
    val uiState = homeViewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()

    val dialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    )
    var showDialog by remember {
        mutableStateOf(false)
    }
    val horizontalListState = rememberLazyListState()
    val verticalListState = rememberLazyListState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (uiState.error.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(uiState.error)
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopRowSection(
            modifier = Modifier,
            initial = uiState.user?.email.toString()[0]
                .uppercase(Locale.getDefault()),
            onClickInitialBox = {
                onNavigateToAbout()
            }
        )

        LazyRow(state = horizontalListState) {
            items(uiState.categories) { category ->
                HomeCategoryItem(
                    category = category,
                    isSelected = category.name == uiState.selectedCategory,
                    onSelectCategory = {
                        homeViewModel.onEvent(HomeUiEvent.FilterHomes(category.name))
                    }
                )
                Divider()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = uiState.selectedCategory, style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            )
        }

        if (uiState.homes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Oops no homes available. Check different category",
                    style = TextStyle(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }


        LazyColumn(state = verticalListState) {
            items(uiState.homes) { home ->
                HomeItem(
                    home = home,
                    onSelectHome = {
                        onOpenHome(it)
                    }
                )
            }
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun TopRowSection(
    modifier: Modifier = Modifier,
    header: String = "SoftKeja",
    initial: String,
    onClickInitialBox: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = header,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
        )
        Box(
            modifier = modifier
                .size(40.dp)
                .background(
                    MaterialTheme.colors.primary,
                    RoundedCornerShape(14.dp),
                )
                .clickable {
                    onClickInitialBox()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                style = TextStyle(
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 16.sp
                )
            )

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = Color.Green,
                        shape = CircleShape
                    )
                    .align(
                        Alignment.BottomStart
                    )
                    .padding(bottom = 6.dp, start = 6.dp)
            )
        }

    }
}

@Composable
fun HomeCategoryItem(
    category: HomeCategory,
    isSelected: Boolean,
    onSelectCategory: (HomeCategory) -> Unit
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(alpha = 0.3f)
    val contentColor =
        if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
    val animatedBg = animateColorAsState(targetValue = backgroundColor)
    val animatedContentColor = animateColorAsState(targetValue = contentColor)
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
            .background(
                color = animatedBg.value,
                shape = CircleShape
            )
            .clickable {
                onSelectCategory(category)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = category.name,
            style = TextStyle(
                color = animatedContentColor.value
            )
        )
    }
}

@Composable
fun HomeItem(
    modifier: Modifier = Modifier,
    home: Home,
    onSelectHome: (Home) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSelectHome(home)
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GlideImage(
            imageModel = home.imageUrl,
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
        )
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = home.name,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
            Text(
                text = "${home.monthly_rent} Ksh",
                style = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .wrapContentSize()
                    .background(
                        if (home.available)
                            MaterialTheme.colors.primary.copy(alpha = 0.2f) else MaterialTheme.colors.secondary.copy(
                            alpha = 0.2f
                        ),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = if (home.available) "Available" else "Occupied",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Open Home",
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun TopRowPreview() {
    TopRowSection(
        header = "SoftKeja",
        modifier = Modifier,
        initial = "G",
        onClickInitialBox = {}
    )
}

@Preview
@Composable
fun HomeCategoryPrev() {
    HomeCategoryItem(
        isSelected = false,
        onSelectCategory = {},
        category = HomeCategory()
    )
}
