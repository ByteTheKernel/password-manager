package com.example.password_manager.presentation.passwords.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.password_manager.R
import com.example.password_manager.databinding.ItemPagingFooterBinding
import com.example.password_manager.utils.*


class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemPagingFooterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: ItemPagingFooterBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            when (loadState) {
                is LoadState.Loading -> {
                    progressBar.show()
                    retryButton.gone()
                    errorMsg.gone()
                }
                is LoadState.Error -> {
                    progressBar.hide()
                    retryButton.show()
                    errorMsg.show()
                    errorMsg.text = loadState.error.localizedMessage ?: "Ошибка загрузки"
                }
                else -> {
                    progressBar.hide()
                    retryButton.gone()
                    errorMsg.gone()
                }
            }
        }
    }
}


