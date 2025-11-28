package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * Display a sliding description panel at the bottom of the screen.
 * The panel only appears when "visible" is true and the
 * description isn't blank.
 */
@Composable
fun DescriptionPanel(
   visible: Boolean,
   description: String
){

    //Animate panel visibility with vertical slide-in and slide-out transitions
    AnimatedVisibility(
        visible = visible && description.isNotBlank(),
        enter = slideInVertically {it/2},
        exit = slideOutVertically {it/2}
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            //Description Box!!
            //The surface acts as the container for the description text.
            Surface(
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.BottomCenter)
            ){
                //Displays the actual description
                Text(
                    text = description,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}


