package com.hfad.search

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

const val API_KEY = "360c2147"

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url: HttpUrl = request.url.newBuilder().addQueryParameter("apiKey", API_KEY).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}