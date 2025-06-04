package com.example.password_manager.presentation.passwords.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.password_manager.R
import com.example.password_manager.databinding.ItemPasswordBinding
import com.example.password_manager.presentation.passwords.model.PasswordListItem

class PasswordsAdapter(
    private val onClick: (PasswordListItem) -> Unit
) : PagingDataAdapter<PasswordListItem, PasswordsAdapter.PasswordViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val binding = ItemPasswordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PasswordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PasswordViewHolder(private val binding: ItemPasswordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PasswordListItem) = with(binding) {
            passwordTitle.text = item.title

            if (item.iconUrl.isNotBlank()) {
                Log.d("PasswordsAdapter", "iconUrl = ${item.iconUrl}")
                Glide.with(passwordIcon.context)
                    .load(item.iconUrl)
                    .error(R.drawable.ic_password_default)
                    .placeholder(R.drawable.ic_password_default)
                    .into(passwordIcon)
            } else {
                passwordIcon.setImageResource(R.drawable.ic_password_default)
            }

            root.setOnClickListener { onClick(item) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PasswordListItem>() {
            override fun areItemsTheSame(old: PasswordListItem, new: PasswordListItem) = old.id == new.id
            override fun areContentsTheSame(old: PasswordListItem, new: PasswordListItem) = old == new
        }
    }
}

