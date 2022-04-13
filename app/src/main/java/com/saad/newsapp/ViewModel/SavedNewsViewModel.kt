package com.saad.newsapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saad.newsapp.Model.Article
import com.saad.newsapp.Repository.Repository
import kotlinx.coroutines.launch

class SavedNewsViewModel(private val repository: Repository) : ViewModel() {


    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getAllSavedArticle() = repository.getAllArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }
}