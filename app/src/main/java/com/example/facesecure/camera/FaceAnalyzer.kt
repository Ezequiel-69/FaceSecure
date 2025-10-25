package com.example.facesecure.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.facesecure.ml.FaceEmbeddingExtractor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceAnalyzer(
    context: Context,
    private val onFaceDetected: (FloatArray) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .enableTracking()
        .build()

    private val detector = FaceDetection.getClient(options)
    private val extractor = FaceEmbeddingExtractor(context)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val bitmap = bitmapFromMediaImage(mediaImage, rotationDegrees)
                        val faceBitmap = cropFace(bitmap, faces[0])
                        val embedding = extractor.getEmbedding(faceBitmap)
                        onFaceDetected(embedding)
                    }
                }
                .addOnFailureListener { it.printStackTrace() }
                .addOnCompleteListener { imageProxy.close() }
        } else {
            imageProxy.close()
        }
    }

    /** Convierte un Image (YUV_420_888) a Bitmap */
    private fun bitmapFromMediaImage(mediaImage: android.media.Image, rotationDegrees: Int): Bitmap {
        val yBuffer = mediaImage.planes[0].buffer
        val vuBuffer = mediaImage.planes[2].buffer

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()
        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        val yuvImage = android.graphics.YuvImage(
            nv21,
            android.graphics.ImageFormat.NV21,
            mediaImage.width,
            mediaImage.height,
            null
        )

        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            android.graphics.Rect(0, 0, mediaImage.width, mediaImage.height),
            100,
            out
        )
        val imageBytes = out.toByteArray()
        var bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        val matrix = android.graphics.Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        bitmap = android.graphics.Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return bitmap
    }

    private fun cropFace(bitmap: Bitmap, face: Face): Bitmap {
        val rect = face.boundingBox
        val left = rect.left.coerceAtLeast(0)
        val top = rect.top.coerceAtLeast(0)
        val width = rect.width().coerceAtMost(bitmap.width - left)
        val height = rect.height().coerceAtMost(bitmap.height - top)
        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }
}
