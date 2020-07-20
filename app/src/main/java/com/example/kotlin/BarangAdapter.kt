package com.example.kotlin

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_barang.view.*
import kotlinx.android.synthetic.main.list_barang.view.*

import java.util.List;

class Adapter(private val list:ArrayList<DataBarang>,var clickListner: OnBarangItemClickListner) : RecyclerView.Adapter<Adapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        lateinit var holder : Holder
        holder = Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_barang,parent,false))
        return holder
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.namabarang.text = list?.get(position)?.namabarang
        holder.view.katbarang.text = list?.get(position)?.kategoribarang
        holder.view.harbarang.text = list?.get(position)?.harbarang
        holder.initialize(list?.get(position),clickListner)
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var nambar = view.namabarang
        var katbar = view.katbarang
        var harbar = view.harbarang
        fun initialize(item: DataBarang, action:OnBarangItemClickListner){
            nambar.text = item.namabarang
            katbar.text= item.kategoribarang
            harbar.text = item.harbarang


            itemView.setOnClickListener{
                action.onItemClick(item,adapterPosition)
            }

        }
    }

}

interface OnBarangItemClickListner{
    fun onItemClick(item: DataBarang, position: Int)
}