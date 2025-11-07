package com.example.active_portfolio_mobile.data.remote.network


import com.example.active_portfolio_mobile.data.remote.dto.CreatePortfolioRequest
import com.example.active_portfolio_mobile.data.remote.dto.DeleteResponse
import com.example.active_portfolio_mobile.data.remote.dto.Portfolio
import com.example.active_portfolio_mobile.data.remote.dto.UpdatePortfolioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface PortfolioService {
    //GET - READ
    @GET("portfolio/allPortfolio")
    suspend fun getAll() : Response<List<Portfolio>>

    @GET("portfolio/{portfolioId}")
    suspend fun getPortfolio(@Path("portfolioId") portfolioId: String) : Response<Portfolio>

    @GET("portfolio/users/{userId}")
    suspend fun getAllUsersPortfolio(@Path("userId") userId: String) : Response<List<Portfolio>>

    @GET("portfolio/visibility/{visibility}")
    suspend fun getAllPortfolioByVisibility(@Path("visibility") visibility: String,
                                            @Query("userId") userId: String? = null
    ): Response<List<Portfolio>>

    //POST - CREATE
    @POST("portfolio")
    suspend fun createPortfolio(@Body portfolio: CreatePortfolioRequest) : Response<Portfolio>

    //PUT - UPDATE
    @PUT("portfolio/{portfolioId}")
    suspend fun updatePortfolio(@Path("portfolioId") portfolioId: String, @Body portfolio: UpdatePortfolioRequest): Response<Portfolio>

    //DELETE - DELETE
    @DELETE("portfolio/{portfolioId}")
    suspend fun deletePortfolio(@Path("portfolioId") portfolioId: String) : Response<DeleteResponse>
}