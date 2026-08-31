package com.example.itew3_midterm_groupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itew3_midterm_groupapp.data.ItemLog
import com.example.itew3_midterm_groupapp.data.ItemLogDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map



class ItemViewModel(private val dao: ItemLogDao) : ViewModel() {

    // ---- Home Page search ----
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Whenever searchQuery changes, switch to a new DB query (search vs. show-all).
    val activeItems: StateFlow<List<ItemLog>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) dao.getActiveItems() else dao.searchActiveItems(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---- History Logs Page ----
    val historyItems: StateFlow<List<ItemLog>> = dao.getHistoryItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    data class LogEvent(
        val id: String,
        val itemName: String,
        val location: String,
        val placedBy: String,
        val timestamp: Long,
        val isRetrieved: Boolean
    )

    val historyEvents: StateFlow<List<LogEvent>> = dao.getAllItems()
        .map { items ->
            val events = mutableListOf<LogEvent>()
            items.forEach { item ->
                events += LogEvent("placed-${item.id}", item.name, item.location, item.placedBy, item.loggedAt, isRetrieved = false)
                if (item.retrieved && item.retrievedAt != null) {
                    events += LogEvent("retrieved-${item.id}", item.name, item.location, item.placedBy, item.retrievedAt, isRetrieved = true)
                }
            }
            events.sortedByDescending { it.timestamp }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // ---- Item Info Page ----
    fun getItemFlow(itemId: Int) = dao.getItemById(itemId)

    // ---- Add Item Log Page ----
    fun addItem(name: String, placedBy: String, location: String, imageUri: String?) {
        viewModelScope.launch {
            dao.insert(
                ItemLog(
                    name = name,
                    placedBy = placedBy,
                    location = location,
                    imageUri = imageUri,
                    loggedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun updateItem(item: ItemLog) {
        viewModelScope.launch { dao.update(item) }
    }

    fun markAsRetrieved(item: ItemLog) {
        viewModelScope.launch {
            dao.update(item.copy(retrieved = true, retrievedAt = System.currentTimeMillis()))
        }
    }

    fun removeItem(item: ItemLog) {
        viewModelScope.launch { dao.delete(item) }
    }
}
