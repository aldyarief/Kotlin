package com.example.kotlin

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_barang.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Barang : AppCompatActivity (), OnBarangItemClickListner, OnDeleteItemClickListner {

    val list = ArrayList<DataBarang>()
    var name: String? = null
    var pass: String? = null
    var server_kategori: String? = null
    var userid: String? = null
    var user: String? = null
    var barang: String? = null
    var beli: String? = null
    var jual: String? = null
    var koreksi: String? = null
    var laporan: String? = null
    private var Spinner: Spinner? = null
    private var kategori: ArrayList<String>? = null
    private var result: JSONArray? = null
    var pd: ProgressDialog? = null
    var kategoriid: TextView? = null
    var kategoriclick: TextView? = null
    private var textname: EditText? = null
    private var textharga: EditText? = null
    var buttonsave: MaterialCardView?= null
    var server_url: String? = null
    var server_barang: String? = null
    var action: String? = null
    var nambar: String? = null
    var btnChoose:Button? = null
    private val SELECT_IMAGE = 100
    var bitmap: Bitmap? = null
    var gambarUpload: ImageView? = null
    var imgDecodableString: String? = null

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
        kategoriid = findViewById(R.id.kategori) as TextView
        kategoriclick = findViewById(R.id.kategoriclick) as TextView
        textname = findViewById(R.id.nambar) as EditText
        textharga = findViewById(R.id.harbar) as EditText
        buttonsave = findViewById<View>(R.id.btnSave) as MaterialCardView
        server_url = "http://aldry.agustianra.my.id/nitip/barang.php"
        action= "adddata"
        btnChoose = findViewById(R.id.btnPick)
        AmbilBarang()
        AmbilKategori()
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)


        btnChoose!!.setOnClickListener(View.OnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, SELECT_IMAGE)
        })


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

    private fun simpanData(name: String,harbar: String,katbar: String,userid: String,action: String,nambar: String) {
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
                            var action="adddata"
                            AmbilKategori()
                            AmbilBarang()
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
                    param["action"] = action
                    param["nambar"] = nambar


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
        if (action.equals("deletedata")) {
            builder.setMessage("Yakin akan menghapus data barang?")
            action="deletedata"
        } else {
            builder.setMessage("Yakin anda menyimpan data barang?")
        }
        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){

                DialogInterface.BUTTON_POSITIVE ->
                    if (action.equals("adddata")) {
                        val nambar ="-"
                        simpanData(name,harbar,katbar,userid!!, action!!,nambar!!)
                    } else if (action.equals("editdata")) {
                        EditData(name,harbar,katbar,userid!!,action!!,nambar!!)
                    } else if (action.equals("deletedata")){
                        action="deletedata"
                        DeleteData(name,harbar,katbar,userid!!,action!!,nambar!!)
                    }
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
        val intent = Intent(this, Dashboard::class.java)
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


    private fun AmbilBarang() {
        server_barang = "http://aldry.agustianra.my.id/nitip/ambilbarang.php"
        val requestQueue = Volley.newRequestQueue(this@Barang)
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST, server_barang,
                Response.Listener { response ->
                    Log.d("response", response)
                    hideDialog()
                    var j: JSONObject? = null
                    try {
                        j = JSONObject(response)
                        result = j.getJSONArray("data")
                        getBarang(result!!)
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


    private fun getBarang(j: JSONArray) {
        //Traversing through all the items in the json array
        for (i in 0 until j.length()) {
            try {
                val json = j.getJSONObject(i)
                list.add(DataBarang(json.getString("namabarang"),json.getString("kategori"),json.getString("hargabarang"),json.getString("idkategori"),json.getString("idbarang")))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        //Setting adapter to show the items in the listview
        val adapter = Adapter(list,this,this)
        adapter.notifyDataSetChanged()

        //tampilkan data dalam recycler view
        mRecyclerView.adapter = adapter
    }

    override fun onItemClick(item: DataBarang, position: Int) {
        action = "editdata"
        kategori!!.clear()
        textname!!.setText(item.namabarang)
        nambar = item.idbarang
        textharga!!.setText(item.harbarang)
        kategori!!.add(item.kategoribarang)
        kategoriclick!!.setText(item.idkategori)

        Spinner!!.setAdapter(ArrayAdapter<String>(this@Barang, android.R.layout.simple_spinner_dropdown_item, kategori!!))

    }

    override fun onClick(item: DataBarang, position: Int) {
        action = "deletedata"
        kategori!!.clear()
        textname!!.setText(item.namabarang)
        nambar = item.idbarang
        textharga!!.setText(item.harbarang)
        kategori!!.add(item.kategoribarang)
        kategoriclick!!.setText(item.idkategori)

        Spinner!!.setAdapter(ArrayAdapter<String>(this@Barang, android.R.layout.simple_spinner_dropdown_item, kategori!!))
        showDialog()
    }

    private fun EditData(name: String,harbar: String,katbar: String,userid: String,action: String,nambar: String) {
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
                            list.clear()
                            AmbilKategori()
                            AmbilBarang()
                            var action="adddata"
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
                    param["action"] = action
                    param["nambar"] = nambar

                    return param
                }
            }
        requestQueue.add(stringRequest)
    }

    private fun DeleteData(name: String,harbar: String,katbar: String,userid: String,action: String,nambar: String) {
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
                            list.clear()
                            AmbilKategori()
                            AmbilBarang()
                            var action="adddata"
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
                    param["action"] = action
                    param["nambar"] = nambar

                    return param
                }
            }
        requestQueue.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(
                selectedImage!!,
                filePathColumn, null, null, null
            )
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            val imageView: ImageView = findViewById(R.id.image_view)
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }
}
