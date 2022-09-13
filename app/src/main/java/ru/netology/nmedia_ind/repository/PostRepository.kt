package ru.netology.nmedia_ind.repository

import ru.netology.nmedia_ind.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long): Post
    fun dislikeById(id: Long): Post
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}
