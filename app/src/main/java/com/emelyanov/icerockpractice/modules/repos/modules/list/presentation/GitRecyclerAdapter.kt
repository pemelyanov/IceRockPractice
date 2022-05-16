package com.emelyanov.icerockpractice.modules.repos.modules.list.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emelyanov.icerockpractice.databinding.RepoListItemBinding
import com.emelyanov.icerockpractice.shared.domain.models.Repo

class GitRecyclerAdapter(
    private val onRepoClick: (String) -> Unit
) : RecyclerView.Adapter<RepoItemViewHolder>()  {

    var items: List<Repo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoListItemBinding.inflate(inflater, parent, false)
        return RepoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoItemViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {
            root.setOnClickListener { onRepoClick(item.name) }
            repoTitle.text = item.name
            repoLang.text = item.language
            repoDescription.text = item.description
            repoLang.setTextColor(item.color ?: Color.WHITE)
        }
    }

    override fun getItemCount() = items.size
}