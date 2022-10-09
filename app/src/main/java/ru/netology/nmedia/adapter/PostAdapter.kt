package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post


interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun openPost(post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class PostViewHolder(
        private val binding: CardPostBinding,
        private val onInteractionListener: OnInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                authorTextView.text = post.author
                publishedTextView.text = post.published.toString()
                contentTextView.text = post.content
                likeImageView.isChecked = post.likedByMe
                likeImageView.text = post.reducingNumber(post.likes)
                shareImageView.text = post.reducingNumber(post.shares)
                likeImageView.setOnClickListener {
                    onInteractionListener.onLike(post)
                }
                shareImageView.setOnClickListener {
                    onInteractionListener.onShare(post)
                }
                menuImageButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        val inflater: MenuInflater = menuInflater
                        inflater.inflate(R.menu.options_post, menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }

                                else -> false
                            }

                        }
                    }.show()
                }


                binding.contentTextView.setOnClickListener {
                    onInteractionListener.openPost(post)
                }


            }
        }
    }
}

class PostDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}
