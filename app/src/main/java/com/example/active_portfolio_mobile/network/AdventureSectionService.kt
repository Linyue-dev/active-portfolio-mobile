package com.example.active_portfolio_mobile.network

import com.example.active_portfolio_mobile.model.AdventureSection
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AdventureSectionService {
    @GET("sections/allFromAdventure/{adventureId}/{filterPortfolio}")
    suspend fun getSectionsOfAdventure(
        @Path("adventureId") adventureId: String,
        @Path("filterPortfolio") filterPortfolio: String = ""
    ): Response<List<AdventureSection>>
}