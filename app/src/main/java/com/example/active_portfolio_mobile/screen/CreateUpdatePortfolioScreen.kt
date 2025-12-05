package com.example.active_portfolio_mobile.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.portfolio.PopUpMessage
import com.example.active_portfolio_mobile.composables.portfolio.PortfolioActionButtons
import com.example.active_portfolio_mobile.composables.portfolio.PortfolioFormFields
import com.example.active_portfolio_mobile.composables.portfolio.VisibilitySelector
import com.example.active_portfolio_mobile.data.remote.dto.CreatePortfolioRequest
import com.example.active_portfolio_mobile.data.remote.dto.Portfolio
import com.example.active_portfolio_mobile.data.remote.dto.UpdatePortfolioRequest
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalAuthViewModel
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
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
 * @param existingPortfolio Optional portfolio object. If it is provided, the screen pre-fills fields for editing.
 * @param singlePortfolioMV The viewModel handling portfolio operations and state.
 */
@Composable
fun CreateOrEditPortfolioScreen(
    isEditing : Boolean,
    existingPortfolio: Portfolio? = null,
    singlePortfolioMV: SinglePortfolioMV = viewModel()
){
    //User info
    val authViewModel: AuthViewModel = LocalAuthViewModel.current
    val user = authViewModel.uiState.collectAsStateWithLifecycle().value.user

    //Local state for form fields.
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf("private") }

    LaunchedEffect(existingPortfolio){
        if(isEditing && existingPortfolio!=null ){
            title = existingPortfolio.title
            description = (existingPortfolio.description).toString()
            visibility = existingPortfolio.visibility
        }
    }
    
    //Observing error and connection messages for the viewModel
    val errorMessage by singlePortfolioMV.errorMessage.collectAsState()
    val popupMessage by singlePortfolioMV.popUpMessage.collectAsState()
    val errorCode by singlePortfolioMV.errorCode.collectAsState()
    
    val navController = LocalNavController.current

    MainLayout {
        //The surface for the fields, visibility and action button.
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                val titleError =
                    if (errorCode == 400) errorMessage else null

                //Portfolio title and description input fields
                PortfolioFormFields(
                    title = title,
                    onTitleChange = { title = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    titleError = titleError
                )
                Spacer(modifier = Modifier.height(12.dp))

                //Visibility selection dropdown
                VisibilitySelector(
                    visibility,
                    onVisibilityChange = { visibility = it })

                //Display error message if code isn't 400
                if(errorCode!= 400){
                    errorMessage?.let {
                            msg ->
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.padding(10.dp))
                //Action buttons for Save/Create and Delete
                PortfolioActionButtons(
                    isEditing, onSaveClick = {
                        /*Handles both create and update logic*/
                        if (isEditing && existingPortfolio != null) {
                            /* Handle the update part*/
                            val portfolio = UpdatePortfolioRequest(
                                newTitle = title,
                                newDescription = description,
                                newVisibility = visibility.lowercase()
                            )
                            singlePortfolioMV.updatePortfolio(authViewModel.tokenManager.getToken(), existingPortfolio.id, portfolio)
                        } else {
                            /* Handle the creation part*/
                            val portfolio = CreatePortfolioRequest(
                                title = title,
                                userId = user?.id ?: "",
                                description = description,
                                visibility = visibility.lowercase()
                            )
                            singlePortfolioMV.createPortfolio(authViewModel.tokenManager.getToken(), portfolio)
                        }
                    },
                    /* Handle the delete part*/
                    onDeleteClick = if (isEditing) {
                        {
                            existingPortfolio?.let {
                                singlePortfolioMV.deletePortfolio(authViewModel.tokenManager.getToken(), it.id)
                            }
                        }
                    } else null
                )


                //The pop up message that appears when the user clicks
                //on the action button. If the message is successful
                //return the user to the landing page.
                PopUpMessage(popupMessage) {
                    val isSuccess = popupMessage?.startsWith("SUCCESS:") == true
                    singlePortfolioMV.ClearPopUpMessage()

                    if(isSuccess){
                        //Navigate back
                        navController.navigate(Routes.Main.route){
                            popUpTo(Routes.Main.route) {inclusive = true}
                        }
                    }
                }
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
        singlePortfolioMV = fakeViewModel,
        existingPortfolio   = null
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(name = "Edit Portfolio Screen")
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
        singlePortfolioMV = fakeViewModel,
        existingPortfolio  = fakePortfolio
    )
}
