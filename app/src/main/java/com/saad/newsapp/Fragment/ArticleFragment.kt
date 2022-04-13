package com.saad.newsapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.saad.newsapp.DB.ArticleDataBase
import com.saad.newsapp.Model.Article
import com.saad.newsapp.R
import com.saad.newsapp.Repository.Repository
import com.saad.newsapp.ViewModel.BreakingNewsViewModel
import com.saad.newsapp.ViewModel.SavedNewsViewModel
import com.saad.newsapp.ViewModelFactoryProvider.ViewModelProviderFactory
import com.saad.newsapp.databinding.FragmentArticleBinding
import com.saad.newsapp.databinding.FragmentBreakingNewsBinding


class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: SavedNewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //start loading
        startLoading()

        //initializing data for fragment
        setupUI()
        setupViewModel()

        //click listeners
        setupClickListeneners()


    }


    private fun setupUI() {

        binding.webView.apply {

            val article = args.article
            loadUrl(article.url!!)
            settings.displayZoomControls = true
            settings.allowContentAccess = true

            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.paginationProgressBar.visibility = View.GONE
                }
            }

        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this,
            ViewModelProviderFactory(Repository(ArticleDataBase(requireContext()))))[SavedNewsViewModel::class.java]
    }

    private fun setupClickListeneners() {
        binding.apply {
            fab.setOnClickListener {
                viewModel.saveArticle(args.article)
                Snackbar.make(requireView(), "Article Saved Successfully...", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun startLoading(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
    private fun stopLoading(){
        binding.paginationProgressBar.visibility = View.GONE
    }

}