package com.example.kotlin

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.jumlah_penjualan.view.*
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.transition.Slide
import android.transition.TransitionManager
import android.widget.PopupWindow
import androidx.annotation.RequiresApi


class Penjualan : AppCompatActivity() {
    var dialog: AlertDialog.Builder? = null
    var dialogView: View? = null
    var KontenView: LinearLayout?= null
    var button:com.google.android.material.card.MaterialCardView?= null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)
        KontenView = findViewById(R.id.konten_view) as LinearLayout
        button = findViewById<View>(R.id.btnLogin) as com.google.android.material.card.MaterialCardView


    }


    private fun DialogForm() {
        var totalsemua: String? = null
        dialog = AlertDialog.Builder (this);
        dialogView = getLayoutInflater().inflate(R.layout.jumlah_penjualan, KontenView, false);

        KontenView!!.addView(dialogView);

        KontenView!!.menukurang.setOnClickListener{
            var dataqnt = KontenView!!.qnt!!.text.toString().trim { it <= ' ' }
            var total: Int? = null
            var totalsemua: String? = null

            total = dataqnt.toInt()-"1".toInt()
            totalsemua = total.toString()
            KontenView!!.qnt.text= totalsemua
        }

        KontenView!!.menutambah.setOnClickListener{
            var dataqnt = KontenView!!.qnt!!.text.toString().trim { it <= ' ' }
            var total: Int? = null


            total = dataqnt.toInt()+"1".toInt()
            totalsemua = total.toString()
            KontenView!!.qnt.text= totalsemua
        }

        KontenView!!.btnLogin.setOnClickListener{
            totalsemua=KontenView!!.qnt!!.text.toString().trim { it <= ' ' }
            Toast.makeText(this@Penjualan, totalsemua, Toast.LENGTH_SHORT).show()
            KontenView!!.removeView(dialogView)
        }
    }
}