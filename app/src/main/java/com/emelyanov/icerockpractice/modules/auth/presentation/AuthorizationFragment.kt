package com.emelyanov.icerockpractice.modules.auth.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.databinding.FragmentAuthorizationBinding
import com.emelyanov.icerockpractice.modules.auth.domain.AuthorizationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {
    private lateinit var binding: FragmentAuthorizationBinding

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)

        binding.authButton.setOnClickListener {
            viewModel.authButtonClick()
        }

        return binding.root
    }
}