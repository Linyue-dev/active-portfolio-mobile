package com.example.active_portfolio_mobile.data.remote.network

import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.AdventureUpdateRequest
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdventureService {
    @GET("adventures/id/{id}")
    suspend fun getOne(
        @Path("id") id: String
    ) : Response<Adventure>

    @GET("adventures/all")
    suspend fun getAll() : Response<List<Adventure>>

    @GET("adventures/allByUser/{userId}")
    suspend fun getAllByUser(
        @Path("userId") userId: String
    ) : Response<List<Adventure>>

    @GET("adventures/byPortfolio/{portfolioId}")
    suspend fun getAllAdventureFromPortfolio(
        @Path("portfolioId") portfolioId: String
    ) : Response <List<Adventure>>

    @GET("adventures/mostRecent/public/{quantity}")
    suspend fun getRecentPublicAdventures(
        @Path("quantity") quantity: String
    ) : Response <List<Adventure>>
    
    @POST("adventures")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body adventureToCreate: Adventure
    ) : Response<Adventure>

    @PUT("adventures/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body updatedAdventure: AdventureUpdateRequest
    ) : Response<Adventure>

    @DELETE("adventures/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : Response<DeleteAdventureResponseBody>
}

@Serializable
data class DeleteAdventureResponseBody (
    val deletedAdventure: Adventure
    //val deletedSections: List<>
)