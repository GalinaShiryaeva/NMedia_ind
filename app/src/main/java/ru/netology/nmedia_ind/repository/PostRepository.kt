package ru.netology.nmedia_ind.repository

import ru.netology.nmedia_ind.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, callback: Callback<Post>)
    fun likeByIdAsync(id: Long, callback: Callback<Post>)
    fun unlikeByIdAsync(id: Long, callback: Callback<Post>)
    fun shareById(id: Long)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)

    interface Callback<T> {
        fun onSuccess(post: T)
        fun onError(e: Exception)
    }
}
