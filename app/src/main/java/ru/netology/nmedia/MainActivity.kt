package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будующего",
            content = "Привет это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичка до уверенных профессианалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начить цепочку перемен - http://netolo.gy/fyb",
            published = "19 июля 2022",
            likedByMe = false,
            share = 999,
            like = 999
        )

        with(binding) {
            authorTextView.text = post.author
            publishedTextView.text = post.published
            contentTextView.text = post.content
            likeTextView.text = post.reducingNumber(post.like)
            shareTextView.text = post.reducingNumber(post.share)
            viewsTextView.text = post.reducingNumber(post.views)
            if (post.likedByMe) {
                likeImageView.setImageResource(R.drawable.ic_liked_24)
            }

            likeImageView.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likeImageView.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                if (post.likedByMe) post.like++ else post.like--
                likeTextView.text = post.reducingNumber(post.like)
            }
            shareImageView.setOnClickListener {
                post.share++
                shareTextView.text = post.reducingNumber(post.share)
            }
        }
    }
}