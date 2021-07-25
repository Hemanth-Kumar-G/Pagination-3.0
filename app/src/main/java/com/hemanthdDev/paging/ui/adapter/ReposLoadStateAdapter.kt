package com.hemanthdDev.paging.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.hemanthdDev.paging.R
import com.hemanthdDev.paging.databinding.ReposLoadStateFooterViewItemBinding
import com.hemanthdDev.paging.ui.adapter.viewHolder.ReposLoadStateViewHolder

class ReposLoadStateAdapter constructor(private val retry: () -> Unit) :
    LoadStateAdapter<ReposLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: ReposLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ReposLoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repos_load_state_footer_view_item, parent, false)
        val binding = ReposLoadStateFooterViewItemBinding.bind(view)
        return ReposLoadStateViewHolder(binding, retry)
    }
}