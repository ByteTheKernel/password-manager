package com.example.password_manager.presentation.addedit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.password_manager.PasswordManagerApp
import com.example.password_manager.R
import com.example.password_manager.databinding.FragmentAddEditPasswordBinding
import com.example.password_manager.presentation.addedit.viewModel.AddEditPasswordViewModel


class AddEditPasswordFragment : Fragment() {
    private var _binding: FragmentAddEditPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddEditPasswordViewModel by lazy {
        val app = requireActivity().application as PasswordManagerApp
        val presentationComponent = app.appComponent.presentationComponent().create()
        val factory = presentationComponent.daggerViewModelFactory()
        ViewModelProvider(this, factory)[AddEditPasswordViewModel::class.java]
    }

    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val passwordId = arguments?.getLong("id")
        isEditMode = passwordId != null

        passwordId?.let { viewModel.loadPassword(it) }

        viewModel.uiModel.observe(viewLifecycleOwner) { model ->
            if (binding.editTitle.text.toString() != model.title)
                binding.editTitle.setText(model.title)
            if (binding.editLogin.text.toString() != model.login)
                binding.editLogin.setText(model.login)
            if (binding.editPassword.text.toString() != model.password)
                binding.editPassword.setText(model.password)
            if (binding.editUrl.text.toString() != (model.url ?: ""))
                binding.editUrl.setText(model.url)
            if (binding.editDescription.text.toString() != (model.description ?: ""))
                binding.editDescription.setText(model.description)
        }

        binding.editTitle.doAfterTextChanged { viewModel.updateTitle(it?.toString().orEmpty()) }
        binding.editLogin.doAfterTextChanged { viewModel.updateLogin(it?.toString().orEmpty()) }
        binding.editPassword.doAfterTextChanged { viewModel.updatePassword(it?.toString().orEmpty()) }
        binding.editUrl.doAfterTextChanged { viewModel.updateUrl(it?.toString()) }
        binding.editDescription.doAfterTextChanged { viewModel.updateDescription(it?.toString()) }

        binding.saveButton.setOnClickListener {
            val model = viewModel.uiModel.value
            if (model?.title.isNullOrBlank() || model?.password.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Введите название и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.savePassword()
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Пароль сохранён", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

