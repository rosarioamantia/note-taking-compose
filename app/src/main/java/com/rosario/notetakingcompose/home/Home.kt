package com.rosario.notetakingcompose.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.rosario.notetakingcompose.login.LoginScreen
import com.rosario.notetakingcompose.models.Note
import com.rosario.notetakingcompose.repository.Resources
import com.rosario.notetakingcompose.ui.theme.NoteTakingComposeTheme
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    homeViewModel: HomeViewModel?,
    onNoteClick: (id: String) -> Unit,
    navToDetailPage: () -> Unit,
    navToLoginPage: () -> Unit
) {
    val homeState = homeViewModel?.homeState ?: HomeState()

    var openDialog by remember {
        mutableStateOf(false)
    }

    var selectedNote: Note? by remember {
        mutableStateOf(null)
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = remember { SnackbarHostState() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navToDetailPage.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null)
            }
        },
        topBar = {
            TopAppBar(
                navigationIcon = {},
                actions = {
                    IconButton(onClick = {
                        homeViewModel?.signOut()
                        navToLoginPage.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null)
                    }
                },
                title = {
                    Text(text = "Home")
                }
            )
        },
    ){
        padding ->
        Column(modifier = Modifier.padding(padding)) {
            when(homeState.notesList){
                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is Resources.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        itemsIndexed(
                            items = homeState.notesList.data ?: emptyList()
                        ){ index, note ->
                            NoteItem(
                                note = note,
                                onLongClick = {
                                    openDialog = true
                                    selectedNote = note
                                }
                            ){
                                onNoteClick.invoke(note.documentId)
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = homeState.notesList.throwable?.localizedMessage ?: "Unknown Error",
                        color = Color.Red
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = homeViewModel?.hasUser == false) {
        if(homeViewModel?.hasUser == false){
            navToLoginPage.invoke()
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onLongClick: () -> Unit,
    onClick: () -> Unit
){
    Card(
       modifier = Modifier
           .combinedClickable(
               onClick = { onClick.invoke() },
               onLongClick = { onLongClick.invoke() }
           )
           .padding(8.dp)
           .fillMaxWidth(),
    ){
        Column {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(4.dp))

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = note.description,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = formatDate(note.timestamp),
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.End),
                maxLines = 4
            )
        }
    }
}

private fun formatDate(timestamp: Timestamp): String{
    val sdf = SimpleDateFormat("MM-dd-yy", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}


@Preview(showSystemUi = true)
@Composable
fun PrevHomeScreen(){
    NoteTakingComposeTheme {
        Home(
            navToLoginPage = { /*TODO*/ },
            onNoteClick = {},
            navToDetailPage = {},
            homeViewModel = null
        )
    }
}