package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteItem
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotBottomBar
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun NotesLibraryScreen(
    notes: List<NoteItem>,
    lang: String,
    onBack: () -> Unit,
    onToggleBookmark: (String) -> Unit,
    onAddNote: (title: String, subject: String, content: String) -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var newNoteTitle by remember { mutableStateOf("") }
    var newNoteSubject by remember { mutableStateOf("Physics") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Note", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newNoteTitle,
                        onValueChange = { newNoteTitle = it },
                        label = { Text("Note Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_note_title_input")
                    )
                    OutlinedTextField(
                        value = newNoteSubject,
                        onValueChange = { newNoteSubject = it },
                        label = { Text("Subject") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newNoteTitle.isNotBlank()) {
                            onAddNote(newNoteTitle, newNoteSubject, "")
                            newNoteTitle = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
                ) {
                    Text(AppStrings.get("save", lang))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(AppStrings.get("clear", lang))
                }
            }
        )
    }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("notes_library", lang),
                onBack = onBack
            )
        },
        bottomBar = {
            StudyPilotBottomBar(
                currentRoute = "notes",
                lang = lang,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = StudyPilotPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("add_note_fab")
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Physics", "Math", "Chemistry").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        modifier = Modifier
                            .clickable { selectedFilter = filter }
                            .testTag("note_filter_$filter"),
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) StudyPilotPrimary else Color.White,
                        border = if (isSelected) null else CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                    ) {
                        Text(
                            text = if (filter == "All") AppStrings.get("all", lang) else filter,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else StudyPilotTextMuted,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(AppStrings.get("search_notes", lang), fontSize = 13.sp, color = StudyPilotTextSubtle) },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = "Search", tint = StudyPilotTextSubtle, modifier = Modifier.size(20.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("notes_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = StudyPilotPrimary,
                    unfocusedBorderColor = StudyPilotBorder
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notes List
            val filtered = notes.filter { note ->
                val matchesFilter = when (selectedFilter) {
                    "All" -> true
                    "Math" -> note.subject.contains("Math", ignoreCase = true)
                    else -> note.subject.equals(selectedFilter, ignoreCase = true)
                }
                val matchesQuery = searchQuery.isBlank() || note.title.contains(searchQuery, ignoreCase = true)
                matchesFilter && matchesQuery
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_item_${note.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // PDF Icon
                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = StudyPilotRedBg
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.Description,
                                        contentDescription = "PDF",
                                        tint = StudyPilotRed,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = note.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotTextDark
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "${note.fileType}  •  ${note.fileSize}",
                                    fontSize = 11.sp,
                                    color = StudyPilotTextMuted
                                )
                            }

                            IconButton(onClick = { onToggleBookmark(note.id) }) {
                                Icon(
                                    imageVector = if (note.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (note.isBookmarked) StudyPilotPrimary else StudyPilotTextSubtle
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = "Open",
                                tint = StudyPilotTextSubtle
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }
}
