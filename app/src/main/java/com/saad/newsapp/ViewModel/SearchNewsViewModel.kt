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
    var searchNewsResponse: NewsResponse? = null
    var oldString: String = "us"

    var searchNewsPage = 1

    init {
        searchNews("us")
    }

    fun searchNews(searchQuery: String) {

        if(!oldString.equals(searchQuery, ignoreCase = true) ){
            searchNewsResponse?.let {
                it.articles.clear()
                searchNewsPage = 1
                oldString = searchQuery
            }
        }

        viewModelScope.launch {
            _searchNews.postValue(Resource.loading(null))
            repository.searchNews(searchQuery, searchNewsPage).let {

                if (it.isSuccessful) {
                    it.body()?.let { resultResponse ->
                        searchNewsPage++
                        if(searchNewsResponse == null) {
                            searchNewsResponse = resultResponse
                        } else {
                            val oldArticles = searchNewsResponse?.articles
                            val newArticles = resultResponse.articles
                            oldArticles?.addAll(newArticles)
                        }
                        _searchNews.postValue(Resource.success((searchNewsResponse ?: resultResponse)))
                    }
                } else _searchNews.postValue(Resource.error(it.errorBody().toString(), null))

            }
        }
    }



}