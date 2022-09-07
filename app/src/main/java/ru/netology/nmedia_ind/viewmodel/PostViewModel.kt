package ru.netology.nmedia_ind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia_ind.db.AppDb
import ru.netology.nmedia_ind.dto.Post
import ru.netology.nmedia_ind.model.FeedModel
import ru.netology.nmedia_ind.repository.PostRepository
import ru.netology.nmedia_ind.repository.PostRepositoryImpl
import ru.netology.nmedia_ind.util.SingleLiveEvent
import java.lang.Exception
import kotlin.concurrent.thread

val empty = Post(
    0,
    "",
    "",
    "",
    false,
    0,
    0,
    null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

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
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: Exception) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    val edited = MutableLiveData(empty)

    fun likeById(id: Long) {
        thread {
            _data.postValue(FeedModel(loading = true))
            val post = _data.value?.posts.orEmpty().first { it.id == id }
            try {
                if (post.likedByMe == false) {
                    repository.likeById(id)
                } else {
                    repository.dislikeById(id)
                }
                load()
                _data.postValue(FeedModel(loading = false))
            } catch (e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }
    }

    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) {
        thread {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            val posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            _data.postValue(
                _data.value?.copy(posts = posts, empty = posts.isEmpty())
            )
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun save() {
        thread {
            edited.value?.let {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
        }
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