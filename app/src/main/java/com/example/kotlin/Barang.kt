package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Barang : AppCompatActivity() {
    var userid:kotlin.String? = null
    var kategori:com.google.android.material.card.MaterialCardView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barang)
        userid = intent.getStringExtra("userid")
        kategori = findViewById<View>(R.id.kategori) as com.google.android.material.card.MaterialCardView

        kategori!!.setOnClickListener { KirimData()  }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }

    private fun KirimData() {
        val intent = Intent(this@Barang, KategoriBarang::class.java)
        intent.putExtra("userid", userid)
        finish()
        startActivity(intent)
    }
}