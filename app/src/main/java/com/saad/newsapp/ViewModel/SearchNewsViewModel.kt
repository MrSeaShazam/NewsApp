package com.saad.newsapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saad.newsapp.Model.NewsResponse
import com.saad.newsapp.Repository.Repository
import com.saad.newsapp.Util.Resource
import kotlinx.coroutines.launch

class SearchNewsViewModel(private val repository: Repository): ViewModel() {

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>>
        get() = _searchNews

    var searchNewsPage = 1

    init {
        searchNews("us")
    }

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            _searchNews.postValue(Resource.loading(null))
            repository.searchNews(searchQuery, searchNewsPage).let {
                if (it.isSuccessful) {
                    _searchNews.postValue(Resource.success(it.body()))
                } else _searchNews.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }



}