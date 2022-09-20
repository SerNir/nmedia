package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private val empty = Post(
    0,
    "",
    "",
    ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun changeContentAndSave(content: String) {
        edited.value?.let { post ->
            val text = content.trim()
            if (post.content == text) {
                edited.value = edited.value?.copy(content = text)
                return
            }
            edited.value?.let {
                repository.save(it)
            }
            edited.value = empty
        }
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun share(id: Long) = repository.share(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun edit(post: Post) {
        edited.value = post

    }
    fun searchPostById(id:Long):Post? = repository.getPostById(id)
}
