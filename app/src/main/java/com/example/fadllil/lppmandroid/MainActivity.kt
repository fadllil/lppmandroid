package com.example.fadllil.lppmandroid

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.fadllil.lppmandroid.adapter.InfoAdapter
import com.example.fadllil.lppmandroid.model.Info
import com.example.fadllil.lppmandroid.service.RetroClient
import com.example.lppmandroid.service.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_info.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var apiService : ApiService
    lateinit var adapter: InfoAdapter
    var compositeDisposable = CompositeDisposable()

    var dataList : MutableList<Info> = ArrayList()
    var searchView : SearchView?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Home"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Init API
        val retrofit = RetroClient.getInstance
        apiService = retrofit.create(ApiService::class.java!!)

        recycler_view_info.setHasFixedSize(true)
        recycler_view_info.layoutManager = LinearLayoutManager(this)

        adapter = InfoAdapter(this, dataList)
        recycler_view_info.adapter = adapter


        fetchData()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
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
            R.id.login -> {
                intent = Intent(applicationContext, LoginActivity::class.java)

                startActivity(intent)
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
            R.id.nav_home -> {
                intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_tentang -> {
                intent = Intent(applicationContext, TentangActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_penelitian -> {
                intent = Intent(applicationContext, PenelitianActivity::class.java)
                startActivity(intent)
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showSelectedData(info: Info){
        val detailIntent = Intent(this@MainActivity, DetailInfoActivity::class.java)
        detailIntent.putExtra("id", info.idInfo)
        detailIntent.putExtra("judul", info.judulInfo)
        detailIntent.putExtra("keterangan", info.keteranganInfo)
        detailIntent.putExtra("waktu", info.createdAt)
        detailIntent.putExtra("file", info.fileInfo)
        detailIntent.putExtra("url", info.urlFileInfo)
        startActivity(detailIntent)

    }
}

