package com.example.fadllil.lppmandroid.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.fadllil.lppmandroid.R
import com.example.fadllil.lppmandroid.model.Penelitian

class PenAdapter(internal var context: Context, internal var itemList:List<Penelitian>): RecyclerView.Adapter<PenAdapter.MyViewHolder>(), Filterable{

    internal var filterLisResult : List<Penelitian>

    init {
        this.filterLisResult = itemList
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charString: CharSequence?): FilterResults {
                val charSearch = charString.toString()
                if (charSearch.isEmpty()) {
                    filterLisResult = itemList
                }else{
                    val resultList = ArrayList<Penelitian>()
                    for (row in itemList)
                    {
                        if (row.judul!!.toLowerCase().contains(charSearch.toLowerCase()))
                            resultList.add(row)
                    }
                    filterLisResult = resultList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = filterLisResult
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filterLisResult = filterResults!!.values as List<Penelitian>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_data_penelitian,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filterLisResult.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_judul.text = filterLisResult.get(position).judul
        holder.txt_penulis.text = filterLisResult.get(position).penulis
        holder.txt_nosk.text = filterLisResult.get(position).noSk
        holder.txt_rak.text = filterLisResult.get(position).idRak.toString()
        holder.txt_cluster.text = filterLisResult.get(position).namaCluster
    }

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        internal var txt_judul:TextView
        internal var txt_penulis:TextView
        internal var txt_nosk:TextView
        internal var txt_rak:TextView
        internal var txt_cluster:TextView

        init {
            txt_judul = itemView.findViewById(R.id.judul_pen)
            txt_penulis = itemView.findViewById(R.id.penulis_pen)
            txt_nosk = itemView.findViewById(R.id.no_sk_pen)
            txt_rak = itemView.findViewById(R.id.rak_pen)
            txt_cluster = itemView.findViewById(R.id.cluster_pen)
        }
    }

}