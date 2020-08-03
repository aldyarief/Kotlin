package com.example.kotlin

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kotlinx.android.synthetic.main.list_kategori.view.*
import kotlinx.android.synthetic.main.activity_kategori_barang.view.*
import kotlinx.android.synthetic.main.list_barang.view.*

import java.util.List;

class KategoriAdapter(private val list:ArrayList<DataKategori>,var katedit: OnKategoriItemClickListner, var katdelete: OnKatDeleteItemClickListner) : RecyclerView.Adapter<KategoriAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        lateinit var holder : Holder
        holder = Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_kategori, parent, false))
        return holder
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.namakategori.text = list?.get(position)?.namakategori
        holder.view.katid.text = list?.get(position)?.katid
        holder.initialize(list?.get(position),katedit)
        holder.initialize(list?.get(position),katdelete)

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var namkat = view.namakategori
        var katbar = view.katid
        var menukatdelete = view.menukatdelete
        var menukatedit = view.menukatedit

        fun initialize(itemkat: DataKategori, action:OnKategoriItemClickListner){
            namkat.text = itemkat.namakategori
            katbar.text= itemkat.katid

            menukatedit.setOnClickListener{
                action.onItemClick(itemkat,adapterPosition)
            }
        }

        fun initialize(itemkat: DataKategori, action:OnKatDeleteItemClickListner){
            namkat.text = itemkat.namakategori
            katbar.text= itemkat.katid

            menukatdelete.setOnClickListener{
                action.onClick(itemkat,adapterPosition)
            }

        }
    }

}

interface OnKategoriItemClickListner{
    fun onItemClick(item: DataKategori, position: Int)
}

interface OnKatDeleteItemClickListner{
    fun onClick(item: DataKategori, position: Int)
}