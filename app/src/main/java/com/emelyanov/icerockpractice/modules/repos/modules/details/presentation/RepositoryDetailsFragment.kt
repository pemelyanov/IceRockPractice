package com.emelyanov.icerockpractice.modules.repos.modules.details.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.databinding.FragmentAuthorizationBinding
import com.emelyanov.icerockpractice.databinding.FragmentRepoDetailsBinding
import com.emelyanov.icerockpractice.shared.domain.utils.supportActionBar

class RepositoryDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRepoDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(inflater,container,false)

        supportActionBar?.title = "moko-resources"

        return binding.root
    }


}