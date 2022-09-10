package com.denisgithuku.softkeja.presentation.components.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisgithuku.softkeja.domain.model.Home
import com.denisgithuku.softkeja.presentation.components.home.HomeItem
import kotlinx.coroutines.launch

@Composable
fun BookMarksUi(
    scaffoldState: ScaffoldState,
    onOpenHome: (Home) -> Unit,
    bookMarksViewModel: BookMarksViewModel = hiltViewModel()
) {

    val uiState = bookMarksViewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "All Bookmarks", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(state = listState) {
            items(uiState.bookmarks) { home ->
                HomeItem(home = home, onSelectHome = {
                    onOpenHome(it)
                })
            }
        }

        if (uiState.bookmarks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No available bookmarks.", style = TextStyle(fontSize = 16.sp))
            }
        }

    }

}