package com.example.kotlin

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kotlinx.android.synthetic.main.list_barang.view.*

import java.util.List;

class Adapter(private val list:ArrayList<DataBarang>) : RecyclerView.Adapter<Adapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_barang,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.namabarang.text = list?.get(position)?.namabarang
        holder.view.katbarang.text = list?.get(position)?.kategoribarang
        holder.view.harbarang.text = list?.get(position)?.harbarang

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}
