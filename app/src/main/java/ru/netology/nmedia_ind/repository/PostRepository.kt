package ru.netology.nmedia_ind.repository

import ru.netology.nmedia_ind.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: Callback<Post>)
    fun dislikeByIdAsync(id: Long, callback: Callback<Post>)
    fun shareById(id: Long)
    fun removeById(id: Long, callback: Callback<Boolean>)
    fun saveAsync(post: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(post: T)
        fun onError(e: Exception)
    }
}
