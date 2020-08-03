package com.example.kotlin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView


class Penjualan : AppCompatActivity() {
    var dialog: AlertDialog.Builder? = null
    var dialogView: View? = null
    var KontenView: LinearLayout?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)
        KontenView = findViewById(R.id.konten_view) as LinearLayout
        DialogForm()

    }

    private fun DialogForm() {
        dialog = AlertDialog.Builder (this);
        dialogView = getLayoutInflater().inflate(R.layout.jumlah_penjualan, KontenView, false);
        dialog!!.setView(dialogView);
        dialog!!.setCancelable(true);
        dialog!!.setIcon(R.mipmap.ic_launcher);
        dialog!!.setTitle("Form Biodata");
        KontenView!!.addView(dialogView);
    }
}