package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding

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


        viewModel.data.observe(viewLifecycleOwner) {

            val post = id?.let { id -> viewModel.searchPostById(id) }

            with(binding) {
                authorTextView.text = post?.author
                publishedTextView.text = post?.published.toString()
                contentTextView.text = post?.content
                likeImageView.isChecked = post?.likedByMe ?: false
                likeImageView.text = post?.reducingNumber(post.likes)
                shareImageView.text = post?.reducingNumber(post.shares)
                likeImageView.setOnClickListener {
                    post?.id?.let { id -> viewModel.likeById(id) }
                }
                shareImageView.setOnClickListener {

                    post?.let { post ->
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
                }
                menuImageButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        val inflater1: MenuInflater = menuInflater
                        inflater1.inflate(R.menu.options_post, menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    post?.let { it -> viewModel.removeById(it.id) }
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.edit -> {
                                    post?.let { it -> viewModel.edit(it) }
                                    findNavController().navigateUp()
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