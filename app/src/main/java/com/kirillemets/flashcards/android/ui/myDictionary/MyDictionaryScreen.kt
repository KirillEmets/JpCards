package com.kirillemets.flashcards.android.ui.myDictionary

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyDictionaryScreen(
    myDictionaryViewModel: MyDictionaryFragmentViewModel = viewModel(),
    onButtonAddWordsClick: () -> Unit
) {
    val myWordsUIState by myDictionaryViewModel.myWordsUIState.collectAsState(MyWordsUIState())

    when {
        myWordsUIState.loading -> {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        myWordsUIState.dictionaryEmpty -> MyDictionaryScreenNoWords(onButtonAddWordsClick)

        else -> MyDictionaryScreenContent(
            myWordsUIState = myWordsUIState,
            onNoteClick = {
                myDictionaryViewModel.onItemClick(it)
            },
            onNoteLongClick = {
                myDictionaryViewModel.onItemLongClick(it)
            },
            onNoteTtsClick = {
                myDictionaryViewModel.onTtsClick(it)
            },
            onFilterValueChange = {
                myDictionaryViewModel.onFilterValueChange(it)
            }
        )
    }
}

@Composable
fun MyDictionaryScreenNoWords(onButtonAddWordsClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("You currently have no words in your list.", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onButtonAddWordsClick) {
            Text("Add new words")
        }
    }
}

@Preview
@Composable
fun MyDictionaryScreenPreview() {
    MyDictionaryScreenContent(
        myWordsUIState = MyWordsUIState(),
        onNoteClick = {},
        onNoteLongClick = {},
        onNoteTtsClick = {},
        onFilterValueChange = {}
    )
}

@Composable
fun MyDictionaryScreenContent(
    myWordsUIState: MyWordsUIState,
    onNoteClick: (noteId: Int) -> Unit,
    onNoteLongClick: (noteId: Int) -> Unit,
    onNoteTtsClick: (noteId: Int) -> Unit,
    onFilterValueChange: (value: String) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = myWordsUIState.filter,
            onValueChange = onFilterValueChange
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(2.dp),
            text = "${myWordsUIState.wordCount} words shown."
        )

        LazyColumn(Modifier.fillMaxSize()) {
            val notes = myWordsUIState.notes
            items(notes.size) {
                NoteListItem(
                    noteUIState = notes[it],
                    onClick = {
                        onNoteClick(notes[it].noteId)
                    },
                    onLongClick = {
                        onNoteLongClick(notes[it].noteId)
                    },
                    onTtsClick = {
                        onNoteTtsClick(notes[it].noteId)
                    })
            }
        }
    }

    val currentLocalContext = LocalContext.current
    LaunchedEffect(key1 = myWordsUIState.ttsFailed) {
        if (myWordsUIState.ttsFailed)
            Toast.makeText(currentLocalContext, "Tts service failed", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteListItem(
    noteUIState: NoteUIState,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onTtsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
    ) {
        val color =
            if (noteUIState.selected)
                MaterialTheme.colorScheme.primary.copy(0.3f)
            else Transparent

        Column(
            Modifier
                .background(color = color)
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            WordAndPlayButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                japanese = noteUIState.japanese,
                onTtsClick = onTtsClick
            )

            ReadingTranslationProgressRow(
                Modifier.fillMaxWidth(),
                noteUIState.reading,
                noteUIState.english,
                noteUIState.lastDelay,
                noteUIState.lastDelayReversed
            )
        }
    }
}

@Composable
fun WordAndPlayButtonRow(
    modifier: Modifier,
    japanese: String,
    onTtsClick: () -> Unit
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = japanese,
            fontSize = 24.sp
        )

        IconButton(
            modifier = Modifier,
            onClick = onTtsClick
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, null)
        }
    }
}

@Composable
fun ReadingTranslationProgressRow(
    modifier: Modifier,
    reading: String,
    english: String,
    lastDelay: Int,
    lastDelayReversed: Int
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
        ) {
            if (reading.isNotBlank())
                Text(
                    text = reading,
                    fontSize = 18.sp,
                )

            Text(
                text = english,
                fontSize = 18.sp
            )
        }

        Text(
            text = "${lastDelay}d/${lastDelayReversed}d",
            fontSize = 18.sp,
        )
    }
}