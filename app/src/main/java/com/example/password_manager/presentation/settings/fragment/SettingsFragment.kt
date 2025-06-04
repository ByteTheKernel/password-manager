package com.example.password_manager.presentation.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.password_manager.PasswordManagerApp
import com.example.password_manager.R
import com.example.password_manager.databinding.FragmentSettingsBinding
import com.example.password_manager.presentation.settings.vewModel.SettingsViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by lazy {
        val app = requireActivity().application as PasswordManagerApp
        val factory = app.appComponent.presentationComponent().create().daggerViewModelFactory()
        ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.changeButton.setOnClickListener {
            val oldPassword = binding.oldPasswordEdit.text?.toString().orEmpty()
            val newPassword = binding.newPasswordEdit.text?.toString().orEmpty()
            val confirmPassword = binding.confirmPasswordEdit.text?.toString().orEmpty()

            if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.changeMasterPassword(oldPassword, newPassword)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SettingsViewModel.State.Loading -> {
                    binding.changeButton.isEnabled = false
                }
                is SettingsViewModel.State.Success -> {
                    Toast.makeText(requireContext(), "Пароль успешно изменён", Toast.LENGTH_SHORT).show()
                    binding.oldPasswordEdit.setText("")
                    binding.newPasswordEdit.setText("")
                    viewModel.reset()
                    binding.changeButton.isEnabled = true
                }
                is SettingsViewModel.State.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    binding.changeButton.isEnabled = true
                }
                else -> binding.changeButton.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
