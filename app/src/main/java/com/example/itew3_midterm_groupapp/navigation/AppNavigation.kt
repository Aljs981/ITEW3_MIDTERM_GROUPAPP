package com.example.itew3_midterm_groupapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.itew3_midterm_groupapp.ui.screens.AddItemLogScreen
import com.example.itew3_midterm_groupapp.ui.screens.HistoryLogsScreen
import com.example.itew3_midterm_groupapp.ui.screens.HomeScreen
import com.example.itew3_midterm_groupapp.ui.screens.ItemInfoScreen
import com.example.itew3_midterm_groupapp.viewmodel.ItemViewModel

private object Routes {
    const val HOME = "home"
    const val ITEM_INFO = "item_info/{itemId}"
    const val ADD_ITEM = "add_item"
    const val HISTORY_LOGS = "history_logs"

    fun itemInfo(itemId: Int) = "item_info/$itemId"
}

@Composable
fun AppNavigation(viewModel: ItemViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onItemClick = { itemId -> navController.navigate(Routes.itemInfo(itemId)) },
                onAddClick = { navController.navigate(Routes.ADD_ITEM) },
                onLogsTabClick = {
                    navController.navigate(Routes.HISTORY_LOGS) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.ITEM_INFO,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            val item by viewModel.getItemFlow(itemId).collectAsState(initial = null)

            ItemInfoScreen(
                item = item,
                onBack = { navController.popBackStack() },
                onMarkRetrieved = { viewModel.markAsRetrieved(it) },
                onSave = { viewModel.updateItem(it) },
                onRemove = { viewModel.removeItem(it) }
            )
        }

        composable(Routes.ADD_ITEM) {
            AddItemLogScreen(
                viewModel = viewModel,
                onClose = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HISTORY_LOGS) {
            HistoryLogsScreen(
                viewModel = viewModel,
                onHomeTabClick = {
                    navController.navigate(Routes.HOME) {
                        launchSingleTop = true
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
