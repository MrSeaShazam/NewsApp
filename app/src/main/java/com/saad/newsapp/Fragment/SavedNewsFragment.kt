package com.saad.newsapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.saad.newsapp.Adapter.NewsAdapter
import com.saad.newsapp.DB.ArticleDataBase
import com.saad.newsapp.Model.Article
import com.saad.newsapp.Model.Source
import com.saad.newsapp.R
import com.saad.newsapp.Repository.Repository
import com.saad.newsapp.ViewModel.SavedNewsViewModel
import com.saad.newsapp.ViewModelFactoryProvider.ViewModelProviderFactory
import com.saad.newsapp.databinding.FragmentSavedNewsBinding

class SavedNewsFragment : Fragment() {
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var viewModel: SavedNewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing data for fragment
        setupUI()
        setupViewModel()
        setupObserver()

        //item click listener
        setupClickListeneners()

        //swipe to delete item callback
        deleteItemCallBack()

    }

    private fun setupUI() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.adapter = newsAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(Repository(ArticleDataBase(requireContext()))))[SavedNewsViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.getAllSavedArticle().observe(viewLifecycleOwner) { articles ->

            renderList(articles)

        }
    }

    private fun renderList(articles: List<Article>) {

        newsAdapter.differ.submitList(articles)

    }

    private fun setupClickListeneners() {

        newsAdapter.setOnItemClickListener {
            goToArticleFragment(it)
        }
    }

    private fun goToArticleFragment(it: Article) {
        /* val bundle = Bundle().apply { putSerializable("article",it) }
         Log.d("article",bundle.toString())*/
        val article = Article(
            it.id?:0,
            it.author ?: "",
            it.content?:"",
            it.description?: "",
            it.publishedAt?: "",
            it.source?: Source(it.source?.id?:"", it.source?.name?:""),
            it.title?:"",
            it.url?:"",
            it.urlToImage?:""
        )
        val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
        findNavController().navigate(action)
    }

    fun deleteItemCallBack(){
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
    }

    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val article = newsAdapter.differ.currentList[position]
            viewModel.deleteArticle(article)

            Snackbar.make(requireView(),"Article Deleted Successfully...", Snackbar.LENGTH_SHORT)
                .setAction("Undo"){
                    viewModel.saveArticle(article)
                }.show()

        }

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

}