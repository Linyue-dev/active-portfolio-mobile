package com.example.active_portfolio_mobile.network

import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import retrofit2.Response
import retrofit2.http.GET

interface AdventureService {
    @GET("adventures/all")
    suspend fun getAll() : Response<List<Adventure>>
}