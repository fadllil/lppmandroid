package com.example.lppmandroid.service

import android.app.DownloadManager
import android.content.ClipData
import com.example.fadllil.lppmandroid.model.Info
import retrofit2.Call
import com.example.fadllil.lppmandroid.model.LoginResponse
import com.example.fadllil.lppmandroid.model.Penelitian
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
            @Field("username") username:String,
            @Field("password_hash") password:String
    ): Call<LoginResponse>

    @POST("login")
    fun usLogin(
            @Field("username") username:String,
            @Field("password_hash") password:String
    ):Observable<LoginResponse>

    @GET("penelitian")
    fun getDataPenelitian():Call<List<Penelitian>>

    @get:GET("penelitian")
    val data:Observable<List<Penelitian>>

    @get:GET("info")
    val data_info:Observable<List<Info>>

    @get:GET("info/download/{id}")
    val downloadManager:DownloadManager

    @POST("info/download/{id}")
    fun downloadFile(
            @Field("id_info") id_info:Int
    ):Call<ResponseBody>
}