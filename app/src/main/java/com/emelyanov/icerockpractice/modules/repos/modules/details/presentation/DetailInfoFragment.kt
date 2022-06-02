package com.emelyanov.icerockpractice.modules.repos.modules.details.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.databinding.FragmentRepoDetailsBinding
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.RepositoryInfoViewModel
import com.emelyanov.icerockpractice.modules.repos.modules.details.utils.NavigationConsts
import com.emelyanov.icerockpractice.shared.domain.utils.supportActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {
    private lateinit var binding: FragmentRepoDetailsBinding

    private val repoName: String
        get() = requireNotNull(requireArguments().getString(NavigationConsts.REPO_NAME_KEY))

    private val viewModel: RepositoryInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(inflater,container,false)

        supportActionBar?.title = repoName

        viewModel.state.observe(viewLifecycleOwner) { state ->
            with(binding) {
                repoProgressbar.visibility = if(state is RepositoryInfoViewModel.State.Loading) View.VISIBLE else View.GONE

                handleStateForDetailsBlock(state)
                handleStateForNoConnectionError(state)
                handleStateForError(state)
            }
        }

        binding.repoConnectionErrorState.retryButton.setOnClickListener { viewModel.loadInfo() }
        binding.repoErrorState.refreshButton.setOnClickListener { viewModel.loadInfo() }

        return binding.root
    }

    private fun handleStateForDetailsBlock(state: RepositoryInfoViewModel.State) {
        with(binding) {
            detailsScrollContainer.visibility = if(state is RepositoryInfoViewModel.State.Loaded) View.VISIBLE else View.GONE
            repoLink.text = if(state is RepositoryInfoViewModel.State.Loaded) state.githubRepo.url else null
            repoLink.setOnClickListener {
                if(state is RepositoryInfoViewModel.State.Loaded) viewModel.onLinkClick(state.githubRepo.url)
            }
            licenseName.text = if(state is RepositoryInfoViewModel.State.Loaded) state.githubRepo.license else null
            starsCount.text = if(state is RepositoryInfoViewModel.State.Loaded) state.githubRepo.stargazersCount.toString() else null
            forksCount.text = if(state is RepositoryInfoViewModel.State.Loaded) state.githubRepo.forksCount.toString() else null
            watchesCount.text = if(state is RepositoryInfoViewModel.State.Loaded) state.githubRepo.watchersCount.toString() else null

            if(state is RepositoryInfoViewModel.State.Loaded) {
                readmeProgressbar.visibility = if(state.readmeState is RepositoryInfoViewModel.ReadmeState.Loading) View.VISIBLE else View.GONE
                readmeView.visibility = when (state.readmeState) {
                    is RepositoryInfoViewModel.ReadmeState.Loaded -> View.VISIBLE
                    is RepositoryInfoViewModel.ReadmeState.Empty -> View.VISIBLE
                    else -> View.GONE
                }

                readmeView.loadMarkdownText(
                    when (state.readmeState) {
                        is RepositoryInfoViewModel.ReadmeState.Loaded -> state.readmeState.markdown
                        is RepositoryInfoViewModel.ReadmeState.Empty -> getString(R.string.no_readme_text)
                        else -> null
                    }
                )
            }
        }
    }

    private fun handleStateForNoConnectionError(state: RepositoryInfoViewModel.State) {
        with(binding) {
            repoConnectionErrorState.root.visibility = if(state is RepositoryInfoViewModel.State.ConnectionError ||
                state is RepositoryInfoViewModel.State.Loaded &&
                state.readmeState is RepositoryInfoViewModel.ReadmeState.ConnectionError) {

                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun handleStateForError(state: RepositoryInfoViewModel.State) {
        with(binding) {
            repoErrorState.root.visibility = if(state is RepositoryInfoViewModel.State.Error) {
                View.VISIBLE
            } else if(state is RepositoryInfoViewModel.State.Loaded &&
                state.readmeState is RepositoryInfoViewModel.ReadmeState.Error) {

                View.VISIBLE
            } else {
                View.GONE
            }

            repoErrorState.errorDescription.text = if(state is RepositoryInfoViewModel.State.Error) {
                state.error
            } else if(state is RepositoryInfoViewModel.State.Loaded &&
                state.readmeState is RepositoryInfoViewModel.ReadmeState.Error) {

                state.readmeState.error
            } else {
                null
            }
        }
    }

    companion object {
        fun createArguments(repoOwner: String, repoName: String): Bundle {
            return bundleOf(
                NavigationConsts.REPO_OWNER_KEY to repoOwner,
                NavigationConsts.REPO_NAME_KEY to repoName
            )
        }
    }
}