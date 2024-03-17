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

    private var onItemClickListener: ((Password) -> Unit)? = null

    inner class PasswordViewHolder(private val binding: PasswordListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        var item: Password? = null
            private set

        fun bind(password: Password, onClickListener: ((Password) -> Unit)? = null) {

            item = password

            Glide.with(itemView)
                .load(itemView.context.getText(R.string.icon_prefix).toString()
                        + password.passwordSiteUrl
                        + itemView.context.getText(R.string.icon_postfix).toString())
                .error(
                    Glide.with(itemView)
                        .load(itemView.context.getText(R.string.icon_prefix_fallback).toString()
                                + password.passwordSiteUrl
                                + itemView.context.getText(R.string.icon_postfix).toString())
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                )
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.imageViewPasswordImage)
            binding.textViewProductTitle.text = password.passwordSiteName
            itemView.setOnClickListener {
                onClickListener?.invoke(password)
            }
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
        holder.bind(password, onItemClickListener)
    }

    class PasswordDiffCallBack : DiffUtil.ItemCallback<Password>() {
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.passwordId == newItem.passwordId
        }

        override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnItemClickListener(listener: (Password) -> Unit) {
        onItemClickListener = listener
    }
}