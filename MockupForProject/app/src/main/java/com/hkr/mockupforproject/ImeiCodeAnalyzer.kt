package com.hkr.mockupforproject

import android.graphics.Rect
import android.util.Log
import android.util.Size
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class ImeiCodeAnalyzer(
    private val onImeiCodesDetected: (imeis: List<Long>) -> Unit,
    private val rect: State<Rect>,
    private val previewSize: State<Size>
) : ImageAnalysis.Analyzer {
    private val imeiLength = 15
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .enableAllPotentialBarcodes()
            .build()
    )
    private val textscanner = TextRecognition.getClient(
        TextRecognizerOptions.DEFAULT_OPTIONS
    )

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
        val rect = rect.value

        val previewWidth = previewSize.value.width
        val previewHeight =  previewSize.value.height / 1.5

        val scaleX = rect.width().toFloat() / previewWidth
        val scaleY = rect.height().toFloat() / previewHeight

        val adjustedRect = Rect()
        adjustedRect.left = rect.left
        adjustedRect.top = mediaImage.height - rect.top
        adjustedRect.right = rect.right
        adjustedRect.bottom = rect.bottom


        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val imeis = barcodes.mapNotNull { barcode ->
                    barcode.boundingBox?.let {boundingBox ->
                        // Scale the bounding box to fit your rectangle
//                        val scaledBox = Rect(
//                            (boundingBox.left * scaleX).toInt(),
//                            (mediaImage.height - boundingBox.top * scaleY).toInt(),
//                            (boundingBox.right * scaleX).toInt(),
//                            (boundingBox.bottom * scaleY).toInt()
//                        )
//
//                        Log.d("ImeiCodeAnalyzer", "Original box: $boundingBox, Scaled box: $scaledBox, MyRect: $rect")

                        if (adjustedRect.contains(boundingBox)) {
                            try {
                                barcode.rawValue?.takeIf {
                                    it.isNotBlank() && it.length == imeiLength && it.all { char -> char.isDigit() }
                                }?.toLong()
                            } catch (ex: NumberFormatException) {
                                null
                            }
                        } else {
                            null
                        }
                    }
                }
                onImeiCodesDetected(imeis)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }

        textscanner.process(image)
            .addOnSuccessListener {text ->
                for (block in text.textBlocks) {
                    for (line in block.lines) {
                        val lineText = line.text
                        if(lineText.length == imeiLength) {
                            var imeis: List<Long> = listOf()
                            try {
                                imeis = listOf(lineText.toLong())
                            } catch (e : NumberFormatException) { null }
                            if(imeis.isNotEmpty()) {
                                onImeiCodesDetected(imeis)
                            } else null
                        }

                        // I leave all these examples in here in case we want to work with it later
                        /*
                        val blockText = block.text
                        val blockCornerPoints = block.cornerPoints
                        val blockFrame = block.boundingBox
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                        }
                        */
                    }
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
