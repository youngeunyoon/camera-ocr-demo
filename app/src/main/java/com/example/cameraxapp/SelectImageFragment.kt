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
import androidx.fragment.app.Fragment
import com.example.cameraxapp.databinding.FragmentSelectImageBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.FileNotFoundException
import java.io.InputStream

class SelectImageFragment : Fragment() {
    private lateinit var binding: FragmentSelectImageBinding
    private var imagePath: Uri? = null

//    companion object {
//        const val REQUEST_CODE = 2
//    }

    companion object {
        private const val ARG_IMAGE_PATH = "imagePath"

        fun newInstance(imagePath: Uri): SelectImageFragment {
            val fragment = SelectImageFragment()
            val args = Bundle().apply {
                putParcelable(ARG_IMAGE_PATH, imagePath)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var imageView: ImageView
    private var uri: Uri? = null
    private var bitmap: Bitmap? = null
    private var image: InputImage? = null
    private lateinit var recognizer: TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getParcelable(ARG_IMAGE_PATH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val binding = inflater.inflate(R.layout.fragment_select_image, container, false)
//        val imageView: ImageView = binding.findViewById(R.id.imageView)
//
//        imagePath?.let {
//            imageView.setImageURI(it)
//        }
//
//        return binding

        binding = FragmentSelectImageBinding.inflate(inflater)

        imagePath?.let {
            binding.imageView.setImageURI(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageView)
//        textInfo = view.findViewById(R.id.text_info)
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

//        val btnGetImage: Button = view.findViewById(R.id.btn_get_image)
//        btnGetImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = MediaStore.Images.Media.CONTENT_TYPE
//            startActivityForResult(intent, ResultOcrFragment.REQUEST_CODE)
//        }
//
//        val btnDetectionImage: Button = view.findViewById(R.id.btn_detection_image)
//        btnDetectionImage.setOnClickListener { textRecognition(recognizer) }

//        val buttonOcr: Button = view.findViewById(R.id.ocrButton)
//        buttonOcr.setOnClickListener {
//            textRecognition(recognizer)
//        }
        binding.ocrButton.setOnClickListener {
            textRecognition(recognizer)
        }

        binding.retryButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, CameraFragment())
//                .addToBackStack(null)
                .commit()
        }
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
//                    textInfo.text = resultText
                    val resultFragment = ResultOcrFragment.newInstance(resultText)
                    // 코드로 프래그먼트 교체
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, resultFragment)
//                        .addToBackStack(null)
                        .commit()
                }
                .addOnFailureListener { e ->
                    Log.e("텍스트 인식", "실패: ${e.message}")
                    val resultText = "텍스트 인식 실패"
//                    textInfo.text = resultText
                    val resultFragment = ResultOcrFragment.newInstance(resultText)
                    // 코드로 프래그먼트 교체
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, resultFragment)
//                        .addToBackStack(null)
                        .commit()
                }
        }
    }
}
