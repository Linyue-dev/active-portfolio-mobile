package com.example.active_portfolio_mobile.network

import com.example.active_portfolio_mobile.model.Adventure
import com.example.active_portfolio_mobile.model.AdventureUpdateRequest
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdventureService {
    @GET("adventures/all")
    suspend fun getAll() : Response<List<Adventure>>

    @POST("adventures")
    suspend fun create(@Body adventureToCreate: Adventure) : Response<Adventure>

    @PUT("adventures/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Body updatedAdventure: AdventureUpdateRequest
    ) : Response<Adventure>

    @DELETE("adventures/{id}")
    suspend fun delete(
        @Path("id") id: String
    ) : Response<DeleteAdventureResponseBody>
}

@Serializable
data class DeleteAdventureResponseBody (
    val deletedAdventure: Adventure
    //val deletedSections: List<>
)