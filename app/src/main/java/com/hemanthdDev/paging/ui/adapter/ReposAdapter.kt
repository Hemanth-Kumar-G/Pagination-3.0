package com.hemanthdDev.paging.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hemanthdDev.paging.R
import com.hemanthdDev.paging.databinding.RepoViewItemBinding
import com.hemanthdDev.paging.databinding.SeparatorViewItemBinding
import com.hemanthdDev.paging.ui.UiModel
import com.hemanthdDev.paging.ui.adapter.viewHolder.RepoViewHolder
import com.hemanthdDev.paging.ui.adapter.viewHolder.SeparatorViewHolder
import javax.inject.Inject

class ReposAdapter @Inject constructor() :
    PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem &&
                        oldItem.repo.name == newItem.repo.name) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.desc == newItem.desc)

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel?.let {
            when (it) {
                is UiModel.RepoItem -> (holder as RepoViewHolder).bind(it.repo)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(it.desc)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.repo_view_item) {
            val binding =
                RepoViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RepoViewHolder(binding)
        } else {
            val binding =
                SeparatorViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SeparatorViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.RepoItem -> R.layout.repo_view_item
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

}
