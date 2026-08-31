package com.example.itew3_midterm_groupapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.itew3_midterm_groupapp.data.ItemLog
import com.example.itew3_midterm_groupapp.ui.components.BottomNavBar
import com.example.itew3_midterm_groupapp.ui.components.SulyapTab
import com.example.itew3_midterm_groupapp.ui.theme.*
import com.example.itew3_midterm_groupapp.viewmodel.ItemViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ItemViewModel,
    onItemClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onLogsTabClick: () -> Unit
) {
    val items by viewModel.activeItems.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    Scaffold(
        containerColor = SulyapBackground,
        bottomBar = {
            BottomNavBar(
                currentTab = SulyapTab.HOME,
                onTabSelected = { if (it == SulyapTab.LOGS) onLogsTabClick() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = SulyapGreen,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add item", tint = SulyapBackground)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SulyapBackground)
                .padding(padding)
        ) {
            // ---- Header block (bg 070D1F, white bottom border) ----
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SulyapHeaderBg)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text("HOME TRACKER", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                Text("Hello, User", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f), thickness = 1.dp)

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = viewModel::onSearchQueryChanged,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)),
                    placeholder = { Text("Search items or location", color = Color.White.copy(alpha = 0.4f)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.6f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SulyapSearchBarBg,
                        unfocusedContainerColor = SulyapSearchBarBg,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = SulyapGreen,
                        unfocusedBorderColor = SulyapSearchBarBg
                    )
                )

                Spacer(Modifier.height(20.dp))
                Text(
                    "CURRENTLY LOGGED ITEMS",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.67f)
                )
                Spacer(Modifier.height(8.dp))

                if (items.isEmpty()) {
                    Box(Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text("No items logged yet. Tap + to add one.", color = Color.White.copy(alpha = 0.5f))
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(items, key = { it.id }) { item ->
                            ItemRow(item = item, onClick = { onItemClick(item.id) })
                        }
                        item { Spacer(Modifier.height(90.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemRow(item: ItemLog, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SulyapSearchBarBg)
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- This box is the fix: it now actually renders the picked photo ---
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.06f))
        ) {
            if (item.imageUri != null) {
                AsyncImage(
                    model = Uri.parse(item.imageUri),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.SemiBold, color = Color.White)
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SulyapGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(item.location, fontSize = 13.sp, color = Color.White.copy(alpha = 0.6f))
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SulyapGreen))
            Spacer(Modifier.width(4.dp))
            Text(timeAgo(item.loggedAt), style = MaterialTheme.typography.labelSmall, color = SulyapGreen)
        }
    }
}

private fun timeAgo(loggedAt: Long): String {
    val diffMs = System.currentTimeMillis() - loggedAt
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMs)
    return when {
        minutes < 1 -> "now"
        minutes < 60 -> "${minutes}m"
        minutes < 1440 -> "${minutes / 60}h"
        else -> "${minutes / 1440}d"
    }
}