package ru.eugeneprojects.passwordmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.databinding.PasswordListItemBinding

class PasswordPagingAdapter : PagingDataAdapter<Password, PasswordPagingAdapter.PasswordViewHolder>(PasswordDiffCallBack()) {

    inner class PasswordViewHolder(val binding: PasswordListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(password: Password) {

            Glide.with(itemView)
                .load("yandex.ru/favicon.ico")
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.imageViewPasswordImage)
            binding.textViewProductTitle.text = password.passwordSiteName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        return PasswordViewHolder(
            PasswordListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val password = getItem(position) ?: return
        holder.bind(password)
    }

    class PasswordDiffCallBack : DiffUtil.ItemCallback<Password>() {
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.passwordId == newItem.passwordId
        }

        override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }
    }
}