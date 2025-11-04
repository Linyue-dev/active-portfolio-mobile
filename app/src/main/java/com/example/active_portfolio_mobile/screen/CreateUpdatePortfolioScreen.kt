package com.example.active_portfolio_mobile.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.portfolio.GetAllPortfolio
import com.example.active_portfolio_mobile.composables.portfolio.PortfolioActionButtons
import com.example.active_portfolio_mobile.composables.portfolio.PortfolioFormFields
import com.example.active_portfolio_mobile.composables.portfolio.VisibilitySelector
import com.example.active_portfolio_mobile.data.remote.dto.CreatePortfolioRequest
import com.example.active_portfolio_mobile.data.remote.dto.Portfolio
import com.example.active_portfolio_mobile.data.remote.dto.UpdatePortfolioRequest
import com.example.active_portfolio_mobile.viewModels.SinglePortfolioMV


/**
 * A composable screen for creating a new portfolio or updating an existing one.
 * The screen includes form fields for the portfolio title and description, a visibility selector,
 * action button for Save/Create, and optional delete, and Error messages from the viewModel.
 *
 * If isEditing is true and existingPortfolio is provided, the fields are pre-filled
 * and the user can update or delete the portfolio.
 * If isEditing is false, the user can create a new portfolio.
 *
 * @param isEditing Boolean flag indicating if the screen is in edit mode.
 * @param isLoading Boolean flag indicting if an action is in progress and to not load the button.
 * @param singlePortfolioMV The viewModel handling portfolio operations and state.
 * @param existingPortfolio Optional portfolio object. If it is provided, the screen pre-fills fields for editing.
 */
@Composable
fun CreateOrEditPortfolioScreen(
    isEditing : Boolean,
    isLoading: Boolean,
    singlePortfolioMV: SinglePortfolioMV = viewModel(),
    existingPortfolio: Portfolio? = null
){
    //Local state for form fields.
    var title by remember { mutableStateOf(existingPortfolio?.title ?: "") }
    var description by remember { mutableStateOf(existingPortfolio?.description ?: "") }
    var visibility by remember { mutableStateOf(existingPortfolio?.visibility ?: "private") }


    //Observing error and connection messages for the viewModel
    val errorMessage by singlePortfolioMV.errorMessage.collectAsState()
    val connectionStatus by singlePortfolioMV.connectionStatus.collectAsState()


    Surface(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            //Portfolio title and description input fields
            PortfolioFormFields(
                title = title,
                onTitleChange = { title = it },
                description = description,
                onDescriptionChange = { description = it }
            )
            Spacer(modifier = Modifier.height(12.dp))

            //Visibility selection dropdown
            VisibilitySelector(
                visibility,
                onVisibilityChange = { visibility = it })

            //Display error message if present
            errorMessage?.let {
                    msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            //Action buttons for Save/Create and Delete
            PortfolioActionButtons(
                isEditing, isLoading, onSaveClick = {
                    /*Handles both create and update logic*/
                    if (isEditing && existingPortfolio != null) {
                        val portfolio = UpdatePortfolioRequest(
                            newTitle = title,
                            newDescription = description,
                            newVisibility = visibility.lowercase()
                        )
                        singlePortfolioMV.updatePortfolio(existingPortfolio.id, portfolio)
                    } else {
                        val portfolio = CreatePortfolioRequest(
                            title = title,
                            userId = "68d60237d20ee7d551a9a7d3",
                            description = description,
                            visibility = visibility.lowercase()
                        )
                        singlePortfolioMV.createPortfolio(portfolio)
                    }
                }, onDeleteClick = if (isEditing) {
                    {
                        existingPortfolio?.let {
                            singlePortfolioMV.deletePortfolio(it.id)
                        }
                    }
                } else null
            )

            //Test fetch composable (for debugging) !!!
            GetAllPortfolio()
            //Display connection status if present
            connectionStatus?.let { status ->
                Text(
                    text = status,
                    color = if (status.startsWith("✅")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Button to test the backend connection manually
            Button(onClick = { singlePortfolioMV.testBackendConnection() }) {
                Text("Test Backend Connection")
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true,name = "Create Portfolio Screen")
@Composable
fun PreviewCreatePortfolioScreen(){
    val fakeViewModel = SinglePortfolioMV()

    CreateOrEditPortfolioScreen(
        isEditing = false,
        isLoading = false,
        singlePortfolioMV = fakeViewModel,
        existingPortfolio  = null
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Edit Portfolio Screen")
@Composable
fun PreviewEditPortfolioScreen(){
    val fakeViewModel = SinglePortfolioMV()

    val fakePortfolio = Portfolio(
        id = "12345",
        title = "My Awesome Portfolio",
        createdBy = "68d60237d20ee7d551a9a7d3",
        shareToken = null,
        description = "This is a sample portfolio description for preview.",
        visibility = "Public"
    )

    CreateOrEditPortfolioScreen(
        isEditing = true,
        isLoading = false,
        singlePortfolioMV = fakeViewModel,
        existingPortfolio  = fakePortfolio
    )
}