package com.example.kotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener


class Dashboard : AppCompatActivity() {
    var name: String? = null
    var pass:kotlin.String? = null
    var userid:kotlin.String? = null
    var user:kotlin.String? = null
    var barang:kotlin.String? = null
    var beli:kotlin.String? = null
    var jual:kotlin.String? = null
    var koreksi:kotlin.String? = null
    var laporan:kotlin.String? = null
    var sharedPreferences: SharedPreferences? = null
    val PREFS_FILENAME = "com.example.kotlin"
    var masteruser:LinearLayout?= null
    var logout:LinearLayout?= null
    var masterbarang:LinearLayout?= null
    var lokasi:LinearLayout?= null
    var sampleImages = intArrayOf(
        R.drawable.shoppe3,
        R.drawable.shoppe2,
        R.drawable.shoppe
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        name = intent.getStringExtra("name")
        pass = intent.getStringExtra("pass")
        userid = intent.getStringExtra("userid")
        user = intent.getStringExtra("user")
        barang = intent.getStringExtra("barang")
        beli = intent.getStringExtra("beli")
        jual = intent.getStringExtra("jual")
        koreksi = intent.getStringExtra("koreksi")
        laporan = intent.getStringExtra("laporan")
        sharedPreferences = this.getSharedPreferences(PREFS_FILENAME, 0)
        masteruser = findViewById<View>(R.id.user) as LinearLayout
        masterbarang = findViewById<View>(R.id.barang) as LinearLayout
        logout = findViewById<View>(R.id.logout) as LinearLayout
        lokasi = findViewById<View>(R.id.log) as LinearLayout

        val carouselView = findViewById(R.id.carouselView) as CarouselView;
        carouselView.setPageCount(sampleImages.size);
        carouselView.setImageListener(imageListener);

        logout!!.setOnClickListener {
            val editor = sharedPreferences!!.edit()
            editor.putBoolean("session_status",false)
            editor.apply();
            val intent = Intent(this@Dashboard, Login::class.java)
            finish()
            startActivity(intent)
        }

        masteruser!!.setOnClickListener {
            if (user.equals("1", ignoreCase = true)) {
                KirimData()
            } else {
                Toast.makeText(this@Dashboard, "Anda Tidak Memiliki Akses", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        masterbarang!!.setOnClickListener {
            KirimBarang()
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private fun KirimData() {
        val intent = Intent(this@Dashboard, Profile::class.java)
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

    private fun KirimBarang() {
        val intent = Intent(this@Dashboard, Barang::class.java)
        intent.putExtra("userid", userid)
        finish()
        startActivity(intent)
    }


    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            // You can use Glide or Picasso here
            imageView.setImageResource(sampleImages[position])
        }
    }
}
