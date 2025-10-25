package com.example.facesecure.ml

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FaceEmbeddingExtractor(context: Context, modelPath: String = "facenet.tflite") {

    private val interpreter: Interpreter

    init {
        val modelBytes = context.assets.open(modelPath).readBytes()
        val buffer = ByteBuffer.allocateDirect(modelBytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(modelBytes)
        buffer.rewind()
        interpreter = Interpreter(buffer)
    }

    fun getEmbedding(faceBitmap: Bitmap): FloatArray {
        val input = bitmapToFloatBuffer(faceBitmap)
        val output = Array(1) { FloatArray(512) }
        interpreter.run(input, output)
        return output[0]
    }

    private fun bitmapToFloatBuffer(bitmap: Bitmap): ByteBuffer {
        val inputWidth = 160
        val inputHeight = 160
        val resized = Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true)

        val buffer = ByteBuffer.allocateDirect(1 * inputWidth * inputHeight * 3 * 4)
        buffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputWidth * inputHeight)
        resized.getPixels(intValues, 0, inputWidth, 0, 0, inputWidth, inputHeight)

        for (pixelValue in intValues) {
            val r = ((pixelValue shr 16) and 0xFF) / 255.0f
            val g = ((pixelValue shr 8) and 0xFF) / 255.0f
            val b = (pixelValue and 0xFF) / 255.0f
            buffer.putFloat((r - 0.5f) * 2f)
            buffer.putFloat((g - 0.5f) * 2f)
            buffer.putFloat((b - 0.5f) * 2f)
        }

        buffer.rewind()
        return buffer
    }
}


