package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.longArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.CardPostBinding

import ru.netology.nmedia.databinding.FragmentPostBinding

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.longArg: Long? by LongArg
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CardPostBinding.inflate(
            inflater,
            container,
            false
        )


        val id = arguments?.getLong("POST_ID")
//        id?.let { viewModel.searchPostById(it) }
        Log.d("show id", "$id")
        val post = id?.let { it -> viewModel.searchPostById(it) }

        viewModel.data.observe(viewLifecycleOwner) {
            Log.d("show", "$post")
            with(binding) {
                authorTextView.text = post?.author
                publishedTextView.text = post?.published
                contentTextView.text = post?.content
                likeImageView.isChecked = post?.likedByMe ?: false
                likeImageView.text = post?.reducingNumber(post.likes)
                shareImageView.text = post?.reducingNumber(post.shares)
                likeImageView.setOnClickListener {
                    post?.id?.let { it1 -> viewModel.likeById(it1) }
                }
                shareImageView.setOnClickListener {
                    post?.let { it1 -> viewModel.share(it1.id) }
                }
                menuImageButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        val inflater: MenuInflater = menuInflater
                        inflater.inflate(R.menu.options_post, menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    post?.let { it1 -> viewModel.removeById(it1.id) }
                                    findNavController().navigate(R.id.action_postFragment_to_feedFragment)
                                    true
                                }
                                R.id.edit -> {
                                    post?.let { it1 -> viewModel.edit(it1) }
                                    true
                                }

                                else -> false
                            }

                        }
                    }.show()
                }
                if (!post?.video.isNullOrBlank()) {
                    binding.videoGroup.visibility = View.VISIBLE
                }


            }
        }

        return binding.root
    }
}

object LongArg : ReadWriteProperty<Bundle, Long?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Long?) {
        value?.let { thisRef.putLong(property.name, it) }
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Long? =
        thisRef.getLong(property.name)

}