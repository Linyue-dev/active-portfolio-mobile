package com.example.active_portfolio_mobile.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.active_portfolio_mobile.Navigation.LocalNavController
import com.example.active_portfolio_mobile.Navigation.Routes


//Display the landing page of the app with the tile and navigation button.
//Users can navigate to the comments page or the About us page from here.
@Composable
fun LandingPage(modifier : Modifier){
    val navController: NavController = LocalNavController.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(
                text = "Active Portfolio",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                //View comment button
                Button(
                    onClick = { navController.navigate(Routes.Comment.route) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ){
                    Text(
                        text = "View comments",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }
                // About Us Button
                Button(
                    onClick = { navController.navigate(Routes.About.route) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = "About us!!",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }
            }

        }
    }
}