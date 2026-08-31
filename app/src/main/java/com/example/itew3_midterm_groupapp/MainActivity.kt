package com.example.itew3_midterm_groupapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.itew3_midterm_groupapp.data.AppDatabase // Add this import
import com.example.itew3_midterm_groupapp.navigation.AppNavigation
import com.example.itew3_midterm_groupapp.viewmodel.ItemViewModel
import com.example.itew3_midterm_groupapp.viewmodel.ItemViewModelFactory // Add this import
import com.example.itew3_midterm_groupapp.ui.theme.ITEW3_MIDTERM_GROUPAPPTheme

class MainActivity : ComponentActivity() {

    // UPDATE: We are now using your Factory to build the ViewModel!
    private val viewModel: ItemViewModel by viewModels {
        // This gets your database instance and passes the DAO to the factory
        ItemViewModelFactory(AppDatabase.getDatabase(this).itemLogDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Wrap your app in your custom theme
            ITEW3_MIDTERM_GROUPAPPTheme {
                // Call your navigation component and pass the ViewModel
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}