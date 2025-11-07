package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
    Card {
        Text(section.content)
    }
}

/**
 * A view for displaying an Adventure Section of type "link".
 * @param section the section to be shown.
 */
@Composable
fun LinkSectionView(section: AdventureSection) {
    Column {
        Card {
            Text(section.content)
        }
        if (section.description != null) {
            Card {
                Text(section.description)
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

    Column {
        HorizontalCenteredHeroCarousel(
            state = carouselState,
            itemSpacing = 10.dp
        ) { index ->
            Image(
                bitmap = bitmaps.value[index].asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.width(300.dp).height(200.dp)
            )
        }
        if (section.description != null) {
            Card {
                Text(section.description)
            }
        }
    }
}