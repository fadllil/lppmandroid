package com.example.fadllil.lppmandroid.service

import android.util.Base64
import com.example.lppmandroid.service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val AUTH = "Basic "+ Base64.encodeToString("fadllil:123456".toByteArray(),Base64.NO_WRAP)
    private const val BASE_URL = "http://192.168.43.33/lppm-rest-api/public/"

    private val okHttpClient =  OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                        .addHeader("Authorization", AUTH)
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()

    val instance: ApiService by lazy{
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        retrofit.create(ApiService::class.java)
    }
}