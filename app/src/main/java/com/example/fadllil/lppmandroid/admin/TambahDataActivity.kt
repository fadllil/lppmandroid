package com.example.fadllil.lppmandroid.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.fadllil.lppmandroid.R
import com.example.fadllil.lppmandroid.model.DefaultResponse
import com.example.fadllil.lppmandroid.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_tambah_data.*
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody





class TambahDataActivity : AppCompatActivity() {

    private lateinit var file: File
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_data)

        val actionbar = supportActionBar
        actionbar!!.title = "Tambah Informasi"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        btn_pilih_file.setOnClickListener {
            intent = Intent().setType("application/pdf")
                    .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        btn_simpan.setOnClickListener{

            val judul = edt_judul.text.toString().trim()
            val keterangan = edt_keterangan.text.toString().trim()

            if(judul.isEmpty()){
                edt_judul.error = "Judul required"
                edt_judul.requestFocus()
                return@setOnClickListener
            }

            if(keterangan.isEmpty()){
                edt_keterangan.error = "Keterangan required"
                edt_keterangan.requestFocus()
                return@setOnClickListener
            }

//            if (file.isEmpty()){
//                btn_pilih_file.error = "File required"
//                btn_pilih_file.requestFocus()
//                return@setOnClickListener
//            }

            val requestFile = RequestBody.create(
                    MediaType.parse(contentResolver.getType(uri)),
                    file
            )

            val teksJudul = RequestBody.create(
                    okhttp3.MultipartBody.FORM, judul)

            val teksKeterangan = RequestBody.create(
                    okhttp3.MultipartBody.FORM, keterangan)

            val body = MultipartBody.Part.createFormData("file_info", file.nameWithoutExtension, requestFile)

            RetrofitClient.instance.saveInfo(teksJudul,teksKeterangan,body).enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                    Toast.makeText(applicationContext, t?.message, Toast.LENGTH_LONG).show()
                    Log.d("retrofit", "gagal "+body.toString())
                }

                override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                    Toast.makeText(applicationContext, response?.body()?.message, Toast.LENGTH_LONG).show()
                    intent = Intent(this@TambahDataActivity, HomeAdminActivity::class.java)
                    startActivity(intent)
                    Log.d("retrofit", "berhasil "+response.toString())
                }

            })
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == Activity.RESULT_OK){

            uri = data?.data!!

            //file = File("/storage/17F0-0C17/Download.pdf")
            var url1= data.data.path.replace(":","/")
            var url2=url1.replace("/document/raw","",true)
            file = File(url2)

            Toast.makeText(applicationContext,file.toString(),Toast.LENGTH_SHORT).show();
//            Toast.makeText(applicationContext,uril,Toast.LENGTH_SHORT).show();
//            Toast.makeText(applicationContext,data!!.data.path.toString(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(applicationContext,selectedFile.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
