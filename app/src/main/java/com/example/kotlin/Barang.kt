package com.example.kotlin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class Barang : AppCompatActivity() {
    var name: String? = null
    var pass:kotlin.String? = null
    var server_url: String? = null
    var server_kategori: String? = null
    var userid:kotlin.String? = null
    var user:kotlin.String? = null
    var barang:kotlin.String? = null
    var beli:kotlin.String? = null
    var jual:kotlin.String? = null
    var koreksi:kotlin.String? = null
    var laporan:kotlin.String? = null
    private var Spinner: Spinner? = null
    private var kategori: ArrayList<String>? = null
    private var result: JSONArray? = null
    var pd: ProgressDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barang)
        userid = intent.getStringExtra("userid")
        name = intent.getStringExtra("name")
        pass = intent.getStringExtra("pass")
        userid = intent.getStringExtra("userid")
        user = intent.getStringExtra("user")
        barang = intent.getStringExtra("barang")
        beli = intent.getStringExtra("beli")
        jual = intent.getStringExtra("jual")
        koreksi = intent.getStringExtra("koreksi")
        laporan = intent.getStringExtra("laporan")
        server_url = "http://aldry.agustianra.my.id/nitip/ambilkategori.php"
        Spinner= findViewById(R.id.Spinner) as Spinner
        pd = ProgressDialog(this)
        AmbilKategori()

    }


    private fun AmbilKategori() {
        val requestQueue = Volley.newRequestQueue(this@Barang)
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST, server_url,
                Response.Listener { response ->
                    Log.d("response", response)
                    hideDialog()
                    var j: JSONObject? = null
                    try {
                        j = JSONObject(response)
                        result = j.getJSONArray("data")
                        getKategori(result!!)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@Barang, "Error JSON", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, Response.ErrorListener { error ->
                    VolleyLog.d("ERROR", error.message)
                    Toast.makeText(this@Barang, error.message, Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    return HashMap()
                }
            }
        requestQueue.add(stringRequest)
    }


    private fun getKategori(j: JSONArray) {
        //Traversing through all the items in the json array
        for (i in 0 until j.length()) {
            try {
                val json = j.getJSONObject(i)
                kategori!!.add(json.getString("nama"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        //Setting adapter to show the items in the listview
        val arrayAdapter= ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,kategori!!);
        Spinner!!.setAdapter(arrayAdapter)
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

    private fun showDialog() {
        if (!pd!!.isShowing) pd!!.show()
    }


    private fun hideDialog() {
        if (pd!!.isShowing) pd!!.dismiss()
    }

}