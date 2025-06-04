package com.example.password_manager.presentation.password_detail.fragment

import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.password_manager.PasswordManagerApp
import com.example.password_manager.R
import com.example.password_manager.databinding.FragmentPasswordDetailsBinding
import com.example.password_manager.presentation.model.PasswordUiModel
import com.example.password_manager.presentation.password_detail.viewModel.PasswordDetailsState
import com.example.password_manager.presentation.password_detail.viewModel.PasswordDetailsViewModel
import com.example.password_manager.utils.getFaviconUrl
import android.content.ClipboardManager
import android.graphics.Color
import android.text.InputType
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout

class PasswordDetailsFragment : Fragment() {
    private var _binding: FragmentPasswordDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PasswordDetailsViewModel by lazy {
        val app = requireActivity().application as PasswordManagerApp
        val factory = app.appComponent.presentationComponent().create().daggerViewModelFactory()
        ViewModelProvider(this, factory)[PasswordDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val passwordId = arguments?.getLong("id") ?: run {
            Toast.makeText(requireContext(), "Пароль не найден", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        viewModel.loadPassword(passwordId)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PasswordDetailsState.Loading -> showLoading()
                is PasswordDetailsState.Success -> showPassword(state.password)
                is PasswordDetailsState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is PasswordDetailsState.Deleted -> {
                    Toast.makeText(requireContext(), "Пароль удалён", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun showLoading() {
    }

    private fun showPassword(password: PasswordUiModel) = with(binding) {
        Glide.with(iconView.context)
            .load(password.iconUrl)
            .error(R.drawable.ic_password_default)
            .placeholder(R.drawable.ic_password_default)
            .into(iconView)

        titleView.setText(password.title)
        titleLayout.isVisible = password.title.isNotBlank()

        loginView.apply {
            setText(password.login)
            isFocusable = false
            isFocusableInTouchMode = false
            isClickable = true
            setTextColor(Color.BLACK)
            setOnClickListener {
                copyToClipboard(password.login, "Логин скопирован")
            }
        }
        loginLayout.isVisible = password.login.isNotBlank()

        passwordView.apply {
            setText(password.password)
            isFocusable = false
            isFocusableInTouchMode = false
            isClickable = true
            setTextColor(Color.BLACK)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setOnClickListener {
                copyToClipboard(password.password, "Пароль скопирован")
            }
        }
        passwordLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        passwordLayout.isVisible = password.password.isNotBlank()

        urlView.apply {
            setText(password.url.orEmpty())
            isFocusable = false
            isFocusableInTouchMode = false
            isClickable = true
            setTextColor(Color.BLACK)
            setOnClickListener {
                copyToClipboard(password.url, "URL скопирован")
            }
        }
        urlLayout.isVisible = !password.url.isNullOrBlank()

        // Description
        descriptionView.setText(password.description.orEmpty())
        descriptionLayout.isVisible = !password.description.isNullOrBlank()

        // Удалить
        deleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Удалить пароль?")
                .setMessage("Вы уверены, что хотите удалить этот пароль?")
                .setPositiveButton("Удалить") { _, _ ->
                    viewModel.deletePassword(password.id)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
        // Изменить
        editButton.setOnClickListener {
            val bundle = Bundle().apply { putLong("id", password.id) }
            findNavController().navigate(R.id.addEditPasswordFragment, bundle)
        }
    }

    private fun copyToClipboard(text: String?, toastMsg: String) {
        if (!text.isNullOrBlank()) {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("data", text))
            Toast.makeText(requireContext(), toastMsg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
