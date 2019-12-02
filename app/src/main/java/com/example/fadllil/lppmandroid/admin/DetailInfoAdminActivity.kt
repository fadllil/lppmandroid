package com.example.fadllil.lppmandroid.admin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.fadllil.lppmandroid.R
import com.example.fadllil.lppmandroid.service.RetroClient
import com.example.lppmandroid.service.ApiService

class DetailInfoAdminActivity : AppCompatActivity() {

    lateinit var apiService : ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info_admin)

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

        val id_info = intent.getIntExtra("id", 0)
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
