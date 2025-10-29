package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp

/**
 * A list of selectable items, many of which can be selected at once.
 * @param list The list of items to be displayed and selected.
 * Display is based on the item's toString() value.
 * @param selectedItems A list of items to be pre-selected.
 * @param selectItem A function that will be called whenever an item is selected. The item itself
 * is passed as a parameter.
 * @param deselectItem A function that will be called whenever an item is deselected. The item
 * itself is passed as a parameter.
 */
@Composable
fun <T> MultiSelectList(list: List<T>,
                        selectedItems: List<T>,
                        selectItem: (T) -> Unit,
                        deselectItem: (T) -> Unit) {
    Column {
        list.forEach { item ->
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.padding(all = 5.dp)
                ) {
                    Row {
                        var checkedState by rememberSaveable {
                            mutableStateOf(item in selectedItems)
                        }
                        Text(item.toString())
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