package com.emelyanov.icerockpractice.modules.repos.modules.details.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.emelyanov.icerockpractice.databinding.FragmentRepoDetailsBinding
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.RepositoryInfoViewModel
import com.emelyanov.icerockpractice.shared.domain.utils.supportActionBar
import dagger.hilt.android.AndroidEntryPoint

private const val FIRST_ENTERED_KEY = "firstEnteredKey"

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {
    private lateinit var binding: FragmentRepoDetailsBinding
    private val viewModel: RepositoryInfoViewModel by viewModels()
    private val repoId: String
        get() = requireNotNull(requireArguments().getString(REPO_ID_KEY))

    private var firstEntered = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstEntered = savedInstanceState?.getBoolean(FIRST_ENTERED_KEY) ?: true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(inflater,container,false)

        supportActionBar?.title = repoId

        viewModel.state.observe(viewLifecycleOwner) { state ->
            with(binding) {
                repoProgressbar.visibility = if(state is RepositoryInfoViewModel.State.Loading) View.VISIBLE else View.GONE

                handleStateForDetailsBlock(state)
                handleStateForNoConnectionError(state)
                handleStateForError(state)
            }
        }

        binding.repoConnectionErrorState.retryButton.setOnClickListener { viewModel.loadInfo(repoId) }
        binding.repoErrorState.refreshButton.setOnClickListener { viewModel.loadInfo(repoId) }

        if(firstEntered) {
            viewModel.loadInfo(repoId)
            firstEntered = false
        }

        return binding.root
    }

    private fun handleStateForDetailsBlock(state: RepositoryInfoViewModel.State) {
        with(binding) {
            if(state is RepositoryInfoViewModel.State.Loaded) {
                detailsScrollContainer.visibility = View.VISIBLE
                repoLink.text = state.githubRepo.url
                repoLink.setOnClickListener {
                    val uri = Uri.parse(state.githubRepo.url)
                    Intent(Intent.ACTION_VIEW).apply {
                        data = uri
                        startActivity(this)
                    }
                }

                licenseName.text = state.githubRepo.license
                starsCount.text = state.githubRepo.stargazersCount.toString()
                forksCount.text = state.githubRepo.forksCount.toString()
                watchesCount.text = state.githubRepo.watchersCount.toString()

                readmeProgressbar.visibility = if(state.readmeState is RepositoryInfoViewModel.ReadmeState.Loading) View.VISIBLE else View.GONE
                if(state.readmeState is RepositoryInfoViewModel.ReadmeState.Loaded) {
                    readmeView.visibility = View.VISIBLE
                    readmeView.loadMarkdownText(state.readmeState.markdown)
                } else if(state.readmeState is RepositoryInfoViewModel.ReadmeState.Empty) {
                    readmeView.visibility = View.VISIBLE
                    readmeView.loadMarkdownText("No README.md")
                } else { readmeView.visibility = View.GONE }
            } else { binding.detailsScrollContainer.visibility = View.GONE }
        }
    }

    private fun handleStateForNoConnectionError(state: RepositoryInfoViewModel.State) {
        with(binding) {
            if(state is RepositoryInfoViewModel.State.ConnectionError ||
                state is RepositoryInfoViewModel.State.Loaded &&
                state.readmeState is RepositoryInfoViewModel.ReadmeState.ConnectionError) {
                repoConnectionErrorState.root.visibility = View.VISIBLE
            } else { repoErrorState.root.visibility = View.GONE }
        }
    }

    private fun handleStateForError(state: RepositoryInfoViewModel.State) {
        with(binding) {
            if(state is RepositoryInfoViewModel.State.Error) {
                repoErrorState.root.visibility = View.VISIBLE
                repoErrorState.errorDescription.text = state.error
            } else if(state is RepositoryInfoViewModel.State.Loaded && state.readmeState is RepositoryInfoViewModel.ReadmeState.Error) {
                repoErrorState.root.visibility = View.VISIBLE
                repoErrorState.errorDescription.text = state.readmeState.error
            } else { repoErrorState.root.visibility = View.GONE }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FIRST_ENTERED_KEY, firstEntered)
    }

    companion object {
        private const val REPO_ID_KEY = "repoIdKey"

        fun createArguments(repoId: String): Bundle {
            return bundleOf(REPO_ID_KEY to repoId)
        }
    }
}