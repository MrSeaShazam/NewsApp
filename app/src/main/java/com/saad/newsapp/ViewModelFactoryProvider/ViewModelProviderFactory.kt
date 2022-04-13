package com.saad.newsapp.ViewModelFactoryProvider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saad.newsapp.Repository.Repository
import com.saad.newsapp.ViewModel.BreakingNewsViewModel
import com.saad.newsapp.ViewModel.SavedNewsViewModel
import com.saad.newsapp.ViewModel.SearchNewsViewModel

class ViewModelProviderFactory(val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BreakingNewsViewModel::class.java) -> {
                return BreakingNewsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchNewsViewModel::class.java) -> {
                return SearchNewsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SavedNewsViewModel::class.java) -> {
                return SavedNewsViewModel(repository) as T
            }
            else -> { throw IllegalArgumentException("Unknown class name")}
        }
    }
}