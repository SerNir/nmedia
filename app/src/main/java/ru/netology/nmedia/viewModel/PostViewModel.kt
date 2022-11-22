package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModeState
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.utils.SingleLiveEvent

private val empty = Post(
    0,
    "Netology",
    "Netology",
    "",
    10L
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(application).postDao())

    val data: LiveData<FeedModel> = repository.posts.map {
        FeedModel(it, it.isEmpty())
    }
    private val _state = MutableLiveData<FeedModeState>()
    val state: LiveData<FeedModeState>
        get() = _state
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        _state.value = FeedModeState.Loading
        try {
            repository.getAllAsync()
            _state.value = FeedModeState.Idle
        } catch (e: Exception) {
            _state.value = FeedModeState.Error
        }
    }

    fun refresh() = viewModelScope.launch {
        _state.value = FeedModeState.Refreshing
        try {
            repository.getAllAsync()
            _state.value = FeedModeState.Idle
        } catch (e: Exception) {
            _state.value = FeedModeState.Error
        }
    }


    fun save() = viewModelScope.launch {
        edited.value?.let {
            repository.saveAsync(it)
            _postCreated.postValue(Unit)
        }
        edited.value = empty
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likeByIdAsync(id)
            _state.value = FeedModeState.Like
        } catch (e: Exception) {
            _state.value = FeedModeState.Error
        }
    }

    fun dislikedById(id: Long) = viewModelScope.launch {
        try {
            repository.dislikeByIdAsync(id)
            _state.value = FeedModeState.Dislike

        } catch (e: Exception) {
            _state.value = FeedModeState.Error
        }
    }

    fun share(id: Long) = repository.share(id)

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeByIdAsync(id)
            _state.value = FeedModeState.DeletePost
        } catch (e: Exception) {
            _state.value = FeedModeState.Error
        }
    }


    fun edit(post: Post) {
        edited.value = post

    }


    fun searchPostById(id: Long): Post? {
//        repository.getPostByIdAsync(id, object : GetCallback<Post> {
//            override fun onSuccess(posts: Post) {
//                _data.value = FeedModel(post = posts)
//            }
//
//            override fun onError(e: Exception) {
//                _data.postValue(FeedModel(error = true))
//            }
//
//        })
        return null
    }
}
