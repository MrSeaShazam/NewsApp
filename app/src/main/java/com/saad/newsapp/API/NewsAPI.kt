package com.saad.newsapp.API

import com.saad.newsapp.Model.NewsResponse
import com.saad.newsapp.Util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apiKey:String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        searchQuery: String = "tesla",
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apiKey:String = Constants.API_KEY
    ): Response<NewsResponse>

}