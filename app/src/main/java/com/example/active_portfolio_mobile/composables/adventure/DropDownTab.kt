package com.example.active_portfolio_mobile.composables.adventure

import android.R.attr.type
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A tab containing a form which begins closed but opens when you click its title.
 * @param name The name to give the tab, will be displayed as the clickable title.
 * @param content The content which will appear when the tab is opened.
*/
@Composable
fun DropDownTab(name: String, content: @Composable (() -> Unit)) {
    var isDisplayed by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth(0.75f).padding(top = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        // The clickable title card. Clicking on it will open the contents.
        Card(modifier = Modifier.fillMaxWidth()
            .clickable(onClick = { isDisplayed = !isDisplayed },
                onClickLabel = "Toggle $name contents"),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = name, textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(vertical = 10.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        // The content is displayed only once the title has been clicked.
        // Clicking again will close it.
        if (isDisplayed) {
            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                content()
            }
        }
    }
}