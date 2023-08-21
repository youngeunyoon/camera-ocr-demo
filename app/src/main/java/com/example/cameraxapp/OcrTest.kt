package com.example.cameraxapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.FileNotFoundException
import java.io.InputStream

class OcrTest : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 2
    }

    private lateinit var imageView: ImageView
    private var uri: Uri? = null
    private var bitmap: Bitmap? = null
    private var image: InputImage? = null
    private lateinit var textInfo: TextView
    private lateinit var recognizer: TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ocr_test)

        imageView = findViewById(R.id.imageView)
        textInfo = findViewById(R.id.textInfo)
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val btnGetImage: Button = findViewById(R.id.resetButton)
        btnGetImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_CODE)
        }

        val btnDetectionImage: Button = findViewById(R.id.ocrButton)
        btnDetectionImage.setOnClickListener { textRecognition(recognizer) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            uri = data?.data
            uri?.let { setImage(it) }
        }
    }

    private fun setImage(uri: Uri) {
        try {
            val `in`: InputStream? = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(`in`)
            imageView.setImageBitmap(bitmap)

            bitmap?.let {
                image = InputImage.fromBitmap(it, 0)
            }
            Log.e("setImage", "이미지 to 비트맵")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun textRecognition(recognizer: TextRecognizer) {
        image?.let {
            recognizer.process(it)
                .addOnSuccessListener { visionText ->
                    Log.e("텍스트 인식", "성공")
                    val resultText = visionText.text
                    textInfo.text = resultText
                }
                .addOnFailureListener { e ->
                    Log.e("텍스트 인식", "실패: ${e.message}")
                }
        }
    }
}