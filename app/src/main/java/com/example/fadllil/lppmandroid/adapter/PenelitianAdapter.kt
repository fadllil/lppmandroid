package com.example.fadllil.lppmandroid.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.fadllil.lppmandroid.R
import com.example.fadllil.lppmandroid.model.Penelitian
import kotlinx.android.synthetic.main.item_data_penelitian.view.*

class PenelitianAdapter(private val penelitian: List<Penelitian>) : RecyclerView.Adapter<PenelitianAdapter.ViewHolder>(), Filterable {

    lateinit var listFiltered: List<Penelitian>
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var list: MutableList<Penelitian>
    private lateinit var con: Context


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data:Penelitian)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data_penelitian, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount() : Int {
        return penelitian.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val penelitian = penelitian[position]
        holder.judul_penelitian.text = penelitian.judul
        holder.no_sk_penelitian.text = penelitian.noSk
        holder.penulis_penelitian.text = penelitian.penulis
        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(penelitian)}
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val judul_penelitian : TextView = itemView.judul_pen
        val no_sk_penelitian : TextView = itemView.no_sk_pen
        val penulis_penelitian : TextView = itemView.penulis_pen
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charString: String = constraint.toString()
                if (charString.isEmpty()){
                    listFiltered = list
                }else{
                    val filteredList = ArrayList(penelitian)
                    for (penelitianModel in list) {
                        if (penelitianModel.judul!!.toLowerCase().contains(charString.toLowerCase()) || penelitianModel.idRak!!.equals(charString)) {
                            filteredList.add(penelitianModel)
                        }
                    }
                    listFiltered = filteredList
                }
                var filterResults : FilterResults = FilterResults()
                filterResults.values = listFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFiltered = results!!.values as List<Penelitian>
                notifyDataSetChanged()
            }
        }
    }
}