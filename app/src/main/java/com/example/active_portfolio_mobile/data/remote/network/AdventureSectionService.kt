package com.example.active_portfolio_mobile.data.remote.network

import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSectionCreationRequest
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSectionUpdateRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AdventureSectionService {
    @GET("sections/allFromAdventure/{adventureId}")
    suspend fun getSectionsOfAdventure(
        @Path("adventureId") adventureId: String,
        @Query("filterPortfolio") filterPortfolio: String = ""
    ): Response<List<AdventureSection>>

    @POST("sections")
    suspend fun createSection(
        @Body sectionToCreate: AdventureSectionCreationRequest
    ): Response<String>

    @Multipart
    @POST("sections")
    suspend fun createImageSection(
        @Part("label") label: RequestBody,
        @Part("description") description: RequestBody,
        @Part("type") type: RequestBody,
        @Part("adventureId") adventureId: RequestBody,
        @Part contentFiles: List<MultipartBody.Part>
    ): Response<String>

    @PUT("sections/{id}")
    suspend fun updateSection(
        @Path("id") sectionId: String,
        @Body updatedSection: AdventureSectionUpdateRequest
    ): Response<String>

    @Multipart
    @PUT("sections/{id}")
    suspend fun updateImageSection(
        @Path("id") id: String,
        @Part("newLabel") label: RequestBody,
        @Part("newDescription") description: RequestBody,
        @Part newContentFiles: List<MultipartBody.Part>
    ): Response<String>

    @DELETE("sections/{id}")
    suspend fun delete(
        @Path("id") id: String
    ) : Response<AdventureSection>
}