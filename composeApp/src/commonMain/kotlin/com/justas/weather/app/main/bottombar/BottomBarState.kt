package com.justas.weather.app.main.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class BottomBarState(
    val items: PersistentList<BottomBarItem> = persistentListOf(
        BottomBarItem.HOME,
        BottomBarItem.INFO
    ),
    val selectedItem: BottomBarItem = BottomBarItem.HOME
)

data class BottomBarItem(
    val name: String = "",
    val icon: ImageVector = Icons.Default.Home,
) {
    companion object {
        val HOME = BottomBarItem(name = "Home", icon = Icons.Default.Home)
        val INFO = BottomBarItem(name = "Info", icon = Icons.Default.Info)
    }
}
