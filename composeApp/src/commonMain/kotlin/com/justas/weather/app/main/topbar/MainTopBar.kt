package com.justas.weather.app.main.topbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.justas.weather.app.home.DropDownMenuBox
import com.justas.weather.app.theme.AppTypography
import com.justas.weather.core.domain.model.CommonPlace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    state: MainTopBarState,
    modifier: Modifier = Modifier,
    onItemSelected: (CommonPlace) -> Unit = {},
    onExpandedChange: (Boolean) -> Unit = {},
    onTextFieldValueChange: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    TopAppBar(
        modifier =
            Modifier
                .fillMaxWidth()
                .then(modifier),
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        title = {
            DropDownMenuBox(
                items = state.places,
                selectedPlace = state.selectedPlace,
                isExpanded = state.isDropdownMenuExpanded,
                textFieldValue = state.dropdownMenuTextFieldValue,
                modifier = modifier,
                onItemSelected = onItemSelected,
                onExpandedChange = onExpandedChange,
                onTextFieldValueChange = onTextFieldValueChange,
            )
        },
        actions = { MainRefreshIconButton(onRefresh = onRefresh) },
    )
}

@Composable
private fun MainRefreshIconButton(onRefresh: () -> Unit) {
    IconButton(
        modifier =
            Modifier
                .padding(end = 16.dp),
        onClick = onRefresh,
    ) {
        Icon(
            modifier =
                Modifier
                    .size(28.dp),
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh",
        )
    }
}

@Composable
private fun MainText(text: String?,) {
    if (text == null) return
    Text(
        modifier =
            Modifier
                .padding(start = 16.dp),
        text = text,
        style = AppTypography.titleLarge,
        overflow = TextOverflow.Ellipsis,
    )
}
