package com.example.fadllil.lppmandroid.service

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetroClient {
    private var instance:Retrofit?=null

    val getInstance:Retrofit
    get() {
        if (instance == null)
            instance = Retrofit.Builder()
                    .baseUrl("http://192.168.43.33/lppm-rest-api/public/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        return instance!!
    }
}