package com.justas.weather.app.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.justas.weather.app.theme.AppTypography
import com.justas.weather.core.domain.model.CommonPlace
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuBox(
    items: ImmutableList<CommonPlace>,
    selectedPlace: CommonPlace,
    isExpanded: Boolean,
    textFieldValue: String,
    modifier: Modifier = Modifier,
    onItemSelected: (CommonPlace) -> Unit = {},
    onExpandedChange: (Boolean) -> Unit = {},
    onTextFieldValueChange: (String) -> Unit = {},
) {
    val filteredOptions =
        items.filter { place ->
            place.name
                .contains(
                    other = selectedPlace.name,
                    ignoreCase = true,
                )
        }.take(5)
    Box(
        modifier =
            Modifier
                .then(modifier),
    ) {
        ExposedDropdownMenuBox(
            modifier =
                Modifier
                    .padding(
                        vertical = 32.dp,
                    ),
            expanded = isExpanded,
            onExpandedChange = onExpandedChange,
        ) {
            OutlinedTextField(
                modifier =
                    Modifier
                        .menuAnchor(),
                value = textFieldValue,
                onValueChange = onTextFieldValueChange,
                textStyle = AppTypography.titleLarge,
                singleLine = true,
                isError = filteredOptions.isEmpty(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Down",
                    )
                },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        disabledTextColor = Color.Black,
                        errorTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        disabledBorderColor = Color.Black,
                        errorBorderColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTrailingIconColor = Color.Black,
                        disabledTrailingIconColor = Color.Black,
                        errorTrailingIconColor = Color.Black,
                        unfocusedTrailingIconColor = Color.Black,
                        cursorColor = Color.Black,
                        errorCursorColor = Color.Black,
                    ),
            )
            if (filteredOptions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {
                        // We shouldn't hide the menu when the user enters/removes any character
                    },
                ) {
                    filteredOptions.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item.name,
                                )
                            },
                            onClick = { onItemSelected(item) },
                        )
                    }
                }
            }
        }
    }
}
