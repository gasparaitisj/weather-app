package com.justas.weather.app.home.topbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.justas.weather.app.main.theme.AppTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** A text field that allows the user to type in to filter down options. */
@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun <T> TextFieldMenu(
    modifier: Modifier = Modifier,
    /** The label for the text field */
    label: String,
    /** All the available options. */
    options: List<T>,
    /** The selected option. */
    selectedOption: T?,
    /** When the option is selected via tapping on the dropdown option or typing in the option. */
    onOptionSelected: (T?) -> Unit,
    /** Converts [T] to a string for populating the initial text field value. */
    optionToString: (T) -> String,
    /** Returns the filtered options based on the input. This where you need to implement your search. */
    filteredOptions: (searchInput: String) -> List<T>,
    /** Creates the row for the filtered down option in the menu. */
    optionToDropdownRow: @Composable (T) -> Unit = { option ->
        Text(optionToString(option))
    },
    /** Creates the view when [filteredOptions] returns a empty list. */
    noResultsRow: @Composable () -> Unit = {
        // By default, wrap in a menu item to get the same style
        DropdownMenuItem(
            onClick = {},
            text = {
                Text(
                    "No matches found",
                )
            },
        )
    },
    isLoading: Boolean = true,
    focusRequester: FocusRequester = remember { FocusRequester() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (expanded: Boolean) -> Unit = { expanded ->
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = Color.Black,
                strokeWidth = 2.5.dp,
            )
        } else {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
                tint = Color.Black,
            )
        }
    },
    textFieldColors: TextFieldColors =
        ExposedDropdownMenuDefaults.textFieldColors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Black,
            errorTextColor = Color.Black,
            focusedTextColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            errorCursorColor = Color.Black,
            disabledPlaceholderColor = Color.Black.copy(alpha = 0.3f),
            errorPlaceholderColor = Color.Black.copy(alpha = 0.3f),
            focusedPlaceholderColor = Color.Black.copy(alpha = 0.3f),
            unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.3f),
        ),
    bringIntoViewRequester: BringIntoViewRequester = remember { BringIntoViewRequester() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    // Get our text for the selected option
    val selectedOptionText =
        remember(selectedOption) {
            selectedOption?.let { optionToString(it) }.orEmpty()
        }

    // Default our text input to the selected option
    var textInput by remember(selectedOptionText) {
        mutableStateOf(TextFieldValue(selectedOptionText))
    }

    var dropDownExpanded by remember { mutableStateOf(false) }

    // Update our filtered options everytime our text input changes
    val filteredOptions =
        remember(textInput, dropDownExpanded) {
            when (dropDownExpanded) {
                true -> filteredOptions(textInput.text)
                // Skip filtering when we don't need to
                false -> emptyList()
            }
        }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = dropDownExpanded && !isLoading,
        onExpandedChange = { dropDownExpanded = !dropDownExpanded },
        modifier = modifier,
    ) {
        // Text Input
        TextField(
            value = textInput,
            onValueChange = {
                textInput = it
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .menuAnchor()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        // When only 1 option left when we lose focus, selected it.
                        if (!focusState.isFocused) {
                            // Whenever we lose focus, always hide the dropdown
                            dropDownExpanded = false

                            when (filteredOptions.size) {
                                // Auto select the single option
                                1 ->
                                    if (filteredOptions.first() != selectedOption) {
                                        onOptionSelected(filteredOptions.first())
                                    }
                                // Nothing to we can auto select - reset our text input to the selected value
                                else -> textInput = textInput.copy(selectedOptionText)
                            }
                        } else {
                            // When focused:
                            // Ensure field is visible by scrolling to it
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                            // Show the dropdown right away
                            dropDownExpanded = true
                        }
                    },
            label = {
                Text(
                    text = if (isLoading) "Loading..." else label,
                    style = AppTypography.labelMedium.copy(color = Color.Black),
                )
            },
            readOnly = isLoading,
            placeholder = { Text(text = "Please enter a city...") },
            trailingIcon = { trailingIcon(dropDownExpanded) },
            colors = textFieldColors,
            singleLine = true,
            keyboardOptions =
                keyboardOptions.copy(
                    imeAction =
                        when (filteredOptions.size) {
                            // We will either reset input or auto select the single option
                            0, 1 -> ImeAction.Done
                            // Keyboard will hide to make room for search results
                            else -> ImeAction.Search
                        },
                ),
            keyboardActions =
                KeyboardActions(
                    onAny = {
                        when (filteredOptions.size) {
                            // Remove focus to execute our onFocusChanged effect
                            0, 1 -> focusManager.clearFocus(force = true)
                            // Can't auto select option since we have a list, so hide keyboard to give more room for dropdown
                            else -> keyboardController?.hide()
                        }
                    },
                ),
        )

        // Dropdown
        if (dropDownExpanded) {
            val dropdownOptions =
                remember(textInput) {
                    if (textInput.text.isEmpty()) {
                        options.take(5)
                    } else {
                        filteredOptions(textInput.text).take(5)
                    }
                }

            DropdownMenu(
                modifier =
                    Modifier
                        .width(256.dp),
                expanded = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false },
                properties = PopupProperties(focusable = false),
            ) {
                if (dropdownOptions.isEmpty()) {
                    noResultsRow()
                } else {
                    dropdownOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                dropDownExpanded = false
                                onOptionSelected(option)
                                focusManager.clearFocus(force = true)
                            },
                            text = {
                                optionToDropdownRow(option)
                            },
                        )
                    }
                }
            }
        }
    }
}
