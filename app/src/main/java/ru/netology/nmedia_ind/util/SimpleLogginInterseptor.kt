package ru.netology.nmedia_ind.util

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http2.Http2Reader.Companion.logger
import java.io.IOException
import java.lang.String


class SimpleLoggingInterceptor : Interceptor {
    //    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request: Request = chain.request()
//        LOGGER.info("Intercepted headers: {} from URL: {}", request.headers, request.url)
//        return chain.proceed(request)
//    }
//
//    companion object {
//        private val LOGGER = HttpLoggingInterceptor.Logger
//            SimpleLoggingInterceptor::class.java
//        )
//    }
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        logger.info(
            String.format(
                "Sending request %s on %s%n%s",
                request.url, chain.connection(), request.headers
            )
        )
        val response: Response = chain.proceed(request)
        val t2 = System.nanoTime()
        logger.info(
            String.format(
                "Received response for %s in %.1fms%n%s",
                response.request.url, (t2 - t1) / 1e6, response.headers
            )
        )
        return response
    }
}