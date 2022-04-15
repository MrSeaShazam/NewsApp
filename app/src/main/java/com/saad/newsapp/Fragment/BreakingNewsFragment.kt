package com.saad.newsapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.newsapp.Adapter.NewsAdapter
import com.saad.newsapp.DB.ArticleDataBase
import com.saad.newsapp.Model.Article
import com.saad.newsapp.Model.NewsResponse
import com.saad.newsapp.R
import com.saad.newsapp.Repository.Repository
import com.saad.newsapp.Util.Status
import com.saad.newsapp.ViewModel.BreakingNewsViewModel
import com.saad.newsapp.ViewModelFactoryProvider.ViewModelProviderFactory
import com.saad.newsapp.databinding.FragmentBreakingNewsBinding

class BreakingNewsFragment : Fragment() {
    private lateinit var binding: FragmentBreakingNewsBinding
    lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: BreakingNewsViewModel
    var isScrolling = false
    var totalResults = -1

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val manager = recyclerView.layoutManager as LinearLayoutManager
            val currentItems = manager.childCount
            val totalItems = manager.itemCount
            val scrollOutItems = manager.findFirstVisibleItemPosition()

            if(totalResults > totalItems && isScrolling && (currentItems + scrollOutItems == totalItems))
            {
                isScrolling = false
                viewModel.getBreakingNews("us")
                // call api
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupViewModel()
        setupObserver()

        //click listeners
        setupClickListeneners()
    }


    private fun setupUI() {
        newsAdapter = NewsAdapter()
        binding.apply {
            rvBreakingNews.adapter = newsAdapter
            rvBreakingNews.addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(Repository(ArticleDataBase(requireContext()))))[BreakingNewsViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.breakingNews.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    stopLoading()
                    it.data?.let { newsResponse -> renderList(newsResponse) }
                }
                Status.LOADING -> {
                    startLoading()
                }
                Status.ERROR -> {
                    stopLoading()
                   /* //Handle Error
                    BaseActivity.showMessage(requireContext(), it.message, Toast.LENGTH_LONG)*/
                }
            }
        }
    }

    private fun renderList(newsResponse: NewsResponse) {
        Log.d("listsize",newsResponse.articles.size.toString())
        totalResults = newsResponse.totalResults
        newsAdapter.differ.submitList(newsResponse.articles.toList())

    }

    private fun setupClickListeneners() {

        newsAdapter.setOnItemClickListener {
            goToArticleFragment(it)
        }
    }

    private fun goToArticleFragment(it: Article) {

        val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
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