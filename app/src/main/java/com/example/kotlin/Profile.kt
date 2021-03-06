package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class Profile : AppCompatActivity() {
    private var textname: EditText? = null
    private  var textpass: EditText? = null
    var button:com.google.android.material.card.MaterialCardView?= null
    var name: String? = null
    var pass:kotlin.String? = null
    var userid:kotlin.String? = null
    var user:kotlin.String? = null
    var barang:kotlin.String? = null
    var beli:kotlin.String? = null
    var jual:kotlin.String? = null
    var koreksi:kotlin.String? = null
    var laporan:kotlin.String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        name = intent.getStringExtra("name")
        pass = intent.getStringExtra("pass")
        userid = intent.getStringExtra("userid")
        user = intent.getStringExtra("user")
        barang = intent.getStringExtra("barang")
        beli = intent.getStringExtra("beli")
        jual = intent.getStringExtra("jual")
        koreksi = intent.getStringExtra("koreksi")
        laporan = intent.getStringExtra("laporan")
        textname = findViewById<View>(R.id.txtname) as EditText
        textpass = findViewById<View>(R.id.txtpass) as EditText
        button = findViewById<View>(R.id.btnGanti) as com.google.android.material.card.MaterialCardView

        textname!!.setText(name)
        textpass!!.setText(pass)

        button!!.setOnClickListener {
            val intent = Intent(this@Profile, EditUser::class.java)
            intent.putExtra("name", name!!.trim())
            intent.putExtra("pass", pass!!.trim())
            intent.putExtra("userid", userid!!.trim())
            intent.putExtra("user", user!!.trim())
            intent.putExtra("barang", barang!!.trim())
            intent.putExtra("beli", beli!!.trim())
            intent.putExtra("jual", jual!!.trim())
            intent.putExtra("koreksi", koreksi!!.trim())
            intent.putExtra("laporan", laporan!!.trim())
            finish()
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this@Profile, Dashboard::class.java)
        intent.putExtra("name", name!!.trim())
        intent.putExtra("pass", pass!!.trim())
        intent.putExtra("userid", userid!!.trim())
        intent.putExtra("user", user!!.trim())
        intent.putExtra("barang", barang!!.trim())
        intent.putExtra("beli", beli!!.trim())
        intent.putExtra("jual", jual!!.trim())
        intent.putExtra("koreksi", koreksi!!.trim())
        intent.putExtra("laporan", laporan!!.trim())
        finish()
        startActivity(intent)
    }
}