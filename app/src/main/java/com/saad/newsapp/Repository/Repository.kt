package com.saad.newsapp.Repository

import com.saad.newsapp.API.RetrofitInstance
import com.saad.newsapp.DB.ArticleDataBase
import com.saad.newsapp.Model.Article

class Repository(val db: ArticleDataBase) {

    //from api

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    //from local db

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getAllArticles() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}