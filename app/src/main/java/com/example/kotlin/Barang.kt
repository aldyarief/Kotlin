package com.example.kotlin

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.list_barang.view.*


class Barang : AppCompatActivity () {
    var name: String? = null
    var pass:kotlin.String? = null
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
    var kategoriid: TextView? = null
    private var textname: EditText? = null
    private var textharga: EditText? = null
    var buttonsave: com.google.android.material.card.MaterialCardView?= null
    var server_url: String? = null

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
        Spinner= findViewById(R.id.Spinner) as Spinner
        pd = ProgressDialog(this)
        kategori = ArrayList()
        AmbilKategori()
        kategoriid = findViewById(R.id.kategori) as TextView
        textname = findViewById(R.id.nambar) as EditText
        textharga = findViewById(R.id.harbar) as EditText
        buttonsave = findViewById<View>(R.id.btnSave) as com.google.android.material.card.MaterialCardView
        server_url = "http://aldry.agustianra.my.id/nitip/barang.php"

        Spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //this method will execute when we pic an item from the spinner
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Setting the values to textviews for a selected item
                kategoriid!!.setText(getIDkategori(position))
            }

            //When no item is selected this method would execute
            override fun onNothingSelected(parent: AdapterView<*>?) {
                kategoriid!!.setText("")
            }
        }

        buttonsave!!.setOnClickListener {
            val name = textname!!.text.toString().trim { it <= ' ' }
            val harbar = textharga!!.text.toString().trim { it <= ' '}
            val katbar = kategoriid!!.text.toString().trim { it <= ' '}


            if (!name.isEmpty() && !harbar.isEmpty()) {
                showDialog()
            } else if (name.isEmpty()) {
                textname!!.error = "nama barang tidak boleh kosong"
                textname!!.requestFocus()
            } else if (harbar.isEmpty()) {
                textharga!!.error = "harga barang tidak boleh kosong"
                textharga!!.requestFocus()
            }
        }

    }

    private fun simpanData(name: String,harbar: String,katbar: String,userid: String) {
        val requestQueue = Volley.newRequestQueue(this@Barang)
        pd!!.setCancelable(false)
        pd!!.setMessage("Harap Menunggu...")

        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST, server_url,
                Response.Listener { response ->
                    Log.d("response", response)
                    hideDialog()
                    try {
                        val jObject = JSONObject(response)
                        val pesan = jObject.getString("pesan")
                        val hasil = jObject.getString("result")

                        if (hasil.equals("true", ignoreCase = true)) {
                            requestQueue.stop()
                            Toast.makeText(this@Barang, pesan, Toast.LENGTH_SHORT).show()
                            textname!!.text.clear()
                            textharga!!.text.clear()
                            kategori!!.clear()
                            textname!!.requestFocus()
                            AmbilKategori()
                        } else {
                            Toast.makeText(this@Barang, pesan, Toast.LENGTH_SHORT).show()
                            requestQueue.stop()
                            textname!!.text.clear()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@Barang, "Error JSON", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error ->
                    VolleyLog.d("ERROR", error.message)
                    Toast.makeText(this@Barang, error.message, Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param: MutableMap<String, String> =
                        HashMap()
                    param["name"] = name
                    param["harbar"] = harbar
                    param["katbar"] = katbar
                    param["userid"] = userid

                    return param
                }
            }
        requestQueue.add(stringRequest)
    }


    private fun showDialog(){
        // Late initialize an alert dialog object
        lateinit var dialog:AlertDialog
        val name = textname!!.text.toString().trim { it <= ' ' }
        val harbar = textharga!!.text.toString().trim { it <= ' '}
        val katbar = kategoriid!!.text.toString().trim { it <= ' '}

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)
        // Set a title for alert dialog
        builder.setTitle("Master Barang")
        // Set a message for alert dialog
        builder.setMessage("Yakin anda menyimpan data barang?")

        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> simpanData(name,harbar,katbar,userid!!)
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss();
            }
        }

        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)
        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)
        // Initialize the AlertDialog using builder object
        dialog = builder.create()
        // Finally, display the alert dialog
        dialog.show()
    }



    private fun hideDialog() {
        if (pd!!.isShowing) pd!!.dismiss()
    }

    private fun AmbilKategori() {
        server_kategori = "http://aldry.agustianra.my.id/nitip/ambilkategori.php"
        val stringRequest = StringRequest(server_kategori,
            Response.Listener { response ->
                var j: JSONObject? = null
                try {
                    //Parsing the fetched Json String to JSON Object
                    j = JSONObject(response)

                    //Storing the Array of JSON String to our JSON Array
                    result = j.getJSONArray("data")

                    //Calling method getStudents to get the students from the JSON Array
                    getKategori(result!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { })

        //Creating a request queue
        val requestQueue = Volley.newRequestQueue(this@Barang)

        //Adding request to the queue
        requestQueue.add(stringRequest)
    }

    private fun getKategori(j: JSONArray) {
        //Traversing through all the items in the json array
        for (i in 0 until j.length()) {
            try {
                //Getting json object
                val json = j.getJSONObject(i)

                //Adding the name of the student to array list
                kategori!!.add(json.getString("nama"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        //Setting adapter to show the items in the spinner
        Spinner!!.setAdapter(ArrayAdapter<String>(this@Barang, android.R.layout.simple_spinner_dropdown_item, kategori!!))
    }

    private fun getIDkategori(position: Int): String? {
        var idkategori = ""
        try {
            //Getting object of given index
            val json = result!!.getJSONObject(position)

            //Fetching name from that object
            idkategori = json.getString("idkategori")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //Returning the name
        return idkategori
    }


    override fun onBackPressed() {
        startActivity(Intent(this, Dashboard::class.java))
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
    }

    private fun KirimData() {
        val intent = Intent(this@Barang, KategoriBarang::class.java)
        intent.putExtra("userid", userid)
        finish()
        startActivity(intent)
    }

}