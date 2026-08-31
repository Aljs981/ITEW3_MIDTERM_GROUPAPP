package com.example.itew3_midterm_groupapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.itew3_midterm_groupapp.data.ItemLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInfoScreen(
    item: ItemLog?,
    onBack: () -> Unit,
    onMarkRetrieved: (ItemLog) -> Unit,
    onSave: (ItemLog) -> Unit,
    onRemove: (ItemLog) -> Unit
) {
    if (item == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    var editing by remember { mutableStateOf(false) }
    var nameField by remember(item.id) { mutableStateOf(item.name) }
    var locationField by remember(item.id) { mutableStateOf(item.location) }
    var placedByField by remember(item.id) { mutableStateOf(item.placedBy) }

    var showRetrievedBanner by remember { mutableStateOf(false) }
    var showRemoveConfirm by remember { mutableStateOf(false) }

    Scaffold(containerColor = com.example.itew3_midterm_groupapp.ui.theme.SulyapBackground) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                if (item.imageUri != null) {
                    AsyncImage(
                        model = item.imageUri.toUri(), // Updated to toUri() extension
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(260.dp)
                    )
                } else {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.35f), shape = RoundedCornerShape(50))
                ) {
                    // Updated to AutoMirrored ArrowBack
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                if (showRetrievedBanner) {
                    RetrievedBanner(
                        itemName = item.name,
                        location = item.location,
                        onDismiss = { showRetrievedBanner = false },
                        modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp)
                    )
                }
            }

            Column(Modifier.padding(20.dp)) {
                if (editing) {
                    OutlinedTextField(
                        value = nameField,
                        onValueChange = { nameField = it },
                        label = { Text("Item name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(Modifier.height(20.dp))

                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "LOCATION",
                    value = locationField,
                    editing = editing,
                    onValueChange = { locationField = it }
                )
                Spacer(Modifier.height(14.dp))
                DetailRow(
                    icon = Icons.Default.Schedule,
                    label = "TIME LOGGED",
                    value = formatLoggedTime(item.loggedAt),
                    editing = false,
                    onValueChange = {}
                )
                Spacer(Modifier.height(14.dp))
                DetailRow(
                    icon = Icons.Default.Person,
                    label = "PLACED BY",
                    value = placedByField,
                    editing = editing,
                    onValueChange = { placedByField = it }
                )

                Spacer(Modifier.height(28.dp))

                if (editing) {
                    Button(
                        onClick = {
                            onSave(item.copy(name = nameField, location = locationField, placedBy = placedByField))
                            editing = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Save changes", color = Color.Black) }
                } else if (!item.retrieved) {
                    Button(
                        onClick = {
                            onMarkRetrieved(item)
                            showRetrievedBanner = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = com.example.itew3_midterm_groupapp.ui.theme.SulyapMarkRetrievedBg)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = com.example.itew3_midterm_groupapp.ui.theme.SulyapBlueLink)
                        Spacer(Modifier.width(8.dp))
                        Text("Mark as Retrieved", color = com.example.itew3_midterm_groupapp.ui.theme.SulyapBlueLink)
                    }
                } else {
                    AssistChip(
                        onClick = {},
                        label = { Text("Retrieved") },
                        leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null) }
                    )
                }

                Spacer(Modifier.height(12.dp))

                if (!editing) {
                    Button(
                        onClick = { editing = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5A627),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Edit details", color = Color.White)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { showRemoveConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.itew3_midterm_groupapp.ui.theme.SulyapRemoveBg),
                    // Removed redundant qualifier
                    border = BorderStroke(1.dp, com.example.itew3_midterm_groupapp.ui.theme.SulyapRemoveBorder)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Remove Item", color = Color.White)
                }
            }
        }
    }

    if (showRemoveConfirm) {
        AlertDialog(
            onDismissRequest = { showRemoveConfirm = false },
            title = { Text("Remove \"${item.name}\"?") },
            text = { Text("This permanently deletes the item log. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showRemoveConfirm = false
                    onRemove(item)
                    onBack()
                }) { Text("Remove", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun DetailRow(
    // Removed redundant qualifier
    icon: ImageVector,
    label: String,
    value: String,
    editing: Boolean,
    onValueChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (editing) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(value, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun RetrievedBanner(
    itemName: String,
    location: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                // @Suppress("SpellCheckingInspection") can be added above the function if "SULYAP" still gets flagged
                Text("SULYAP RETRIEVE-LOG", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text("Item Retrieved", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    "$itemName in $location has been retrieved.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Dismiss") }
    }
}

private fun formatLoggedTime(loggedAt: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(Date(loggedAt))
}