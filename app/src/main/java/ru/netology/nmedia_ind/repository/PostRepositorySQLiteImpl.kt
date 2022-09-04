package ru.netology.nmedia_ind.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia_ind.dao.PostDao
import ru.netology.nmedia_ind.dto.Post
import ru.netology.nmedia_ind.entity.PostEntity

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = dao.getAll().map {
        it.map(PostEntity::toDto)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}