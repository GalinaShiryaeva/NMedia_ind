package ru.netology.nmedia_ind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia_ind.db.AppDb
import ru.netology.nmedia_ind.dto.Post
import ru.netology.nmedia_ind.repository.PostRepository
import ru.netology.nmedia_ind.repository.PostRepositorySQLiteImpl

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

//class PostViewModel(val repository: PostRepository): ViewModel() {
class PostViewModel(application: Application) : AndroidViewModel(application) {

//    private val repository: PostRepository = PostRepositoryFileImpl(application)
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao()
    )

    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
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