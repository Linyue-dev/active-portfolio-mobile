package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A list of items, one of which can be selected at a time.
 * @param list The list of items to be displayed and selected.
 * @param selectedItem The item in the list that is selected.
 * @param setSelectedItem The setter function for the selected item.
 */
@Composable
fun SingleSelectList(list: List<String>, selectedItem: String, setSelectedItem: (String) -> Unit) {
    Column {
        list.forEach { item ->
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.padding(all = 5.dp),
                    onClick = {
                        setSelectedItem(item)
                    }) {
                    var fontWeight = FontWeight.Normal
                    if (item == selectedItem) {
                        fontWeight = FontWeight.Bold
                    }
                    Text(text = item, fontWeight = fontWeight)
                }
            }
        }
    }
}