package com.example.password_manager.presentation.passwords.fragment

import com.example.password_manager.presentation.passwords.viewModel.PasswordsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.password_manager.PasswordManagerApp
import com.example.password_manager.R
import com.example.password_manager.databinding.FragmentPasswordsBinding
import com.example.password_manager.presentation.passwords.model.PasswordListItem
import com.example.password_manager.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PasswordsFragment : Fragment() {
    private var _binding: FragmentPasswordsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PasswordsViewModel by lazy {
        val app = requireActivity().application as PasswordManagerApp
        val factory = app.appComponent.presentationComponent().create().daggerViewModelFactory()
        ViewModelProvider(this, factory)[PasswordsViewModel::class.java]
    }


    private lateinit var adapter: PasswordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PasswordsAdapter { item: PasswordListItem ->
            findNavController().navigate(
                R.id.passwordDetailsFragment,
                Bundle().apply { putLong("id", item.id) }
            )
        }

        binding.passwordsRecycler.adapter = adapter.withLoadStateFooter(
            footer = PagingLoadStateAdapter { adapter.retry() }
        )
        binding.passwordsRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.searchEdit.doAfterTextChanged { text ->
            viewModel.setSearchQuery(text?.toString().orEmpty())
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.passwordsPagingFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            when {
                loadState.refresh is LoadState.Loading -> showLoading()
                loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0 -> showEmpty()
                loadState.refresh is LoadState.NotLoading -> showSuccess()
                loadState.refresh is LoadState.Error -> showError((loadState.refresh as LoadState.Error).error.message.orEmpty())
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showLoading() = with(binding) {
        shimmerInclude.shimmer.show()
        shimmerInclude.shimmer.startShimmer()
        passwordsRecycler.gone()
        emptyView.gone()
    }

    private fun showSuccess() = with(binding) {
        shimmerInclude.shimmer.stopShimmer()
        shimmerInclude.shimmer.gone()
        passwordsRecycler.show()
        emptyView.gone()
    }

    private fun showEmpty() = with(binding) {
        shimmerInclude.shimmer.stopShimmer()
        shimmerInclude.shimmer.gone()
        passwordsRecycler.gone()
        emptyView.show()
    }

    private fun showError(message: String) = with(binding) {
        shimmerInclude.shimmer.stopShimmer()
        shimmerInclude.shimmer.gone()
        passwordsRecycler.gone()
        emptyView.show()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

