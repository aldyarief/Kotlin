package com.example.kotlin

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kotlinx.android.synthetic.main.list_kategori.view.*

import java.util.List;

class KategoriAdapter(private val list:ArrayList<DataKategori>) : RecyclerView.Adapter<KategoriAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_kategori,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.namakategori.text = list?.get(position)?.namakategori




    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}