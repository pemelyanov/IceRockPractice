package com.emelyanov.icerockpractice.modules.repos.modules.list.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.databinding.FragmentRepositoriesBinding
import com.emelyanov.icerockpractice.modules.repos.modules.list.domain.RepositoriesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.contracts.contract

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {
    private lateinit var binding: FragmentRepositoriesBinding

    private val viewModel: RepositoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoriesBinding.inflate(inflater, container, false)

        binding.goToDetailsButton.setOnClickListener {
            viewModel.repoClick()
        }

        return binding.root
    }
}