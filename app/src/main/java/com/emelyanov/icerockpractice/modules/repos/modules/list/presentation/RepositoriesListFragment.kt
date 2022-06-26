package com.emelyanov.icerockpractice.modules.repos.modules.list.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.emelyanov.icerockpractice.R

import com.emelyanov.icerockpractice.databinding.FragmentRepositoriesBinding
import com.emelyanov.icerockpractice.modules.repos.modules.list.domain.RepositoriesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.contracts.contract

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {
    private lateinit var binding: FragmentRepositoriesBinding

    private val viewModel: RepositoriesListViewModel by viewModels()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoriesBinding.inflate(inflater, container, false)

        val adapter = GitRecyclerAdapter(
            onRepoClick = viewModel::onRepoClick
        )
        binding.reposRecycler.adapter = adapter

        val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
            setDrawable(resources.getDrawable(R.drawable.recycler_divider, requireContext().theme))
        }
        binding.reposRecycler.addItemDecoration(decorator)

        viewModel.state.observe(viewLifecycleOwner) {
            with(binding) {
                reposRecycler.visibility = if(it is RepositoriesListViewModel.State.Loaded) View.VISIBLE else View.GONE

                adapter.items = if(it is RepositoriesListViewModel.State.Loaded) it.repos else emptyList()
                reposProgressbar.visibility = if(it is RepositoriesListViewModel.State.Loading) View.VISIBLE else View.GONE

                reposEmptyState.root.visibility = if(it is RepositoriesListViewModel.State.Empty) View.VISIBLE else View.GONE

                reposConnectionErrorState.root.visibility = if(it is RepositoriesListViewModel.State.ConnectionError) View.VISIBLE else View.GONE

                reposErrorState.root.visibility = if(it is RepositoriesListViewModel.State.Error) View.VISIBLE else View.GONE
                reposErrorState.errorDescription.text = if(it is RepositoriesListViewModel.State.Error) it.error else null

                reposEmptyState.refreshButton.setOnClickListener { viewModel.refresh() }
                reposConnectionErrorState.retryButton.setOnClickListener { viewModel.refresh() }
                reposErrorState.refreshButton.setOnClickListener { viewModel.refresh() }
            }
        }

        return binding.root
    }
}