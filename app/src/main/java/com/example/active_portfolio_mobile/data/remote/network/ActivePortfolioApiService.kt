package com.example.active_portfolio_mobile.data.remote.network

import com.example.active_portfolio_mobile.network.AdventureSectionService
import com.example.active_portfolio_mobile.network.AdventureService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import kotlin.jvm.java

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
}

private const val BASE_URL = "https://activeportfolio.onrender.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * The retrofit service object used to make requests to the Active Portfolio REST API.
 */
object ActivePortfolioApi {
    val adventure : AdventureService by lazy {
        retrofit.create(AdventureService::class.java)
    }
    val adventureSection : AdventureSectionService by lazy {
        retrofit.create(AdventureSectionService::class.java)
    }
    val portfolio: PortfolioService by lazy{
        retrofit.create(PortfolioService::class.java)
    }
}