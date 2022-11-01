package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        private var url = "http://10.0.2.2:9999"

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

                if (post.attachment != null){
                    attachmentGroup.visibility = View.VISIBLE

                    attachmentTextView.text = post.attachment?.description.toString()

                    Glide.with(binding.attachmentImageView)
                        .load("$url/images/${post.attachment?.url}")
                        .timeout(10_000)
                        .into(binding.attachmentImageView)
                }

                Glide.with(binding.logoImageView)
                    .load("$url/avatars/${post.authorAvatar}")
                    .placeholder(R.drawable.ic_download_24)
                    .error(R.drawable.ic_error_download_24)
                    .circleCrop()
                    .timeout(10_000)
                    .into(binding.logoImageView)
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
