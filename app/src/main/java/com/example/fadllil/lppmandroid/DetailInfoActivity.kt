package com.example.fadllil.lppmandroid

import android.Manifest
import android.app.DownloadManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.fadllil.lppmandroid.service.RetroClient
import com.example.fadllil.lppmandroid.service.RetrofitClient
import com.example.lppmandroid.service.ApiService
import kotlinx.android.synthetic.main.activity_detail_info.*
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailInfoActivity : AppCompatActivity() {

    lateinit var apiService : ApiService
    private val STORAGE_PERMISSION_CODE: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info)

        //Init API
        val retrofit = RetroClient.getInstance
        apiService = retrofit.create(ApiService::class.java!!)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Detail Info"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        showDetail()

        btn_download.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                }
                else{
                    startDownloading()
                }
            }
            else{
                startDownloading()
            }
        }
    }

    private fun startDownloading() {
        val url = intent.getStringExtra("url")

        Log.d("Download",url)

        /* val downloadmanager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setTitle("LPPM")
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setVisibleInDownloadsUi(false)
        val file = File(Environment.getExternalStorageDirectory(), "/download.pdf")
        request.setDestinationUri(Uri.fromFile(file))
        downloadmanager.enqueue(request)
        */

        val request = DownloadManager.Request(Uri.parse("http://192.168.43.33/lppm-rest-api/storage/"+url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle("Download")
        request.setDescription("The file is Downloading...")

        Log.d("Download",request.toString())

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager =  getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                            startDownloading()
                }
                else{
                    Toast.makeText(this, "Permission danied!", Toast.LENGTH_LONG).show()
                }
            }
        }
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
