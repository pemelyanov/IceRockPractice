package com.emelyanov.icerockpractice.modules.auth.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.emelyanov.icerockpractice.R
import com.emelyanov.icerockpractice.databinding.FragmentAuthBinding
import com.emelyanov.icerockpractice.modules.auth.domain.AuthViewModel
import com.emelyanov.icerockpractice.shared.domain.utils.bindTextTwoWay
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        viewModel.state.observe(viewLifecycleOwner) {
            binding.authButtonProgressbar.visibility = if(it is AuthViewModel.State.Loading) View.VISIBLE else View.GONE
            binding.authButton.isEnabled = it !is AuthViewModel.State.Loading
            binding.authButton.text = if(it is AuthViewModel.State.Loading) "" else getText(R.string.sign_in)
            binding.tokenField.error = if(it is AuthViewModel.State.InvalidInput) it.reason else ""
        }

        binding.authButton.setOnClickListener {
            viewModel.onSignButtonPressed()
        }

        binding.tokenField.editText?.bindTextTwoWay(viewModel.token, this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect {
                    if(it is AuthViewModel.Action.ShowError) {
                        AlertDialog.Builder(requireContext())
                            .setPositiveButton("Ok", DialogInterface.OnClickListener({d,_ -> d.cancel()}))
                            .setMessage(it.message)
                            .setTitle("Error")
                            .create()
                            .show()
                    }
                }
            }
        }
    }
}