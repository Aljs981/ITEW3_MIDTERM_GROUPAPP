package com.example.itew3_midterm_groupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itew3_midterm_groupapp.data.ItemLogDao

class ItemViewModelFactory(private val dao: ItemLogDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemViewModel(dao) as T
    }
}
