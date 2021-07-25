package com.hemanthdDev.paging.ui.adapter.viewHolder

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.hemanthdDev.paging.databinding.ReposLoadStateFooterViewItemBinding

class ReposLoadStateViewHolder constructor(
    private val binding: ReposLoadStateFooterViewItemBinding,
    retry: (() -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry?.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }
}