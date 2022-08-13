package ru.netology.nmedia.activity

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch

import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    private val newPostContract = registerForActivityResult(NewPostActivity.Contract) { text ->
        if (text.isNullOrBlank()) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.error_empty_content),
                Toast.LENGTH_SHORT
            ).show()
            return@registerForActivityResult
        }

        viewModel.changeContentAndSave(text)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val intentChooser =
                    Intent.createChooser(intent, getString(R.string.choose_share_post))
                startActivity(intentChooser)
                viewModel.share(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {

                viewModel.edit(post)
            }

            override fun playVideo(post: Post) {
                val intent = Intent(ACTION_VIEW, Uri.parse(post.video))
                val intentChooser = Intent.createChooser(intent, getString(R.string.play_video))
                startActivity(intentChooser)
            }

        }
        )

        binding.listRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.listRecyclerView.smoothScrollToPosition(0)
                }

            }
        }

        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                return@observe
            }
            newPostContract.launch(it.content)
        }



        binding.add.setOnClickListener {
            newPostContract.launch("")
        }


    }


}