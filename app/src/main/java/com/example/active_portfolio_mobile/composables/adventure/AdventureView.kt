package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.SectionType
import com.example.active_portfolio_mobile.viewModels.AdventureSectionUpdateVM

/**
 * A view of an adventure with its sections.
 * Sections which are shown can be optionally filtered by portfolio.
 * @param adventureToView the adventure to be viewed... Shocker, I know.
 * @param modifier the modifier to be applied to the view.
 * @param filterPortfolioId the id of the portfolio by which sections will be filtered.
 * Only sections included in the portfolio will appear. Leaving this empty will show all sections.
 */
@Composable
fun AdventureView(
    adventureToView: Adventure,
    modifier: Modifier = Modifier,
    filterPortfolioId: String = "",
    adventureSectionVM: AdventureSectionUpdateVM = viewModel()
) {
    LaunchedEffect(adventureToView) {
        adventureSectionVM.fetchSections(adventureToView.id, filterPortfolioId)
    }
    val sections = adventureSectionVM.sections

    Card(modifier = modifier) {
        Column {
            Text(adventureToView.title)

            sections.value.forEach { section ->
                DropDownTab(section.label) {
                    when(section.type) {
                        SectionType.TEXT -> TextSectionView(section)
                        SectionType.LINK -> LinkSectionView(section)
                        SectionType.IMAGE -> ImageSectionView(
                            section,
                            viewModel(key = section.id)
                        )
                    }
                }
            }
        }
    }
}