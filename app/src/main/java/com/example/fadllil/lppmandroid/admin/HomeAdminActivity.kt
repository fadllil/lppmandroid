package com.example.fadllil.lppmandroid.admin

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.fadllil.lppmandroid.LoginActivity
import com.example.fadllil.lppmandroid.R
import com.example.fadllil.lppmandroid.adapter.InfoAdapter
import com.example.fadllil.lppmandroid.model.Info
import com.example.fadllil.lppmandroid.model.SharedPrefManager
import com.example.fadllil.lppmandroid.service.RetroClient
import com.example.lppmandroid.service.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home_admin.*
import kotlinx.android.synthetic.main.app_bar_home_admin.*
import kotlinx.android.synthetic.main.content_home_admin.*

class HomeAdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var apiService : ApiService
    lateinit var adapter: InfoAdapter
    var compositeDisposable = CompositeDisposable()

    var dataList : MutableList<Info> = ArrayList()
    var searchView : SearchView?=null
    lateinit var loading : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar!!.title = "Home Admin"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Init API
        val retrofit = RetroClient.getInstance
        apiService = retrofit.create(ApiService::class.java!!)

        recycler_view_info_admin.setHasFixedSize(true)
        recycler_view_info_admin.layoutManager = LinearLayoutManager(this)

        adapter = InfoAdapter(this, dataList)
        recycler_view_info_admin.adapter = adapter

        loading = ProgressDialog(this)
        loading.setMessage("Mengambil data...")
        loading.show()

        fetchData()

        fab.setOnClickListener { view ->
            intent = Intent(applicationContext, TambahDataActivity::class.java)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun fetchData() {
        compositeDisposable.add(apiService.data_info
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ data -> displayData(data)})
    }

    private fun displayData(data: List<Info>?) {
        loading.dismiss()
        dataList.clear()
        dataList.addAll(data!!)
        adapter.notifyDataSetChanged()

        adapter.setCustomItemClickListener(object : InfoAdapter.CustomItemClickListener{
            override fun onItemClicked(info: Info) {
                showSelectedData(info)
//                Toast.makeText(applicationContext, info.keteranganInfo, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        if (!searchView!!.isIconified){
            searchView!!.isIconified = true
            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_admin, menu)
        val searchManager = getSystemService(Context. SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Int.MAX_VALUE

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(this)
                        .setTitle("Konfirmasi")
                        .setMessage("Anda Ingin Keluar ?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                            SharedPrefManager.getInstance(applicationContext).clear()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                        })
                        .setNegativeButton("BATAL", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                        })
                        .show()

                return true
            }
            R.id.action_search -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home_admin -> {
                intent = Intent(applicationContext, HomeAdminActivity::class.java)
                startActivity(intent)
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
        super.onStart()

        if(!SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

    private fun showSelectedData(info: Info){
        val detailIntent = Intent(this@HomeAdminActivity, DetailInfoAdminActivity::class.java)
        detailIntent.putExtra("id", info.idInfo)
        detailIntent.putExtra("judul", info.judulInfo)
        detailIntent.putExtra("keterangan", info.keteranganInfo)
        detailIntent.putExtra("waktu", info.createdAt)
        detailIntent.putExtra("file", info.fileInfo)
        startActivity(detailIntent)

    }
}
