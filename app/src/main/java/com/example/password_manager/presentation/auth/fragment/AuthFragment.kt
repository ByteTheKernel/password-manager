package com.example.password_manager.presentation.auth.fragment

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.password_manager.PasswordManagerApp
import com.example.password_manager.R
import com.example.password_manager.data.source.datastore.PrefsManager
import com.example.password_manager.data.source.keystore.KeyStoreUtils
import com.example.password_manager.databinding.FragmentAuthBinding
import com.example.password_manager.presentation.auth.viewModel.AuthState
import com.example.password_manager.presentation.auth.viewModel.AuthViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by lazy {
        val app = requireActivity().application as PasswordManagerApp
        val factory = app.appComponent.presentationComponent().create().daggerViewModelFactory()
        ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.titleText.text = "Загрузка..."
                    binding.actionButton.isEnabled = false
                }
                is AuthState.RequireCreate -> {
                    binding.titleText.text = "Придумайте мастер-пароль"
                    binding.actionButton.isEnabled = true
                    binding.actionButton.text = "Установить"
                    binding.passwordInput.error = null

                    binding.actionButton.setOnClickListener {
                        val pass = binding.passwordEdit.text.toString()
                        viewModel.createMaster(pass)
                    }
                }
                is AuthState.RequireAuth -> {
                    binding.titleText.text = "Введите мастер-пароль"
                    binding.actionButton.isEnabled = true
                    binding.actionButton.text = "Войти"
                    binding.passwordInput.error = null

                    binding.actionButton.setOnClickListener {
                        val pass = binding.passwordEdit.text.toString()
                        viewModel.authenticate(pass)
                    }
                }
                is AuthState.Success -> {
                    findNavController().navigate(R.id.action_authFragment_to_passwordsFragment)
                }
                is AuthState.Error -> {
                    binding.passwordInput.error = state.message
                }
            }
        }

        binding.passwordEdit.doAfterTextChanged {
            viewModel.clearError()
        }

        viewModel.checkState()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
