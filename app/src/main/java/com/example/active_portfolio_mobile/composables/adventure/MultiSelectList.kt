package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A list of selectable items, many of which can be selected at once.
 * @param list The list of items to be displayed and selected.
 * Display is based on the item's toString() value.
 * @param selectedItems A list of items to be pre-selected.
 * @param displayText A function which returns the string value which is to be displayed in the ui
 * for the item being selected by the user.
 * @param selectItem A function that will be called whenever an item is selected. The item itself
 * is passed as a parameter.
 * @param deselectItem A function that will be called whenever an item is deselected. The item
 * itself is passed as a parameter.
 */
@Composable
fun <T> MultiSelectList(
    list: List<T>,
    selectedItems: List<T>,
    displayText: (T) -> String,
    selectItem: (T) -> Unit,
    deselectItem: (T) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        list.forEach { item ->
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(top = 10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var checkedState by rememberSaveable {
                            mutableStateOf(item in selectedItems)
                        }
                        Text(displayText(item), modifier = Modifier.padding(10.dp))
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectItem(item)
                                } else {
                                    deselectItem(item)
                                }
                                checkedState = !checkedState
                            }
                        )
                    }
                }
            }
        }
    }
}