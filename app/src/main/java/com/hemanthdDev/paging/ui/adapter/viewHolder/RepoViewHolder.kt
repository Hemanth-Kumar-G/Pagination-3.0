package com.hemanthdDev.paging.ui.adapter.viewHolder

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hemanthdDev.paging.R
import com.hemanthdDev.paging.data.model.Repo
import com.hemanthdDev.paging.databinding.RepoViewItemBinding

class RepoViewHolder constructor(
    private val binding: RepoViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: Repo?) {
        if (null == repo) {
            val resources = itemView.resources
            binding.apply {
                repoName.text = resources.getString(R.string.loading)
                repoDescription.visibility = View.GONE
                repoLanguage.visibility = View.GONE
                repoStars.text = resources.getString(R.string.unknown)
                repoForks.text = resources.getString(R.string.unknown)
            }
        } else {
            binding.apply {

                repoDescription.visibility =
                    if (repo.description != null) View.VISIBLE else View.GONE
                repoDescription.text = repo.description

                repoName.text = repo.fullName
                repoStars.text = repo.stars.toString()
                repoForks.text = repo.forks.toString()

                // if the language is missing, hide the label and the value
                var languageVisibility = View.GONE

                if (!repo.language.isNullOrEmpty()) {
                    val resources = itemView.context.resources
                    repoLanguage.text = resources.getString(R.string.language, repo.language)
                    languageVisibility = View.VISIBLE
                }
                repoLanguage.visibility = languageVisibility
            }
        }

        binding.root.setOnClickListener {
            repo?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                it.context.startActivity(intent)
            }
        }
    }
}