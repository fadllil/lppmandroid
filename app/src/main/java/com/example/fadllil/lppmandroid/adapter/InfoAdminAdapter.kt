package com.example.fadllil.lppmandroid.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.fadllil.lppmandroid.*
import com.example.fadllil.lppmandroid.model.Info

class InfoAdminAdapter(internal var context: Context, internal var itemList:List<Info>): RecyclerView.Adapter<InfoAdminAdapter.MyViewHolder>(), Filterable{

    private lateinit var onCustomItemClickListener : CustomItemClickListener
    internal var filterLisResult : List<Info>
    internal var mContext = context

    init {
        this.filterLisResult = itemList
    }

    fun setCustomItemClickListener(onCustomItemClickListener: CustomItemClickListener){
        this.onCustomItemClickListener = onCustomItemClickListener
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charString: CharSequence?): FilterResults {
                val charSearch = charString.toString()
                if (charSearch.isEmpty()) {
                    filterLisResult = itemList
                }else{
                    val resultList = ArrayList<Info>()
                    for (row in itemList)
                    {
                        if (row.judulInfo!!.toLowerCase().contains(charSearch.toLowerCase()))
                            resultList.add(row)
                    }
                    filterLisResult = resultList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = filterLisResult
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filterLisResult = filterResults!!.values as List<Info>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_data_info,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filterLisResult.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_judul_info.text = filterLisResult.get(position).judulInfo
        holder.txt_tanggal.text = filterLisResult.get(position).createdAt
        holder.itemView.setOnClickListener({onCustomItemClickListener.onItemClicked(filterLisResult[holder.adapterPosition])})
//
//        holder.setOnCustomItemClickListener(object :CustomItemClickListener{
//            override fun onCustomItemClickListener(view: View, pos: Int) {
//                val intent = Intent(mContext, DetailInfoAdminActivity::class.java)
//                mContext.startActivities(intent)
//            }
//        })
    }

    interface CustomItemClickListener{
        fun onItemClicked(info: Info)
    }

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        internal var txt_judul_info:TextView
        internal var txt_tanggal:TextView
        private var customItemClickListener: CustomItemClickListener?=null

        init {
            txt_judul_info = itemView.findViewById(R.id.judul_info)
            txt_tanggal = itemView.findViewById(R.id.tanggal_info)
        }

        fun setOnCustomItemClickListener(itemClickListener: CustomItemClickListener){
            this.customItemClickListener = itemClickListener
        }
//
//        override fun onClick(v: View?) {
//            this.customItemClickListener!!.onCustomItemClickListener(v!!, adapterPosition)
//        }
    }
    private fun Context.startActivities(intent: Intent) {
        return startActivity(intent)
    }
}
