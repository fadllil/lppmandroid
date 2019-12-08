package com.example.fadllil.lppmandroid.admin

import android.app.Activity
import android.app.Instrumentation
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.RenderScript
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.fadllil.lppmandroid.R
import com.example.fadllil.lppmandroid.model.DefaultResponse
import com.example.fadllil.lppmandroid.model.Info
import com.example.fadllil.lppmandroid.service.RetroClient
import com.example.fadllil.lppmandroid.service.RetrofitClient
import com.example.lppmandroid.service.ApiService
import kotlinx.android.synthetic.main.activity_detail_info_admin.*
import kotlinx.android.synthetic.main.activity_tambah_data.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailInfoAdminActivity : AppCompatActivity() {

    lateinit var apiService : ApiService
    private lateinit var file: File
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info_admin)

        val id_info = intent.getIntExtra("id", 0)

        //Init API
        val retrofit = RetroClient.getInstance
        apiService = retrofit.create(ApiService::class.java!!)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Detail Info Admin"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        showDetail()

        btn_edit_file.setOnClickListener{
            intent = Intent().setType("application/pdf")
                    .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        btn_edit.setOnClickListener{
            val judul_edit = detail_judul.text.toString().trim()
            val keterangan_edit = detail_keterangan.text.toString().trim()

            if(judul_edit.isEmpty()){
                edt_judul.error = "Judul required"
                edt_judul.requestFocus()
                return@setOnClickListener
            }

            if(keterangan_edit.isEmpty()){
                edt_keterangan.error = "Keterangan required"
                edt_keterangan.requestFocus()
                return@setOnClickListener
            }

            val requestFile = RequestBody.create(
                    MediaType.parse(contentResolver.getType(uri)),
                    file
            )

            val teksId = RequestBody.create(

                    okhttp3.MultipartBody.FORM, id_info.toString())

            val teksJudul = RequestBody.create(
                    okhttp3.MultipartBody.FORM, judul_edit)

            val teksKeterangan = RequestBody.create(
                    okhttp3.MultipartBody.FORM, keterangan_edit)

            val body = MultipartBody.Part.createFormData("file_info", file.nameWithoutExtension, requestFile)

            AlertDialog.Builder(this)
                    .setTitle("Konfirmasi")
                    .setMessage("Ingin Merubah Data Ini ?")
                    .setPositiveButton("EDIT", DialogInterface.OnClickListener { dialogInterface, i ->
                        RetrofitClient.instance.editInfo(teksId, teksJudul, teksKeterangan, body).enqueue(object : Callback<DefaultResponse>{
                            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                                Toast.makeText(applicationContext, t?.message, Toast.LENGTH_LONG).show()
                                Log.d("retrofit", "gagal "+body.toString())
                            }

                            override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                                Toast.makeText(applicationContext, response?.body()?.message, Toast.LENGTH_LONG).show()
                                intent = Intent(this@DetailInfoAdminActivity, HomeAdminActivity::class.java)
                                startActivity(intent)
                                Log.d("retrofit", "berhasil "+response.toString())
                            }

                        })
                    })
                    .setNegativeButton("BATAL",DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .show()
        }

        btn_delete.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Konfirmasi")
                    .setMessage("Hapus data ini ?")
                    .setPositiveButton("HAPUS", DialogInterface.OnClickListener { dialogInterface, i ->
                        delete()
                    })
                    .setNegativeButton("BATAL",DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == Activity.RESULT_OK){
            uri = data?.data!!

            var url1= data.data.path.replace(":","/")
            var url2=url1.replace("/document/raw","",true)
            file = File(url2)

            Toast.makeText(applicationContext,file.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private fun delete(){
        val id_info = intent.getIntExtra("id", 0)

        val loading = ProgressDialog(this)
        loading.setMessage("Menghapus data...")
        loading.show()

        RetrofitClient.instance.deleteInfo(id_info).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                loading.dismiss()
                Toast.makeText(applicationContext, t?.message,Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                loading.dismiss()
                Toast.makeText(applicationContext, response?.body()?.message, Toast.LENGTH_LONG).show()
                intent = Intent(this@DetailInfoAdminActivity, HomeAdminActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun showDetail(){
        var judul: TextView = findViewById(R.id.detail_judul)
        var waktu: TextView = findViewById(R.id.detail_tanggal)
        var keterangan: TextView = findViewById(R.id.detail_keterangan)

        val judulInfo=intent.getStringExtra("judul")
        val tanggalInfo=intent.getStringExtra("waktu")
        val keteranganInfo=intent.getStringExtra("keterangan")

        judul.text=judulInfo
        waktu.text=tanggalInfo
        keterangan.text=keteranganInfo
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
