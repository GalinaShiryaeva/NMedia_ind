package ru.netology.nmedia_ind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia_ind.dto.Post
import ru.netology.nmedia_ind.model.FeedModel
import ru.netology.nmedia_ind.repository.PostRepository
import ru.netology.nmedia_ind.repository.PostRepositoryImpl
import ru.netology.nmedia_ind.util.SingleLiveEvent
import kotlin.Exception

val empty = Post(
    0,
    "",
    "",
    "",
    "",
    false,
    0,
    0,
    null
)

private const val MAX_COUNT_OF_LOADS = 1

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()
    private var countLoads = 0

    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun load() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(post: List<Post>) {
                _data.value = FeedModel(
                    posts = post,
                    empty = post.isEmpty(),
                    error = false,
                    loading = false
                )
                countLoads = 0
            }

            override fun onError(e: Exception) {
                loadingHandler()
            }
        })
    }

    fun loadingHandler() {
        countLoads++
        println("countLoads = $countLoads")
        if (countLoads == MAX_COUNT_OF_LOADS) {
            _data.value = FeedModel(error = false, loading = false, waiting = true)
            countLoads = 0
        } else {
            _data.value = FeedModel(error = false, loading = true, waiting = true)
            load()
        }
    }

    val edited = MutableLiveData(empty)

    fun likeById(id: Long) {
        val posts = _data.value?.posts.orEmpty()
        _data.value = FeedModel(posts = posts, loading = true)
        val oldPost =
            _data.value?.posts?.find { it.id == id } ?: error("Post not found")
        var newPosts: List<Post> = posts

        if (!oldPost.likedByMe) {
            repository.likeByIdAsync(id, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    newPosts = newPosts
                        .map { if (it.id == post.id) post else it }
                    _data.value = FeedModel(posts = newPosts)
                }

                override fun onError(e: Exception) {
//                    _data.value = FeedModel(error = true)
                    loadingHandler()
                }
            })
        } else {
            repository.unlikeByIdAsync(id, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    newPosts = newPosts
                        .map { if (it.id == post.id) post else it }
                    _data.value = FeedModel(posts = newPosts)
                }

                override fun onError(e: Exception) {
//                    _data.value = FeedModel(error = true)
                    loadingHandler()
                }
            })
        }
        _data.value = FeedModel(loading = false)
    }

    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) {
        // Оптимистичная модель
        val oldPosts = _data.value?.posts.orEmpty()

        repository.removeByIdAsync(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(post: Unit) {
                val posts = oldPosts.filter { it.id != id }
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: Exception) {
                _data.value = _data.value?.copy(posts = oldPosts)
//                _data.value = FeedModel(error = true)
                loadingHandler()
            }
        })
    }

    fun save() {
        val edit = edited.value ?: empty
        repository.saveAsync(edit, object : PostRepository.Callback<Post> {
            override fun onSuccess(post: Post) {
                _postCreated.postValue(Unit)
                edited.value = empty
                val posts = _data.value?.posts.orEmpty()
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: Exception) {
//                _data.value = FeedModel(error = true)
                loadingHandler()
            }
        })
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun editContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}