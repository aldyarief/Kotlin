package com.example.kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class KategoriBarang : AppCompatActivity() {

    var txt_name: EditText? = null
    var imageView: ImageView? = null
    var buttonChoose: Button? = null
    var bitmap: Bitmap? = null
    var decoded:Bitmap? = null
    var success = 0
    var PICK_IMAGE_REQUEST = 1
    var bitmap_size = 60 // range 1 - 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_barang)

        buttonChoose = findViewById<View>(R.id.buttonChoose) as Button
        imageView = findViewById<View>(R.id.imageView) as ImageView

        buttonChoose!!.setOnClickListener { showFileChooser() }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, Barang::class.java))
        finish()
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    fun getStringImage(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap!!, 512))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun kosong() {
        imageView!!.setImageResource(0)
        txt_name!!.setText(null)
    }

    private fun setToImageView(bmp: Bitmap) {
        //compress image
        val bytes = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes)
        decoded = BitmapFactory.decodeStream(ByteArrayInputStream(bytes.toByteArray()))

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView!!.setImageBitmap(decoded)
    }

    // fungsi resize image
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }


}