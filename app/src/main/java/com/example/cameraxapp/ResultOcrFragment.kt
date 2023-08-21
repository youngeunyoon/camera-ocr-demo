package com.example.cameraxapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cameraxapp.databinding.FragmentResultOcrBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.FileNotFoundException
import java.io.InputStream

class ResultOcrFragment : Fragment() {
    private lateinit var binding: FragmentResultOcrBinding

//    companion object {
//        const val REQUEST_CODE = 2
//    }

    companion object {
        private const val ARG_OCR_RESULT = "ocrResult"

        fun newInstance(result: String): ResultOcrFragment {
            val fragment = ResultOcrFragment()
            val args = Bundle().apply {
                putString(ARG_OCR_RESULT, result)
            }
            fragment.arguments = args
            return fragment
        }
    }

//    private lateinit var imageView: ImageView
    private var uri: Uri? = null
    private var bitmap: Bitmap? = null
    private var image: InputImage? = null
//    private lateinit var textInfo: TextView
    private lateinit var recognizer: TextRecognizer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultOcrBinding.inflate(inflater)
//        return inflater.inflate(R.layout.fragment_result_ocr, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        imageView = view.findViewById(R.id.imageView)
//        textInfo = view.findViewById(R.id.text_info)
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

//        val btnGetImage: Button = view.findViewById(R.id.btn_get_image)
//        btnGetImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = MediaStore.Images.Media.CONTENT_TYPE
//            startActivityForResult(intent, REQUEST_CODE)
//        }
//
//        val btnDetectionImage: Button = view.findViewById(R.id.btn_detection_image)
//        btnDetectionImage.setOnClickListener { textRecognition(recognizer) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            uri = data?.data
            uri?.let { setImage(it) }
        }
    }

    private fun setImage(uri: Uri) {
        try {
            val `in`: InputStream? = requireActivity().contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(`in`)
            binding.imageView.setImageBitmap(bitmap)

            bitmap?.let {
                image = InputImage.fromBitmap(it, 0)
            }
            Log.e("setImage", "이미지 to 비트맵")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}
