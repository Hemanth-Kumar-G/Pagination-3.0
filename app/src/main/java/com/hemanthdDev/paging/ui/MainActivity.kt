package com.hemanthdDev.paging.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.hemanthdDev.paging.R
import com.hemanthdDev.paging.databinding.ActivityMainBinding
import com.hemanthdDev.paging.ui.adapter.ReposAdapter
import com.hemanthdDev.paging.ui.adapter.ReposLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: ReposAdapter

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.searchRepo.text?.trim().toString())
    }

    private fun initSearch(query: String) {

        binding.apply {
            searchRepo.apply {
                setText(query)
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        updateRepoListFromInput()
                        true
                    } else {
                        false
                    }
                }

                setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        updateRepoListFromInput()
                        true
                    } else {
                        false
                    }
                }
            }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    private fun updateRepoListFromInput() {
        binding.searchRepo.text?.trim().let {
            if (it?.isNotEmpty() == true) {
                search(it.toString())
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        val header = ReposLoadStateAdapter { adapter.retry() }
        binding.list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = ReposLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener {
            val isListEmpty = it.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Show a retry header if there was an error refreshing, and items were previously
            // cached OR default to the default prepend state
            header.loadState = it.mediator?.refresh?.takeIf {
                it is LoadState.Error && adapter.itemCount > 0
            } ?: it.prepend

            // Only show the list if refresh succeeds, either from the the local db or the remote.
            binding.list.isVisible =
                it.source.refresh is LoadState.NotLoading || it.mediator?.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = it.mediator?.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails and there are no items.
            binding.retryButton.isVisible =
                it.mediator?.refresh is LoadState.Error && adapter.itemCount == 0
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = it.source.append as? LoadState.Error
                ?: it.source.prepend as? LoadState.Error
                ?: it.append as? LoadState.Error
                ?: it.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this, "\uD83D\uDE28 Wooops ${it.error}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun showEmptyList(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyList.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Android"
    }
}