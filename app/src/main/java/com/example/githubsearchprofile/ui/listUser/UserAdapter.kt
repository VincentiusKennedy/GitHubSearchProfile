package com.example.githubsearchprofile.ui.listUser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubsearchprofile.data.response.UserItems
import com.example.githubsearchprofile.databinding.ItemUserListBinding

class UserAdapter(private val users: List<UserItems>) :
    ListAdapter<UserItems, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {
    private var onUserItemClickListener: OnUserItemClickListener? = null

    class UserViewHolder(val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        holder.apply {
            binding.apply {
                Glide.with(holder.itemView.context)
                    .load(user.avatarUrl)
                    .into(holder.binding.ivUser)
                tvUser.text = user.login
            }
        }
        holder.itemView.setOnClickListener {
            val username = user.login
            onUserItemClickListener?.onUserItemClick(username!!)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserItems>() {
            override fun areItemsTheSame(oldItem: UserItems, newItem: UserItems): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserItems, newItem: UserItems): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setOnUserItemClickListener(listener: OnUserItemClickListener) {
        onUserItemClickListener = listener
    }

    interface OnUserItemClickListener {
        fun onUserItemClick(username: String)
    }
}