package ru.netology.nmedia_ind.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia_ind.dto.Post
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PostRepositoryImpl() : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: error("Body is null") }
            .let { gson.fromJson(it, typeToken.type) }
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .execute()
    }

    override fun likeById(id: Long) {
        val posts = getAll()
        val oldPost =
            posts.find { it.id == id } ?: throw Exception("Post with id = $id not found")
        val post = oldPost.copy(likes = oldPost.likes + 1, likedByMe = !oldPost.likedByMe)

        val request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun dislikeById(id: Long) {
        val posts = getAll()
        val oldPost =
            posts.find { it.id == id } ?: throw Exception("Post with id = $id not found")
        val post = oldPost.copy(likes = oldPost.likes - 1, likedByMe = !oldPost.likedByMe)

        val request = Request.Builder()
            .delete(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun shareById(id: Long) {
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }
}