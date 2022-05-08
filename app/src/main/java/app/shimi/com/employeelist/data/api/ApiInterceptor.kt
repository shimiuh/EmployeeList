package app.shimi.com.employeelist.data.api

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val httpUrl: HttpUrl = original.url
        val url: HttpUrl = httpUrl.newBuilder().build()
        val request: Request = original.newBuilder().url(url).build()
        //return chain.proceed(request)
        val response = chain.proceed(request)
        val  body = response.peekBody(Long.MAX_VALUE).charStream().readText()
        Log.d("TAG"," in onRequestIntercept end req = $url body = $body")
        return response
    }
}