package com.justas.weather.app.main.bottombar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.justas.weather.app.main.theme.AppTypography
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BottomBarView(
    items: ImmutableList<BottomBarItem>,
    selectedItem: BottomBarItem,
    modifier: Modifier = Modifier,
    onItemClick: (BottomBarItem) -> Unit = {},
) {
    BottomAppBar(
        modifier =
            Modifier
                .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.name == selectedItem.name,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                modifier = modifier,
                label = {
                    Text(
                        text = item.name,
                        style = AppTypography.labelMedium,
                    )
                },
                alwaysShowLabel = false,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        unselectedTextColor = Color.Black,
                    ),
            )
        }
    }
}
