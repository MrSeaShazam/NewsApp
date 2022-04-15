package com.saad.newsapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saad.newsapp.Model.NewsResponse
import com.saad.newsapp.Repository.Repository
import com.saad.newsapp.Util.Resource
import kotlinx.coroutines.launch

class BreakingNewsViewModel(private val repository: Repository): ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews: LiveData<Resource<NewsResponse>>
        get() = _breakingNews
    var breakingNewsResponse: NewsResponse? = null

    var breakingNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            _breakingNews.postValue(Resource.loading(null))
            repository.getBreakingNews(countryCode, breakingNewsPage).let {
                if (it.isSuccessful) {
                    it.body()?.let { resultResponse ->
                        breakingNewsPage++
                        if(breakingNewsResponse == null) {
                            breakingNewsResponse = resultResponse
                        } else {
                            val oldArticles = breakingNewsResponse?.articles
                            val newArticles = resultResponse.articles
                            oldArticles?.addAll(newArticles)
                        }
                        _breakingNews.postValue(Resource.success((breakingNewsResponse ?: resultResponse)))
                    }
                } else _breakingNews.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }



}