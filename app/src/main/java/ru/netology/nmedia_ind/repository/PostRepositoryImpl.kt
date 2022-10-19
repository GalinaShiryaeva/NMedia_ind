package ru.netology.nmedia_ind.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia_ind.api.PostApiServiceHolder
import ru.netology.nmedia_ind.dto.Post

private const val RESPONSE_CODE_SUCCESS = 200

class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostApiServiceHolder.service.getPosts()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(
                    call: Call<List<Post>>, response: Response<List<Post>>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() ?: run {
                        callback.onError(RuntimeException("Null body"))
                        return
                    }

                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Post>) {
        PostApiServiceHolder.service.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(
                    call: Call<Post>, response: Response<Post>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() ?: run {
                        callback.onError(
                            RuntimeException("Error '${response.code()}' with message: '${response.message()}'")
                        )
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun likeByIdAsync(
        id: Long,
        callback: PostRepository.Callback<Post>
    ) {
        PostApiServiceHolder.service.like(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(
                            RuntimeException("Error '${response.code()}' with message: '${response.message()}'")
                        )
                        return
                    }

                    val body = response.body() ?: run {
                        callback.onError(
                            RuntimeException("Error '${response.code()}' with message: '${response.message()}'")
                        )
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun unlikeByIdAsync(
        id: Long,
        callback: PostRepository.Callback<Post>
    ) {
        PostApiServiceHolder.service.unlike(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(
                            RuntimeException("Error '${response.code()}' with message: '${response.message()}'")
                        )
                        return
                    }

                    val body = response.body() ?: run {
                        callback.onError(
                            RuntimeException("Error '${response.code()}' with message: '${response.message()}'")
                        )
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun shareById(id: Long) {
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostApiServiceHolder.service.delete(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    if (response.code() == RESPONSE_CODE_SUCCESS) {
                        callback.onSuccess(Unit)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }
}