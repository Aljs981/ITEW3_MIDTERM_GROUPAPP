package com.example.itew3_midterm_groupapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.itew3_midterm_groupapp.ui.components.BottomNavBar
import com.example.itew3_midterm_groupapp.ui.components.SulyapTab
import com.example.itew3_midterm_groupapp.ui.theme.SulyapBackground
import com.example.itew3_midterm_groupapp.ui.theme.SulyapGreen
import com.example.itew3_midterm_groupapp.ui.theme.SulyapSearchBarBg
import com.example.itew3_midterm_groupapp.viewmodel.ItemViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HistoryLogsScreen(
    viewModel: ItemViewModel,
    onHomeTabClick: () -> Unit
) {
    val events by viewModel.historyEvents.collectAsState()
    val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }

    Scaffold(
        containerColor = SulyapBackground,
        bottomBar = {
            BottomNavBar(
                currentTab = SulyapTab.LOGS,
                onTabSelected = { if (it == SulyapTab.HOME) onHomeTabClick() }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(16.dp))
            Text("HISTORY LOGS", style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text("Manage item logs", color = Color.White.copy(alpha = 0.5f))
            Spacer(Modifier.height(20.dp))

            if (events.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    Text("Your item history will show up here.", color = Color.White.copy(alpha = 0.5f))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    itemsIndexed(events)
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.itemsIndexed(
    events: List<com.example.itew3_midterm_groupapp.viewmodel.ItemViewModel.LogEvent>
) {
    items(events.size) { index ->
        val event = events[index]
        val isLast = index == events.lastIndex
        val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
        val timeLabel = if (System.currentTimeMillis() - event.timestamp < 60_000) "Just Now" else timeFormat.format(Date(event.timestamp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // timeline rail: dot + connecting line
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(24.dp)) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (event.isRetrieved) SulyapGreen else Color.White.copy(alpha = 0.4f))
                )
                if (!isLast) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .width(1.dp)
                            .background(Color.White.copy(alpha = 0.15f))
                    )
                }
            }
            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SulyapSearchBarBg)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(event.itemName, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(timeLabel, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (event.isRetrieved) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = if (event.isRetrieved) SulyapGreen else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    if (event.isRetrieved) {
                        Text("Retrieved at ", color = Color.White.copy(alpha = 0.6f))
                        Text(event.location, fontWeight = FontWeight.SemiBold, color = Color.White)
                    } else {
                        Text("Placed by ${event.placedBy} at ", color = Color.White.copy(alpha = 0.6f))
                        Text(event.location, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}