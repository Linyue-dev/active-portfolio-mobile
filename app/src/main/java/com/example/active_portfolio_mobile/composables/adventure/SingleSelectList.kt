package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp

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
                    Text(item)
                }
            }
        }
    }
}