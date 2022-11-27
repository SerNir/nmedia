package ru.netology.nmedia.activity

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModeState
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        private const val TEXT_KEY = "TEXT_KEY"
        var Bundle.textArg: String?
            set(value) = putString(TEXT_KEY, value)
            get() = getString(TEXT_KEY)

        private const val POST_ID = "POST_ID"
        var Bundle.longArg: Long
            set(value) = putLong(POST_ID, value)
            get() = getLong(TEXT_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )


        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                if (!post.likedByMe) {
                    viewModel.likeById(post.id)
                } else {
                    viewModel.dislikedById(post.id)
                }
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


        }
        )

        binding.listRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.emptyText.isVisible = state.empty
        }

        viewModel.state.observe(viewLifecycleOwner){state ->
            binding.progress.isVisible = state is FeedModeState.Loading
            binding.swipeRefresh.isRefreshing = state is FeedModeState.Refreshing
          if (state is FeedModeState.Error){
              Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_SHORT)
                  .setAction(R.string.retry_loading) {viewModel.loadPosts()}
                  .show()
          }
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                return@observe
            }

            if (it.content.isNotBlank() == true) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = it.content
                    })
            }
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        viewModel.newerCount.observe(viewLifecycleOwner){
            println("Newer count: $it")
            if (it == 0)
                binding.elevatedButton.visibility = View.GONE
            else
                binding.elevatedButton.visibility = View.VISIBLE

        }
        binding.elevatedButton.setOnClickListener {
            viewModel.loadPosts()
            binding.elevatedButton.visibility = View.GONE
        }


        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPosts()
            binding.swipeRefresh.isRefreshing = false
        }


        return binding.root
    }
}