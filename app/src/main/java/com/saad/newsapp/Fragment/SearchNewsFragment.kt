package com.saad.newsapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.saad.newsapp.Adapter.NewsAdapter
import com.saad.newsapp.DB.ArticleDataBase
import com.saad.newsapp.Model.Article
import com.saad.newsapp.Model.NewsResponse
import com.saad.newsapp.Model.Source
import com.saad.newsapp.R
import com.saad.newsapp.Repository.Repository
import com.saad.newsapp.Util.Constants
import com.saad.newsapp.Util.Status
import com.saad.newsapp.ViewModel.BreakingNewsViewModel
import com.saad.newsapp.ViewModel.SearchNewsViewModel
import com.saad.newsapp.ViewModelFactoryProvider.ViewModelProviderFactory
import com.saad.newsapp.databinding.FragmentSavedNewsBinding
import com.saad.newsapp.databinding.FragmentSearchNewsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment() {
    private lateinit var binding: FragmentSearchNewsBinding

    lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: SearchNewsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing data for fragment
        setupUI()
        setupViewModel()
        setupObserver()

        //search a news
        searcAnyNews()

        //click listeners
        setupClickListeneners()
    }


    private fun setupUI() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.adapter = newsAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(Repository(ArticleDataBase(requireContext()))))[SearchNewsViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.searchNews.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    stopLoading()
                    it.data?.let { newsResponse ->
                        renderList(newsResponse) }
                }
                Status.LOADING -> {
                    startLoading()
                }
                Status.ERROR -> {
                    stopLoading()
                    /* //Handle Error
                     dismissLoader()
                     BaseActivity.showMessage(requireContext(), it.message, Toast.LENGTH_LONG)*/
                }
            }
        }
    }

    private fun renderList(newsResponse: NewsResponse) {

        newsAdapter.differ.submitList(newsResponse.articles)

    }

    private fun searcAnyNews() {
        var job: Job? = null
        binding.apply {
            etSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(Constants.SEARCH_NEWS_TIME_DELAY)

                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }

            }
        }
    }

    private fun setupClickListeneners() {

        newsAdapter.setOnItemClickListener {
            goToArticleFragment(it)
        }
    }



    private fun goToArticleFragment(it: Article) {
        val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it)
        findNavController().navigate(action)
    }


    private fun showBottomNavigation(){
        //Show Bottom menu from main Activity
        val bottom_menue = requireActivity().findViewById<View>(R.id.bottom_navigation)
        bottom_menue.visibility = View.VISIBLE
    }
    private fun hideBottomNavigation(){
        //Hide Bottom menu from main Activity
        val bottom_menue = requireActivity().findViewById<View>(R.id.bottom_navigation)
        bottom_menue.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        showBottomNavigation()
    }

    override fun onStop() {
        super.onStop()
        hideBottomNavigation()
    }

    private fun startLoading(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
    private fun stopLoading(){
        binding.paginationProgressBar.visibility = View.GONE
    }

}