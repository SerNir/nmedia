package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                authorTextView.text = post.author
                publishedTextView.text = post.published
                contentTextView.text = post.content
                likeTextView.text = post.reducingNumber(post.likes)
                shareTextView.text = post.reducingNumber(post.shares)
                viewsTextView.text = post.reducingNumber(post.views)

                binding.likeImageView.setOnClickListener {
                    likeImageView.setImageResource(
                        if (!post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                    )
                    likeTextView.text = post.reducingNumber(post.likes)
                    viewModel.like()
                }

                binding.shareImageView.setOnClickListener {
                    shareTextView.text = post.reducingNumber(post.shares)
                    viewModel.share()
                }
            }
        }
    }
}