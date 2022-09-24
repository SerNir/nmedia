package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao,
) : PostRepository {
//    private var posts = emptyList<Post>()
//    private var data = MutableLiveData(posts)
//
//    init {
//        posts = dao.getAll()
//        data.value = posts
//    }

    override fun getAll() = Transformations.map(dao.getAll()){ list ->
        list.map {
            Post(it.id, it.author, it.content, it.published, it.likes, it.shares, it.views, it.likedByMe, it.sharedByMe, it.video )
        }

    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun share(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
      dao.save(PostEntity.fromDto(post))
    }

    override fun getPostById(id: Long)=
        dao.getPostById(id)

}