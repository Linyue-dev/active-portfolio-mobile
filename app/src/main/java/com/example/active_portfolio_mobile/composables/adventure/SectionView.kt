package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.viewModels.AdventureSectionImageUpdateVM

/**
 * A view for displaying an Adventure Section of type "text".
 * @param section the section to be shown.
 */
@Composable
fun TextSectionView(section: AdventureSection) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
        .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(section.content, modifier = Modifier.padding(all = 10.dp))
    }
}

/**
 * A view for displaying an Adventure Section of type "link".
 * @param section the section to be shown.
 */
@Composable
fun LinkSectionView(section: AdventureSection) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.fillMaxWidth().padding(all = 10.dp)) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
        ) {
            Text(section.content,
                modifier = Modifier
                    .padding(all = 10.dp)
                    .clickable {
                        try {
                            uriHandler.openUri(section.content)
                        } catch (e: Exception) {
                            println("Invalid or unsupported link: ${section.content}")
                        }
                    },
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textDecoration = TextDecoration.Underline
            )
        }
        if (section.description != null) {
            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(section.description, modifier = Modifier.padding(all = 10.dp))
            }
        }
    }
}

/**
 * A view for displaying an Adventure Section of type "image".
 * @param section the section to be shown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSectionView(
    section: AdventureSection,
    imageSectionVM: AdventureSectionImageUpdateVM = viewModel()
) {
    LaunchedEffect(Unit) {
        imageSectionVM.setSection(section)
    }
    val bitmaps = imageSectionVM.bitmapImages.collectAsStateWithLifecycle()
    val carouselState = rememberCarouselState { bitmaps.value.size }

    Column(modifier = Modifier.fillMaxWidth().padding(all = 10.dp)) {
        HorizontalCenteredHeroCarousel(
            state = carouselState,
            itemSpacing = 10.dp
        ) { index ->
            Image(
                bitmap = bitmaps.value[index].asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.width(500.dp).height(300.dp)
            )
        }
        if (section.description != null) {
            Card(modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(section.description, modifier = Modifier.padding(all = 10.dp))
            }
        }
    }
}