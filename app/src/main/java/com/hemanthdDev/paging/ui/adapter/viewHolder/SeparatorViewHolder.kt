package com.hemanthdDev.paging.ui.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.hemanthdDev.paging.databinding.SeparatorViewItemBinding

class SeparatorViewHolder constructor(
    private val binding: SeparatorViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(desc: String) {
        binding.separatorDescription.text = desc
    }
}