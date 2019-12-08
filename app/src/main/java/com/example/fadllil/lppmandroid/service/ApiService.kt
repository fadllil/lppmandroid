package com.example.lppmandroid.service

import android.app.DownloadManager
import android.content.ClipData
import com.example.fadllil.lppmandroid.model.DefaultResponse
import com.example.fadllil.lppmandroid.model.Info
import retrofit2.Call
import com.example.fadllil.lppmandroid.model.LoginResponse
import com.example.fadllil.lppmandroid.model.Penelitian
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
            @Field("username") username:String,
            @Field("password_hash") password:String
    ): Call<LoginResponse>

    @get:GET("penelitian")
    val data:Observable<List<Penelitian>>

    @get:GET("info")
    val data_info:Observable<List<Info>>


    @FormUrlEncoded
    @POST("info/delete")
    fun deleteInfo(
            @Field("id_info") id_info:Int
    ): Call<DefaultResponse>

    @Multipart
    @POST("info/save")
    fun saveInfo(
            @Part("judul_info") judul_info:RequestBody,
            @Part("keterangan_info") keterangan_info:RequestBody,
            @Part file: MultipartBody.Part
    ): Call<DefaultResponse>

    @Multipart
    @POST("info/update")
    fun editInfo(
            @Part("id_info") id_info: RequestBody,
            @Part("judul_info") judul_info:RequestBody,
            @Part("keterangan_info") keterangan_info:RequestBody,
            @Part file: MultipartBody.Part
    ): Call<DefaultResponse>
}