package com.example.fadllil.lppmandroid

import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.fadllil.lppmandroid.adapter.PenAdapter
import com.example.fadllil.lppmandroid.model.Penelitian
import com.example.fadllil.lppmandroid.service.RetroClient
import com.example.lppmandroid.service.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_penelitian.*
import kotlinx.android.synthetic.main.content_penelitian.*

class PenelitianActivity : AppCompatActivity() {

    lateinit var apiService : ApiService
    lateinit var adapter: PenAdapter
    var compositeDisposable = CompositeDisposable()

    var dataList : MutableList<Penelitian> = ArrayList()
    var searchView : SearchView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penelitian)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Penelitian"

        //Init API
        val retrofit = RetroClient.getInstance
        apiService = retrofit.create(ApiService::class.java!!)

        recycler_view_penelitian.setHasFixedSize(true)
        recycler_view_penelitian.layoutManager = LinearLayoutManager(this)

        adapter = PenAdapter(this, dataList)
        recycler_view_penelitian.adapter = adapter

        fetchData()
    }

    private fun fetchData() {
        compositeDisposable.add(apiService.data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ data -> displayData(data)})
    }

    private fun displayData(data: List<Penelitian>?) {
        dataList.clear()
        dataList.addAll(data!!)
        adapter.notifyDataSetChanged()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.penelitian_menu,menu)

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        return if (id == R.id.action_search){
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView!!.isIconified){
            searchView!!.isIconified = true
            return
        }
        super.onBackPressed()
    }


}
