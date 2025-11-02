package com.example.active_portfolio_mobile.data.remote.network

import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSectionUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdventureSectionService {
    @GET("sections/allFromAdventure/{adventureId}")
    suspend fun getSectionsOfAdventure(
        @Path("adventureId") adventureId: String,
        @Query("filterPortfolio") filterPortfolio: String = ""
    ): Response<List<AdventureSection>>
    @PUT("sections/{id}")
    suspend fun updateSection(
        @Path("id") sectionId: String,
        @Body updatedSection: AdventureSectionUpdateRequest
    ): Response<String>
    @DELETE("sections/{id}")
    suspend fun delete(
        @Path("id") id: String
    ) : Response<AdventureSection>
}